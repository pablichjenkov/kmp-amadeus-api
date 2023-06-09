package com.pablichj.incubator.amadeus.endpoint.offers.hotel.model

import com.pablichj.incubator.amadeus.common.model.MetaWithLang
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class GetOfferResponseBody(
    val data: HotelOfferSearch,
    val meta: MetaWithLang
) {
    fun toJson(): String = Json.encodeToString(this)

    companion object {
        fun fromJson(json: String): GetOfferResponseBody = Json.decodeFromString(json)
    }
}
