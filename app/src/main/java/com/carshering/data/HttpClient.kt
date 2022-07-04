package com.carshering.data

import com.carshering.exception.DownloadException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class HttpClient {

    @Throws(DownloadException::class)
    fun get(url: String): String {
        val connection = URL(url).openConnection() as HttpsURLConnection

        try {
            connection.apply {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; utf-8")
                connectTimeout = 5000
            }
            return connection.inputStream.bufferedReader().readText()
        } catch (error: Exception) {
            throw DownloadException("Download file error", error)
        } finally {
            connection.disconnect()
        }
    }
}