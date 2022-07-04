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
    override fun getAllCarsFromServer(
        onSuccess: (List<Car>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        executor.execute {
            try {
                val serverResponseData = httpClient.get(CAR_URL)
                val cars = JsonToCarListAdapter(serverResponseData).fromJson()

                localRepo.saveCars(cars)
                handler.post { onSuccess(cars) }
            } catch (error: Exception) {
                error.printStackTrace()
                handler.post { onError(Exception()) }
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

