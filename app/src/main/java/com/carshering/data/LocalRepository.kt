package com.carshering.data

import com.carshering.domain.entity.Car

object LocalRepository {

    private var savedCars: List<Car>? = null

    fun saveCars(cars: List<Car>) {
        savedCars = cars
    }

    fun getCars(): List<Car>? {
        return savedCars
    }
}