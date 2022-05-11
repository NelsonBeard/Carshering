package com.carshering.data

import android.os.Handler
import com.carshering.domain.entity.Car
import com.carshering.domain.usecase.CarDAO
import com.carshering.exception.DownloadException
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class CarDAOImpl(
    private val handler: Handler
) : CarDAO {
    private lateinit var cars: List<Car>
    override fun getAllCars(onSuccess: (List<Car>) -> Unit) {
        Thread {
            val serverResponseData = getServerResponseData()
            cars = JsonToCarListAdapter(serverResponseData).fromJson()
            handOverToUIThread(onSuccess)
        }.start()
    }

    private fun handOverToUIThread(onSuccess: (List<Car>) -> Unit) {
        try {
            handler.post { onSuccess(cars) }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    private fun getServerResponseData(): String {

        val url = "https://raw.githubusercontent.com/NelsonBeard/CarsheringAPI/master/cars.json"
        val connection = URL(url).openConnection() as HttpsURLConnection

        try {
            connection.apply {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; utf-8")
                connectTimeout = 5000

            }
            return connection.inputStream.bufferedReader().readText()
        } catch (error: Exception) {
            throw DownloadException("Download file error")
        } finally {
            connection.disconnect()
        }
    }
}

