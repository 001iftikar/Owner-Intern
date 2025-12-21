package com.iftikar.ownerintern.presentation.booking_requests_screen

import com.iftikar.ownerintern.domain.model.Booking

data class BookingRequestsScreenState(
    val isLoading: Boolean = false,
    val booking: Booking? = Booking(),
    val bookingId: String = "", // had to do this because in booking document id filed is empty string todo: which is should be cleared
    val customerName: String = "",
    val error: String? = null,
    val isAccepted: Boolean? = null,
    val message: Boolean = false // to show the accepted or rejected message, since fb is realtime, it does not show the message whether accepted or rejected
)