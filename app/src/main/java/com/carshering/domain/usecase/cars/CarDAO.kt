package com.carshering.domain.usecase.cars

import com.carshering.domain.entity.Car

interface CarDAO {
    fun getAllCars(onSuccess: (List<Car>) -> Unit, onError: (Exception) -> Unit)
    fun saveCarsToLocalRepo(cars: List<Car>)
}