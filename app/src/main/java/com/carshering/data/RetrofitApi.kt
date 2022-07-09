package com.carshering.data

import com.carshering.domain.entity.CarsData
import com.carshering.domain.entity.RoutesData
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitApi {
    @GET
    suspend fun getCars(@Url url: String): CarsData

    @GET
    suspend fun getRoute(@Url url: String): RoutesData
}