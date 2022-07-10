package com.carshering

import com.carshering.data.RetrofitApi
import com.carshering.data.RetrofitClient
import com.carshering.data.cars.CarsDataLocal
import com.carshering.data.route.RoutesDataLocal
import com.carshering.domain.entity.CarsData
import com.carshering.domain.entity.RoutesData
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder

object StoreGraph {
    private val retrofit: RetrofitApi = RetrofitClient().createRetrofit()

    fun provideCarsStore(): Store<String, CarsData> {
        val cdl = CarsDataLocal

        return StoreBuilder
            .from(
                Fetcher.of { url: String ->
                    retrofit.getCars(url)
                },
                sourceOfTruth = SourceOfTruth.of(
                    nonFlowReader = { cdl.loadCarsData() },
                    writer = { _, carsData: CarsData -> cdl.saveCarsData(carsData) }
                )
            )
            .build()
    }

    fun provideRoutesStore(): Store<String, RoutesData> {
        val rdl = RoutesDataLocal

        return StoreBuilder
            .from(
                Fetcher.of { url: String ->
                    retrofit.getRoute(url)
                },
                sourceOfTruth = SourceOfTruth.of(
                    nonFlowReader = { key -> rdl.loadRoutesData(key) },
                    writer = { key, routes: RoutesData -> rdl.saveRoutesData(key, routes) }
                )
            )
            .build()
    }
}
