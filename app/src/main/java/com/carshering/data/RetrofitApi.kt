package com.carshering.data

import com.carshering.domain.entity.CarsDataFromServer
import com.carshering.domain.entity.RoutesDataFromServer
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitApi {
    @GET
    suspend fun getCars(@Url url: String): CarsDataFromServer

    @GET
    suspend fun getRoute(@Url url: String): RoutesDataFromServer
}