package com.carshering.data

import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class ServerRequestDAO {

    private val url = "https://raw.githubusercontent.com/NelsonBeard/CarsheringAPI/master/cars.json"
    private val connection = URL(url).openConnection() as HttpsURLConnection

    fun getServerResponseData(): JSONObject {
        connect(connection)
        return JSONObject(connection.inputStream.bufferedReader().readText())
    }

    private fun connect(connection: HttpsURLConnection) {
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "text/plain; utf-8")
        connection.connectTimeout = 5000
    }
}
