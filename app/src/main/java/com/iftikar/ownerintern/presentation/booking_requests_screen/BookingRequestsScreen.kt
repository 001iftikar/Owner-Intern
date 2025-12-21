package com.iftikar.ownerintern.presentation.booking_requests_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iftikar.ownerintern.presentation.components.TopBarComponent
import com.iftikar.ownerintern.ui.theme.LightOrange

@Composable
fun BookingRequestsScreen(
    viewModel: BookingRequestScreenViewModel = hiltViewModel(),
    destinationName: String,
    destinationId: String
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction
    LaunchedEffect(Unit) {
        viewModel.getBooking(destinationId)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarComponent(
                content = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically)
                    ) {
                        Text(
                            text = destinationName,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Active Now",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Normal,
                            color = Color.Green
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = LightOrange)
            } else {
                if (state.error != null) {
                    Text(
                        text = state.error!!,
                        style = MaterialTheme.typography.titleMedium,
                        color = Red
                    )
                } else {
                    if (state.booking == null && !state.message) {
                        Text(
                            text = "No requests available",
                            style = MaterialTheme.typography.titleMedium,
                            color = LightOrange
                        )
                    } else {
                        RequestCard(
                            modifier = Modifier.padding(horizontal = 60.dp),
                            customerName = state.customerName,
                            messageText = if (state.isAccepted == true) "Accepted Successfully" else "Rejected Successfully",
                            isAccepted = state.message,
                            onAccept = {
                                onAction(
                                    BookingRequestScreenAction.OnBookingAcceptOrDeclined(
                                        it
                                    )
                                )
                            },
                            onDecline = {
                                onAction(
                                    BookingRequestScreenAction.OnBookingAcceptOrDeclined(
                                        it
                                    )
                                )
                            },
                            onDone = {
                                onAction(BookingRequestScreenAction.AcceptedOrRejectedMessage)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RequestCard(
    modifier: Modifier = Modifier,
    customerName: String,
    messageText: String,
    isAccepted: Boolean,
    onAccept: (Boolean) -> Unit,
    onDecline: (Boolean) -> Unit,
    onDone: () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.LightGray
        )
    ) {
        Column(
            modifier = Modifier
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = if (!isAccepted) "New Request from $customerName" else messageText
            )

            if (!isAccepted) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Blue)
                            .clickable(
                                onClick = { onAccept(true) }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Blue)
                            .clickable(
                                onClick = { onDecline(false) }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Red
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Blue)
                        .clickable(
                            onClick = onDone
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }
    }
}






































