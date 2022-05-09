package com.carshering.ui


import com.carshering.data.CarDAOImpl
import com.carshering.domain.entity.Car

class Presenter : Contract.Presenter {
    override fun getCars(): List<Car> {
        CarDAOImpl().getAllCars{

        }
        return
    }
}
