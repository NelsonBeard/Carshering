package com.carshering.domain.usecase

interface JsonAdapter<T> {
    fun fromJson():T
}