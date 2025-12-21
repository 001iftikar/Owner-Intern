package com.iftikar.ownerintern.domain

sealed interface DataError : Error {
    enum class Remote : DataError {
        PARSING_ERROR,
        UNKNOWN,
        SERVER
    }
}