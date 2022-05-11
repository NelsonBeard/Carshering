package com.carshering.domain.usecase

import com.carshering.domain.entity.Car
import java.lang.Exception

interface CarDAO {

    fun getAllCars(onSuccess: (List<Car>) -> Unit, onError: (Exception) -> Unit)
}