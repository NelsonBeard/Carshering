package com.carshering.data.route

import com.carshering.domain.entity.RoutesData

object RoutesDataLocal {
    private var routesData: MutableMap<String, RoutesData>? = mutableMapOf()

    fun saveRoutesData(key: String, data: RoutesData) {
        routesData?.put(key, data)
    }

    fun loadRoutesData(key: String): RoutesData? {
        return routesData?.get(key)
    }
}