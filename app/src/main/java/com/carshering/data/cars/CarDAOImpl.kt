package com.carshering.data.cars

import com.carshering.R
import com.carshering.domain.entity.Car
import com.carshering.domain.entity.CarsFromServer
import com.carshering.data.RetrofitApi
import com.carshering.domain.usecase.cars.CarDAO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val CAR_URL =
    "https://raw.githubusercontent.com/NelsonBeard/CarsheringAPI/master/cars.json"

class CarDAOImpl(
    private val localRepo: CarsLocalRepository,
    private val retrofit: RetrofitApi
) : CarDAO {
    override fun getAllCarsFromServer(
        onSuccess: (List<Car>) -> Unit,
        onError: (Int) -> Unit
    ) {
        retrofit.getCars(CAR_URL).enqueue(object : Callback<CarsFromServer> {
            override fun onResponse(
                call: Call<CarsFromServer>,
                response: Response<CarsFromServer>
            ) {
                if (response.isSuccessful) {
                    localRepo.saveCars(response.body()!!.cars)
                    onSuccess(response.body()!!.cars)
                } else {
                    Exception().printStackTrace()
                    onError(R.string.error_cant_get_data_toast)
                }
            }

            override fun onFailure(call: Call<CarsFromServer>, t: Throwable) {
                onError(R.string.error_no_internet_connection_toast)
            }
        })
    }

    override fun getSingleCarFromLocalRepo(clickedCarId: String): Car? {
        val savedCars = localRepo.getCars()
        val clickedCar = savedCars.firstOrNull {
            clickedCarId == it.id
        }
        return clickedCar
    }
}

