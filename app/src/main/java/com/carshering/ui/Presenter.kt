package com.carshering.ui

import android.os.Handler
import android.os.Looper
import com.carshering.data.CarDAOImpl
import com.carshering.data.HttpClient

class Presenter : Contract.Presenter {

    private var view: Contract.View? = null
    private val carDAOImpl =
        CarDAOImpl(Handler(Looper.getMainLooper()), HttpClient())

    override fun onAttach(view: Contract.View) {
        this.view = view
    }

    override fun requestCars() {
        carDAOImpl.getAllCars(
            {
                view?.putMarks(it)
            },
            {
                view?.showErrorToast()
            }
        )
    }
}
