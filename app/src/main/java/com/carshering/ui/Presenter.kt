package com.carshering.ui

import com.carshering.data.ServerRequestDAO
import com.carshering.domain.entity.Car
import org.json.JSONObject

class Presenter() {

    fun initCarsList(): List<Car> {
        val cars = mutableListOf<Car>()
        val jsonCarArray = ServerRequestDAO().getServerResponseData().getJSONArray("cars")

        for (i in 0 until jsonCarArray.length()) {
            val car = fromJsonToCar(jsonCarArray.getJSONObject(i))
            cars.add(car)
        }
        return cars
    }
}

private fun fromJsonToCar(jsonCar: JSONObject): Car {
    return Car(
        id = jsonCar.getString("id"),
        registrationNumber = jsonCar.getString("registrationNumber"),
        model = jsonCar.getString("model"),
        color = jsonCar.getString("color"),
        picture = jsonCar.getString("picture"),
        transmission = jsonCar.getString("transmission"),
        remainRange = jsonCar.getInt("remainRange"),
        seats = jsonCar.getInt("seats"),
        lat = jsonCar.getJSONObject("location").getDouble("lat"),
        lng = jsonCar.getJSONObject("location").getDouble("lng")
    )
}
