package com.carshering.domain.entity


data class Car(
    val id: String,
    val registrationNumber: String,
    val model: String,
    val color: String,
    val picture: String,
    val transmission: String,
    val remainRange: Int,
    val seats: Int,
    val lat: Double,
    val lng: Double
)

