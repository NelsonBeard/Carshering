package com.carshering.data.route

import com.carshering.domain.entity.Route
import com.carshering.domain.usecase.JsonAdapter
import com.carshering.exception.JsonParseException
import org.json.JSONObject

class JsonToRouteAdapter(
    private val json: JSONObject
) : JsonAdapter<Route> {

    @Throws(JsonParseException::class)
    override fun fromJson(): Route {
        try {
            return Route(geometry = json.getString("geometry"))
        } catch (error: Exception) {
            throw JsonParseException("Can't parse json", error)
        }
    }
}
