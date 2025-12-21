package com.iftikar.ownerintern.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.iftikar.ownerintern.data.dto.DestinationResponseDto
import com.iftikar.ownerintern.domain.DataError
import com.iftikar.ownerintern.domain.EmptyResult
import com.iftikar.ownerintern.domain.Result
import com.iftikar.ownerintern.domain.model.Booking
import com.iftikar.ownerintern.domain.model.BookingStatus
import com.iftikar.ownerintern.domain.model.Destination
import com.iftikar.ownerintern.domain.model.User
import com.iftikar.ownerintern.domain.repository.DestinationRepository
import com.iftikar.ownerintern.mappers.toDestination
import com.iftikar.ownerintern.utils.FirebaseCollections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DestinationRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : DestinationRepository {
    override fun getAllDestinations(): Flow<Result<List<Destination>, DataError>> = callbackFlow<Result<List<Destination>, DataError>> {
        try {
            val listenerRegistration = db
                .collection(FirebaseCollections.DESTINATIONS)
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        trySend(Result.Error(DataError.Remote.SERVER))
                        return@addSnapshotListener
                    }

                    if (snapshot?.isEmpty == true) {
                        trySend(Result.Success(emptyList<Destination>()))
                    }

                    if (snapshot != null) {
                        val destinations = snapshot
                            .toObjects<DestinationResponseDto>()
                            .map { it.toDestination() }

                        trySend(Result.Success(destinations))
                    } else {
                        trySend(Result.Error(DataError.Remote.PARSING_ERROR))
                    }
                }

            awaitClose {
                listenerRegistration.remove()
            }
        } catch (ex: Exception) {
            Log.e("Destination", "getAllDestinations: ", ex)
            trySend(Result.Error(DataError.Remote.UNKNOWN))
        }
    }.flowOn(Dispatchers.IO)

    override fun getDestinationBooking(id: String, getUserNameAndBookingId: (String, String) -> Unit): Flow<Result<Booking?, DataError>> =
        callbackFlow<Result<Booking?, DataError>> {
            try {
                Log.d("Destination", "getDestinationBooking: Init")
                activateDestination(id)
                Log.d("Destination", "getDestinationBooking: Passed activation")
                val listener = db
                    .collection(FirebaseCollections.BOOKINGS)
                    .whereEqualTo("destinationId", id)
                    .whereEqualTo("status", BookingStatus.PENDING)
                    .orderBy("createdAt", Query.Direction.ASCENDING)
                    .addSnapshotListener { snapshot, error ->

                        if (error != null) {
                            // erro solved by creating indexes
                            Log.e("Destination", "getAllDestinations: ", error)
                            trySend(Result.Error(DataError.Remote.PARSING_ERROR))
                            return@addSnapshotListener
                        }
                        if (snapshot?.isEmpty == false) {
                            val bookingDoc = snapshot.documents.firstOrNull()
                            Log.d("Destination", "getDestinationBooking: Nonempty Snapshot")
                            if (bookingDoc != null) {
                                val bookingId = bookingDoc.id
                                val booking = bookingDoc
                                    .toObject<Booking>()
                                Log.d("Destination", "null check ${booking == null}: $booking")
                                if (booking != null) {
                                    db.collection(FirebaseCollections.USERS)
                                        .whereEqualTo("id", booking.userId)
                                        .get()
                                        .addOnSuccessListener { snapshots ->
                                            val user = snapshots
                                                .first()
                                                .toObject<User>()
                                            getUserNameAndBookingId(user.name, bookingId)
                                        }
                                        .addOnFailureListener {
                                            trySend(Result.Success(null))
                                        }
                                    trySend(Result.Success(booking))
                                } else {
                                    trySend(Result.Success(null))
                                }
                            } else {
                                trySend(Result.Success(null))
                            }


                        } else {
                            Log.d("Destination", "getDestinationBooking: Empty Snapshot")
                            trySend(Result.Success(null))
                        }
                    }
                awaitClose { listener.remove() }
            } catch (ex: Exception) {
                Log.e("Destination", "getAllDestinations: ", ex)
                trySend(Result.Error(DataError.Remote.UNKNOWN))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun acceptOrDeclineBooking(
        isAccept: Boolean,
        id: String
    ): EmptyResult<DataError> = withContext(Dispatchers.IO) {
        try {
            Log.d("Destination", "acceptOrDeclineBooking: Accept Init")
            Log.d("Destination", "acceptOrDeclineBooking: $id")
            val docRef = db.collection(FirebaseCollections.BOOKINGS)
                .document(id)
            if (isAccept) {
                Log.d("Destination", "acceptOrDeclineBooking: Accept sent")
                docRef.update("status", BookingStatus.ACCEPTED)
            } else {
                Log.d("Destination", "acceptOrDeclineBooking: Decline sent")
                docRef.update("status", BookingStatus.REJECTED)
            }
            Result.Success(Unit)
        } catch (ex: Exception) {
            Log.e("Destination", "acceptOrDeclineBooking: ", ex)
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    private fun activateDestination(id: String) {
        db.collection(FirebaseCollections.DESTINATIONS)
            .document(id)
            .update("isActive", true)
    }
}





































