package com.carshering.data

import com.carshering.domain.entity.CarsFromServer
import com.carshering.domain.entity.RoutesFromServer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitApi {
    @GET
    fun getCars(@Url url: String): Call<CarsFromServer>

    @GET
    fun getRoute(@Url url: String): Call<RoutesFromServer>
}