package com.pablichj.incubator.amadeus.common.model

@kotlinx.serialization.Serializable
data class Address (
    val cityName: String? = null,
    val cityCode: String? = null,
    val countryName: String? = null,
    val countryCode: String? = null,
    val stateCode: String? = null,
    val regionCode: String?= null
)