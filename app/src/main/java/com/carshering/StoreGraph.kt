package com.carshering

import com.carshering.data.RetrofitApi
import com.carshering.data.RetrofitClient
import com.carshering.domain.entity.CarsDataFromServer
import com.carshering.domain.entity.RoutesDataFromServer
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder

object StoreGraph {
    private val retrofit: RetrofitApi = RetrofitClient().createRetrofit()

    fun provideCarsStore(): Store<String, CarsDataFromServer> {
        return StoreBuilder
            .from(
                Fetcher.of { url: String ->
                    retrofit.getCars(url)
                },
//                sourceOfTruth = SourceOfTruth.of()
            )
            .build()
    }

    fun provideRoutesStore(): Store<String, RoutesDataFromServer> {
        return StoreBuilder
            .from(
                Fetcher.of { url: String ->
                    retrofit.getRoute(url)
                },
//                sourceOfTruth = SourceOfTruth.of()
            )
            .build()
    }
}
