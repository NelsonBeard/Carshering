package com.carshering.data.cars

import com.carshering.domain.entity.CarsData

object CarsDataLocal {
    private var carsData: CarsData? = null

    fun saveCarsData(data: CarsData) {
        carsData = data
    }

    fun loadCarsData(): CarsData? {
        return carsData
    }
}