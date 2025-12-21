package com.iftikar.ownerintern.domain.model

data class Destination(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val description: String = "",
    val cost: String = "",
    val rating: String = "",
    val ratingCount: String = "",
    val isActive: Boolean = false,
    val imageUrl: String = "",
)
