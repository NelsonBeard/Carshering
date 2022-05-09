package com.carshering.data

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.carshering.domain.entity.Car
import com.carshering.domain.usecase.CarDAO
import com.carshering.ui.MainActivityMap
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection


class CarDAOImpl : CarDAO {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    override fun getAllCars(onSuccess: (cars: List<Car>) -> Unit) {
        executor.execute {
            try {
                handler.post { onSuccess(JsonToCarAdapter().convertJson(getServerResponseData())) }
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }

    private fun getServerResponseData(): String {
        val connection = connect()
        return connection.inputStream.bufferedReader().readText()
    }

    private fun connect(): HttpsURLConnection {
        val url = "https://raw.githubusercontent.com/NelsonBeard/CarsheringAPI/master/cars.json"
        val connection = URL(url).openConnection() as HttpsURLConnection

        try {
            connection.apply {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; utf-8")
                connectTimeout = 5000
            }
            return connection
        } catch (error: Exception) {
            Toast.makeText(MainActivityMap(), "Не удалось загрузить автомобили", Toast.LENGTH_SHORT)
                .show()
            throw Exception("No internet connection")
        } finally {
            connection.disconnect()
        }
    }
}

