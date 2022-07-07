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
    private val localRepo: CarsLocalRepository,
    private val store: StoreGraph,
    private val scope: CoroutineScope
) : CarDAO {

    override fun getAllCarsFromServer(
        onSuccess: (List<Car>) -> Unit,
        onError: (Int) -> Unit
    ) {
        scope.launch {
            try {
                val carsDataFromServer = store.provideCarsStore().get(CAR_URL)
                localRepo.saveCars(carsDataFromServer.cars)
                onSuccess(carsDataFromServer.cars)
            } catch (error: Exception) {
                onError(R.string.error_cant_get_data_toast)
            }
        }
    }

    override fun getSingleCarFromLocalRepo(clickedCarId: String): Car? {
        val savedCars = localRepo.getCars()
        val clickedCar = savedCars.firstOrNull {
            clickedCarId == it.id
        }
        return clickedCar
    }
}

