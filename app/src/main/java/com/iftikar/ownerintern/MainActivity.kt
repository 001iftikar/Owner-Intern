package com.iftikar.ownerintern

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.iftikar.ownerintern.presentation.booking_requests_screen.BookingRequestsScreen
import com.iftikar.ownerintern.presentation.navigation.NavigationRoot
import com.iftikar.ownerintern.presentation.search_screen.SearchScreen
import com.iftikar.ownerintern.ui.theme.OwnerInternTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OwnerInternTheme {
                NavigationRoot()
            }
        }
    }
}
