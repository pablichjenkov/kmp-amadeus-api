package com.pablichj.incubator.amadeus

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private const val HttpOperationTimeoutMillis = 15_000L

internal val httpClient = HttpClient {
    install(HttpTimeout) {
        requestTimeoutMillis = HttpOperationTimeoutMillis
        connectTimeoutMillis =  HttpOperationTimeoutMillis
        socketTimeoutMillis = HttpOperationTimeoutMillis
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
        /*filter { request ->
            request.url.host.contains("ktor.io")
        }*/
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}
