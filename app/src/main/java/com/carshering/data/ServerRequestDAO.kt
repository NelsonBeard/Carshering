package com.carshering.data

import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ServerRequestDAO {
    private val url = "https://raw.githubusercontent.com/NelsonBeard/CarsheringAPI/master/cars.json"
    private val connection = URL(url).openConnection() as HttpsURLConnection

    fun getServerResponseData(): String {
        return connect(connection)
    }
}

private fun connect(connection: HttpsURLConnection): String {

    connection.requestMethod = "GET"
    connection.setRequestProperty("Content-Type", "text/plain; utf-8")
    connection.connectTimeout = 5000

    return connection.inputStream.bufferedReader().readText()
}