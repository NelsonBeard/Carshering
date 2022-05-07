package com.carshering.data

import com.carshering.domain.entity.Car
import org.json.JSONObject

class JsonConverterDTO {
    fun fromJsonToCar(jsonCar: JSONObject): Car {
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
}