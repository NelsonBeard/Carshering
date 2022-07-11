package com.carshering

import com.carshering.data.RetrofitClient
import com.carshering.data.cars.CarDAOImpl
import com.carshering.data.cars.CarsDataLocal
import com.carshering.data.route.GoogleMapToMapboxLatLngAdapter
import com.carshering.data.route.RouteDaoImpl
import com.carshering.data.route.RoutesDataLocal
import com.carshering.ui.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val mainActivity = module {
    factory {
        Presenter(
            carDAO = get(),
            routeDAO = get(),
            scope = get(),
            view = null
        )
    }
}

val presenter = module {
    single {
        CarDAOImpl(
            store = get(),
            scope = get()
        )
    }

    single {
        RouteDaoImpl(
            store = get(),
            scope = get(),
            latLngAdapter = get()
        )
    }

}

val DAOImpls = module {
    single {
        StoreGraph(
            retrofit = get(),
            carsDataLocal = get(),
            routesDataLocal = get()
        )
    }
    factory { CoroutineScope(Dispatchers.Main) }
    single { GoogleMapToMapboxLatLngAdapter() }
}

val store = module {
    single { RetrofitClient().createRetrofit() }
    single { CarsDataLocal }
    single { RoutesDataLocal }
}
