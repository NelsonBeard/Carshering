package com.carshering.data.cars

import com.carshering.domain.entity.Car
import com.carshering.domain.usecase.JsonAdapter
import com.carshering.exception.JsonParseException
import org.json.JSONObject

open class JsonToCarEntityAdapter(
    private val json: JSONObject
) : JsonAdapter<Car> {

    override fun fromJson(): Car {
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
            throw JsonParseException("Can't parse json")
        }
    }
}