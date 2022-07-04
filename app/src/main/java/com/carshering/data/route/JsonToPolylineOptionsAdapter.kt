package com.carshering.data.route

import com.carshering.R
import com.carshering.domain.usecase.JsonAdapter
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import org.json.JSONObject

class JsonToPolylineOptionsAdapter(
    private val serverResponseData: String
) : JsonAdapter<Pair<PolylineOptions, LatLngBounds>> {

    private val line = PolylineOptions()

    override fun fromJson(): Pair<PolylineOptions, LatLngBounds> {
        val jsonObjectWithRoute = convertStringToJsonObject()
        val route = JsonToRouteAdapter(jsonObjectWithRoute).fromJson()
        val routePoints = PolyUtil.decode(route.geometry)

        val latLngBuilder = LatLngBounds.Builder()

        line.apply {
            color(R.color.route_color)
            routePoints.forEach {
                add(it)
                latLngBuilder.include(it)
            }
        }

        val latLngBounds = latLngBuilder.build()
        return Pair(line, latLngBounds)
    }

    private fun convertStringToJsonObject(): JSONObject {
        val wholeJsonObject = JSONObject(serverResponseData)
        val routesArray = wholeJsonObject.getJSONArray("routes")

        return routesArray.getJSONObject(0)
    }
}