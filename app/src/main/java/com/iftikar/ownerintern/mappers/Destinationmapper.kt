package com.iftikar.ownerintern.mappers

import com.iftikar.ownerintern.data.dto.DestinationResponseDto
import com.iftikar.ownerintern.domain.model.Destination

fun DestinationResponseDto.toDestination(): Destination {
    return Destination(
        id = id,
        name = name,
        location = location,
        description = description,
        cost = cost.toString(),
        rating = rating.toString(),
        ratingCount = ratingCount.toString(),
        isActive = isActive,
        imageUrl = imageUrl
    )
}