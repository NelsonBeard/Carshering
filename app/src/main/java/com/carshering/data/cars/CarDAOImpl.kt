package com.carshering.data.cars

import com.carshering.R
import com.carshering.StoreGraph
import com.carshering.domain.entity.Car
import com.carshering.domain.usecase.cars.CarDAO
import com.dropbox.android.external.store4.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val CAR_URL =
    "https://raw.githubusercontent.com/NelsonBeard/CarsheringAPI/master/cars.json"

class CarDAOImpl(
    private val scope: CoroutineScope,
    private val store: StoreGraph
) : CarDAO {

    override fun getAllCars(
        onSuccess: (List<Car>) -> Unit,
        onError: (Int) -> Unit
    ) {
        scope.launch {
            try {
                val carsData = store.provideCarsStore().get(CAR_URL)
                onSuccess(carsData.cars)
            } catch (error: Exception) {
                onError(R.string.error_cant_get_data_toast)
            }
        }
    }

    override fun getSingleCar(
        clickedCarId: String,
        onSuccess: (Car?) -> Unit
    ) {
        scope.launch {
            val allCars = store.provideCarsStore().get(CAR_URL).cars
            val clickedCar = allCars.firstOrNull {
                clickedCarId == it.id
            }
            onSuccess(clickedCar)
        }
    }
}

