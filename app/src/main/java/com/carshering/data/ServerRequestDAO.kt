package com.carshering.data

import android.os.Handler
import android.os.Looper
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection


class ServerRequestDAO {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    private fun getServerResponseData(): JSONObject {
        val url = "https://raw.githubusercontent.com/NelsonBeard/CarsheringAPI/master/cars.json"
        val connection = URL(url).openConnection() as HttpsURLConnection

        connect(connection)
        return JSONObject(connection.inputStream.bufferedReader().readText())
    }

    private fun connect(connection: HttpsURLConnection) {
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "text/plain; utf-8")
        connection.connectTimeout = 5000
    }

    fun execute(onSuccess: (data: JSONObject) -> Unit) {
        executor.execute {
            try {
                val data = getServerResponseData()
                handler.post { onSuccess(data) }
            } catch (error: Exception) {
                error.printStackTrace()
            }

        }
    }
}

