package com.pablichj.incubator.amadeus.endpoint.booking.hotel

import AmadeusError
import com.pablichj.incubator.amadeus.common.CallResult
import com.pablichj.incubator.amadeus.common.Envs
import com.pablichj.incubator.amadeus.common.SingleUseCase
import com.pablichj.incubator.amadeus.endpoint.booking.hotel.model.HotelBookingResponseBody
import com.pablichj.incubator.amadeus.httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HotelBookingUseCase(
    private val dispatcher: Dispatchers
) : SingleUseCase<HotelBookingRequest, CallResult<HotelBookingResponseBody>> {

    override suspend fun doWork(params: HotelBookingRequest): CallResult<HotelBookingResponseBody> {
        val result = withContext(dispatcher.Unconfined) {
            runCatching {
                val response = httpClient.post(hotelBookingUrl) {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.Authorization, params.accessToken.authorization)
                    setBody(params.body)
                }
                if (response.status.isSuccess()) {
                    CallResult.Success<HotelBookingResponseBody>(response.body())
                } else {
                    CallResult.Error(AmadeusError.fromErrorJsonString(response.bodyAsText()))
                }
            }
        }
        return result.getOrElse {
            return CallResult.Error(AmadeusError.fromException(it))
        }
    }

    companion object {
        private val hotelBookingUrl = "${Envs.TEST.hostUrl}/booking/hotel-bookings"
    }
}