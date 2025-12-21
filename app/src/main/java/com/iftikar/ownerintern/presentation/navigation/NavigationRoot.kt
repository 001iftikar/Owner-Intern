package com.iftikar.ownerintern.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.iftikar.ownerintern.presentation.booking_requests_screen.BookingRequestsScreen
import com.iftikar.ownerintern.presentation.search_screen.SearchScreen

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(Route.SearchScreen)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Route.SearchScreen> { _ ->
                SearchScreen(
                    onDestinationClick = { id, name ->
                        backStack.add(Route.BookingRequestsScreen(id, name))
                    }
                )
            }
            entry<Route.BookingRequestsScreen> { route ->
                BookingRequestsScreen(
                    destinationId = route.destinationId,
                    destinationName = route.name
                )
            }
        }
    )
}