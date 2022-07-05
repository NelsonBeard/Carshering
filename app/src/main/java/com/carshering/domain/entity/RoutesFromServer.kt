package com.carshering.domain.entity

import com.google.gson.annotations.SerializedName

data class RoutesFromServer(
    @SerializedName("routes") val routes: List<Route>
)

data class Route(
    @SerializedName("geometry") val geometry: String
)

