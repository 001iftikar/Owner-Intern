package com.iftikar.ownerintern.presentation.search_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iftikar.ownerintern.domain.DataError
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
class SearchScreenViewModel @Inject constructor(
    private val destinationRepository: DestinationRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SearchScreenState())
    val state = _state.asStateFlow()

    init {
        getAllDestinations()
    }

    fun onAction(action: SearchScreenAction) {
        when(action) {
            is SearchScreenAction.OnQueryChange -> {
                _state.update {
                    it.copy(queryText = action.query)
                }
            }
            is SearchScreenAction.OnDestinationSearch -> {
                if (action.query == "") {
                    _state.update {
                        it.copy(queryText = action.query)
                    }
                }
                getQueriedDestination(action.query)
            }
        }
    }
    private fun getAllDestinations() {
        viewModelScope.launch {
            setLoading()
            destinationRepository.getAllDestinations().collect { result ->
                result.onSuccess { destinations ->
                    _state.update {
                        it.copy(isLoading = false, destinations = destinations, error = null)
                    }
                }.onError { error ->
                    when(error) {
                        DataError.Remote.PARSING_ERROR -> setError("Fetching destinations failed, please try again later")
                        DataError.Remote.UNKNOWN -> setError("Some unexpected error occurred")
                        DataError.Remote.SERVER -> setError("This is a server error, please try again later!")
                    }
                }
            }
        }
    }

    private fun getQueriedDestination(query: String) {
        viewModelScope.launch {
            val normalizedQuery = query.trim().lowercase()
            destinationRepository.getAllDestinations().collect { result ->
                 result.onSuccess { destinations ->
                     val filteredDestinations = destinations.filter { destination ->
                        destination.name.contains(normalizedQuery, ignoreCase = true)
                    }

                     _state.update {
                         it.copy(
                             isLoading = false,
                             destinations = filteredDestinations,
                             error = null
                         )
                     }
                }
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




































