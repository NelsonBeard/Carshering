package com.carshering.domain.usecase.cars

import com.carshering.domain.entity.Car

interface CarDAO {
    fun getAllCars(onSuccess: (List<Car>) -> Unit, onError: (Int) -> Unit)
    fun getSingleCar(clickedCarId: String, onSuccess: (Car?) -> Unit)
}