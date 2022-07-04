package com.carshering.data.cars

import com.carshering.domain.entity.Car

object CarsLocalRepository {

    private var savedCars: List<Car> = emptyList()

    fun saveCars(cars: List<Car>) {
        savedCars = cars
    }

    fun getCars(): List<Car> {
        return savedCars
    }
}