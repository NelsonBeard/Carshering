package com.carshering.data.cars

import com.carshering.domain.entity.Car
import com.carshering.domain.usecase.JsonAdapter
import org.json.JSONArray
import org.json.JSONObject

class JsonToCarListAdapter(
    private val serverResponseData: String
) : JsonAdapter<List<Car>> {

    override fun fromJson(): List<Car> {
        val cars = mutableListOf<Car>()
        val jsonArray = convertStringToJsonArray()

        for (i in 0 until jsonArray.length()) {
            val car = JsonToCarEntityAdapter(jsonArray.getJSONObject(i)).fromJson()
            cars.add(car)
        }
        return cars
    }

    private fun convertStringToJsonArray(): JSONArray {
        return JSONObject(serverResponseData).getJSONArray("cars")
    }

}