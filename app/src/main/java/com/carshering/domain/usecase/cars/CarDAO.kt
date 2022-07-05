package com.carshering.domain.usecase.cars

import com.carshering.domain.entity.Car

interface CarDAO {
    fun getAllCarsFromServer(onSuccess: (List<Car>) -> Unit, onError: (Int) -> Unit)
    fun getSingleCarFromLocalRepo(clickedCarId: String): Car?
}