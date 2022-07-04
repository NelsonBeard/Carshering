package com.carshering.exception

class JsonParseException(
    message: String,
    throwable: Throwable
) : Exception(message, throwable)

class DownloadException(
    message: String,
    throwable: Throwable
) : Exception(message, throwable)