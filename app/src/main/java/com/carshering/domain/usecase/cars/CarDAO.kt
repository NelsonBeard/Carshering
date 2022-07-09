package com.carshering.domain.usecase.cars

import com.carshering.domain.entity.Car

interface CarDAO {
    suspend fun getAllCars(onSuccess: (List<Car>) -> Unit, onError: (Int) -> Unit)
    suspend fun getSingleCar(clickedCarId: String): Car?
}