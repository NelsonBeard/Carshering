package com.carshering.data

import android.os.Handler
import com.carshering.domain.entity.Car
import com.carshering.domain.usecase.CarDAO
import java.net.URL
import java.util.concurrent.Executor

const val CAR_URL =
    "https://raw.githubusercontent.com/NelsonBeard/CarsheringAPI/master/cars.json"

class CarDAOImpl(
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
                handOverToUIThreadSuccess(onSuccess)
            } catch (error: Exception) {
                error.printStackTrace()
                handOverToUIThreadError(onError)
            }
        }
    }

    private fun handOverToUIThreadSuccess(onSuccess: (List<Car>) -> Unit) {
        handler.post { onSuccess(cars) }
    }

    private fun handOverToUIThreadError(onError: (Exception) -> Unit) {
        handler.post { onError(Exception()) }
    }
}

