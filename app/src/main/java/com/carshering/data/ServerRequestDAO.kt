package com.carshering.data

import android.os.Handler
import android.os.Looper
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection


class ServerRequestDAO {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    private fun getServerResponseData(): String {
        val connection = connect()
        return connection.inputStream.bufferedReader().readText()
    }

    private fun connect(): HttpsURLConnection {
        val url = "https://raw.githubusercontent.com/NelsonBeard/CarsheringAPI/master/cars.json"
        val connection = URL(url).openConnection() as HttpsURLConnection

        connection.apply {
            this.requestMethod = "GET"
            this.setRequestProperty("Content-Type", "application/json; utf-8")
            this.connectTimeout = 5000
            this.disconnect()
        }
        return connection
    }

    private fun stringToJsonArray(): JSONArray {
        return JSONObject(getServerResponseData()).getJSONArray("cars")
    }

    fun getJsonArray(onSuccess: (data: JSONArray) -> Unit) {
        executor.execute {
            try {
                val data = stringToJsonArray()
                handler.post { onSuccess(data) }
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }
}

