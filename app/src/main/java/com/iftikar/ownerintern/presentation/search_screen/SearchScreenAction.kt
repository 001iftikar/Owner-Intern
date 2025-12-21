package com.iftikar.ownerintern.presentation.search_screen

sealed interface SearchScreenAction {
    data class OnQueryChange(val query: String) : SearchScreenAction
    data class OnDestinationSearch(val query: String) : SearchScreenAction
}