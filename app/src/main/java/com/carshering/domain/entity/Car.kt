package com.carshering.domain.entity

data class Car(
    val id: String,
    val registrationNumber: String,
    val location: Location,
    val model: String,
    val color: String,
    val picture: String,
    val transmission: String,
    val remainRange: Int,
    val seats: Int
)
class Location(
    val lat: Float,
    val lng: Float
)
