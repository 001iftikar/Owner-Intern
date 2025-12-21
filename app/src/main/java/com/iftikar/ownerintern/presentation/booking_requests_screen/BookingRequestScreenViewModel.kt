package com.iftikar.ownerintern.presentation.booking_requests_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iftikar.ownerintern.domain.onError
import com.iftikar.ownerintern.domain.onSuccess
import com.iftikar.ownerintern.domain.repository.DestinationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingRequestScreenViewModel @Inject constructor(
    private val destinationRepository: DestinationRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BookingRequestsScreenState())
    val state = _state.asStateFlow()

    fun onAction(action: BookingRequestScreenAction) {
        when(action) {
            is BookingRequestScreenAction.OnBookingAcceptOrDeclined -> acceptOrDeclineBooking(action.isAccept)
            BookingRequestScreenAction.AcceptedOrRejectedMessage -> {
                _state.update {
                    it.copy(message = false, isAccepted = null)
                }
            }
        }
    }

    fun getBooking(destinationId: String) {
        viewModelScope.launch {
            setLoading()
            destinationRepository.getDestinationBooking(
                id = destinationId,
                getUserNameAndBookingId = { name, bookingId ->
                    _state.update {
                        it.copy(customerName = name, bookingId = bookingId)
                    }
                }).collect { result ->
                result.onSuccess { booking ->
                    _state.update {
                        it.copy(booking = booking, isLoading = false)
                    }
                }.onError { _ ->
                    setError("Oops something happened, please try again later")
                }
            }
        }
    }

    private fun acceptOrDeclineBooking(isAccepted: Boolean) {
        viewModelScope.launch {
            destinationRepository.acceptOrDeclineBooking(
                isAccept = isAccepted,
                id = _state.value.bookingId
            ).onSuccess {
                _state.update {
                    it.copy(message = true, isAccepted = isAccepted)
                }
            }.onError { _ ->
                setError("Request could not be handled, please try again!")
            }
        }
    }

    private fun setLoading() {
        _state.update {
            it.copy(isLoading = true, error = null)
        }
    }

    private fun setError(e: String) {
        _state.update {
            it.copy(isLoading = false, error = e)
        }
    }
}