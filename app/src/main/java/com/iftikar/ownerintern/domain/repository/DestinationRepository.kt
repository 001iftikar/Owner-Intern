package com.iftikar.ownerintern.domain.repository

import com.iftikar.ownerintern.domain.DataError
import com.iftikar.ownerintern.domain.EmptyResult
import com.iftikar.ownerintern.domain.Result
import com.iftikar.ownerintern.domain.model.Booking
import com.iftikar.ownerintern.domain.model.Destination
import kotlinx.coroutines.flow.Flow

interface DestinationRepository {
    fun getAllDestinations(): Flow<Result<List<Destination>, DataError>>
    fun getDestinationBooking(id: String, getUserNameAndBookingId: (String, String) -> Unit): Flow<Result<Booking?, DataError>>
    suspend fun acceptOrDeclineBooking(
        isAccept: Boolean,
        id: String
    ): EmptyResult<DataError>
}