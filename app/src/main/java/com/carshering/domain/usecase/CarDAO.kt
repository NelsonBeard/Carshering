package com.carshering.domain.usecase

import com.carshering.domain.entity.Car

interface CarDAO {
    fun getAllCars(onSuccess: (List<Car>) -> Unit, onError: (Exception) -> Unit)
}