package com.carshering.ui

import com.carshering.domain.entity.Car

class Contract {

    interface View {
        fun putMarks(cars: List<Car>)
        fun showErrorToast()
    }

    interface Presenter {
        fun onAttach(view: View)
        fun requestCars()
    }
}