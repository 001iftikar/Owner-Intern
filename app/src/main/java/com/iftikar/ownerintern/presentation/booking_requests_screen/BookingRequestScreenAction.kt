package com.iftikar.ownerintern.presentation.booking_requests_screen

sealed interface BookingRequestScreenAction {
    data class OnBookingAcceptOrDeclined(val isAccept: Boolean) : BookingRequestScreenAction
    data object AcceptedOrRejectedMessage : BookingRequestScreenAction
}