package com.carshering.data.cars

import android.os.Handler
import com.carshering.data.HttpClient
import com.carshering.domain.entity.Car
import com.carshering.domain.usecase.cars.CarDAO
import java.util.concurrent.Executor

const val CAR_URL =
    "https://raw.githubusercontent.com/NelsonBeard/CarsheringAPI/master/cars.json"

class CarDAOImpl(
    private val localRepo: CarsLocalRepository,
    private val executor: Executor,
    private val handler: Handler,
    private val httpClient: HttpClient
) : CarDAO {
    private lateinit var cars: List<Car>

    override fun getAllCars(
        onSuccess: (List<Car>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        executor.execute {
            try {
                val serverResponseData = httpClient.get(CAR_URL)
                cars = JsonToCarListAdapter(serverResponseData).fromJson()

                saveCarsToLocalRepo(cars)
                handOverToUIThreadSuccess(onSuccess)
            } catch (error: Exception) {
                error.printStackTrace()
                handOverToUIThreadError(onError)
            }
        }
    }

    override fun saveCarsToLocalRepo(cars: List<Car>) {
        localRepo.saveCars(cars)
    }

    private fun handOverToUIThreadSuccess(onSuccess: (List<Car>) -> Unit) {
        handler.post { onSuccess(cars) }
    }

    private fun handOverToUIThreadError(onError: (Exception) -> Unit) {
        handler.post { onError(Exception()) }
    }
}

