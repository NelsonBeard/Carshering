package com.carshering.domain.entity

import com.google.gson.annotations.SerializedName

data class CarsData(
    @SerializedName("cars") val cars: List<Car>
)

data class Car(
    @SerializedName("id") val id: String,
    @SerializedName("registrationNumber") val registrationNumber: String,
    @SerializedName("location") val location: Location,
    @SerializedName("model") val model: String,
    @SerializedName("color") val color: String,
    @SerializedName("picture") val picture: String,
    @SerializedName("transmission") val transmission: String,
    @SerializedName("remainRange") val remainRange: Int,
    @SerializedName("seats") val seats: Int,

    )

data class Location(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)