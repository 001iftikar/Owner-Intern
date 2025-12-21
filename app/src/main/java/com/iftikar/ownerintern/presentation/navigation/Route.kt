package com.iftikar.ownerintern.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object SearchScreen : Route
    @Serializable
    data class BookingRequestsScreen(val destinationId: String, val name: String) : Route
}