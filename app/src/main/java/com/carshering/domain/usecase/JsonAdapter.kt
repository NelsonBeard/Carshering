package com.carshering.domain.usecase

interface JsonAdapter<T> {
    fun convertJson(json: String): T
}