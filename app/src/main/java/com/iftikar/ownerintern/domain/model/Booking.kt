package com.iftikar.ownerintern.domain.model

data class Booking(
    val id: String = "",
    val userId: String = "",
    val destinationId: String = "",
    val status: BookingStatus = BookingStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis()
)

enum class BookingStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}

