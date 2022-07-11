package com.carshering

import com.carshering.data.RetrofitApi
import com.carshering.data.cars.CarsDataLocal
import com.carshering.data.route.RoutesDataLocal
import com.carshering.domain.entity.CarsData
import com.carshering.domain.entity.RoutesData
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder

class StoreGraph(
    private val retrofit: RetrofitApi,
    private val carsDataLocal: CarsDataLocal,
    private val routesDataLocal: RoutesDataLocal
) {

    fun provideCarsStore(): Store<String, CarsData> {

        return StoreBuilder
            .from(
                Fetcher.of { url: String ->
                    retrofit.getCars(url)
                },
                sourceOfTruth = SourceOfTruth.of(
                    nonFlowReader = { carsDataLocal.loadCarsData() },
                    writer = { _, carsData: CarsData -> carsDataLocal.saveCarsData(carsData) }
                )
            )
            .build()
    }

    fun provideRoutesStore(): Store<String, RoutesData> {

        return StoreBuilder
            .from(
                Fetcher.of { url: String ->
                    retrofit.getRoute(url)
                },
                sourceOfTruth = SourceOfTruth.of(
                    nonFlowReader = { key -> routesDataLocal.loadRoutesData(key) },
                    writer = { key, routes: RoutesData ->
                        routesDataLocal.saveRoutesData(
                            key,
                            routes
                        )
                    }
                )
            )
            .build()
    }
}
