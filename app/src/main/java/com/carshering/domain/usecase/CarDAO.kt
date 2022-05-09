package com.carshering.domain.usecase

import com.carshering.domain.entity.Car

interface CarDAO {
    fun getAllCars(onSuccess: (cars: List<Car>) -> Unit)
}