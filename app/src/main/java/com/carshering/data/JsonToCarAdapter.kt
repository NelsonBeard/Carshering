package com.carshering.data

import com.carshering.domain.entity.Car
import com.carshering.domain.usecase.JsonAdapter
import org.json.JSONObject

class JsonToCarAdapter : JsonAdapter<List<Car>> {

    override fun convertJson(json: String): List<Car> {
        val cars = mutableListOf<Car>()
        val jsonArray = JSONObject(json).getJSONArray("cars")

        for (i in 0 until jsonArray.length()) {
            val car = convertJson(jsonArray.getJSONObject(i))
            cars.add(car)
        }
        return cars
    }

    private fun convertJson(json: JSONObject): Car {
        try {
            return Car(
                id = json.getString("id"),
                registrationNumber = json.getString("registrationNumber"),
                model = json.getString("model"),
                color = json.getString("color"),
                picture = json.getString("picture"),
                transmission = json.getString("transmission"),
                remainRange = json.getInt("remainRange"),
                seats = json.getInt("seats"),
                lat = json.getJSONObject("location").getDouble("lat"),
                lng = json.getJSONObject("location").getDouble("lng")
            )
        } catch (error: Exception) {
            throw Exception("Can't parse json")
        }
    }
}