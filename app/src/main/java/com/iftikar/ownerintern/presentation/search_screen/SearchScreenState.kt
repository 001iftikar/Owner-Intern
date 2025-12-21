package com.iftikar.ownerintern.presentation.search_screen

import com.iftikar.ownerintern.domain.model.Destination

data class SearchScreenState(
    val queryText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val destinations: List<Destination> = emptyList()
)
