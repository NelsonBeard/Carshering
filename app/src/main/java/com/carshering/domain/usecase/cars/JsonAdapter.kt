package com.carshering.domain.usecase.cars

interface JsonAdapter<T> {
    fun fromJson():T
}