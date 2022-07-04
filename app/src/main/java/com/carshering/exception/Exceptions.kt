package com.carshering.exception

// указать original exception
class JsonParseException(message :String) : Exception(message)
class DownloadException(message :String) : Exception(message)