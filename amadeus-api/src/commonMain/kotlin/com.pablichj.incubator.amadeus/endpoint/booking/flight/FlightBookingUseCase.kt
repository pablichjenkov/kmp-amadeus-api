package com.pablichj.incubator.amadeus.endpoint.booking.flight

import AmadeusError
import com.pablichj.incubator.amadeus.common.CallResult
import com.pablichj.incubator.amadeus.common.Envs
import com.pablichj.incubator.amadeus.common.SingleUseCase
import com.pablichj.incubator.amadeus.httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FlightBookingUseCase(
    private val dispatcher: Dispatchers
) : SingleUseCase<FlightBookingRequest, CallResult<FlightBookingResponseBody>> {
    override suspend fun doWork(params: FlightBookingRequest): CallResult<FlightBookingResponseBody> {
        return withContext(dispatcher.Unconfined) {
            try {
                val response = httpClient.post(hotelBookingUrl) {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.Authorization, params.accessToken.authorization)
                    setBody(params.body)
                }
                if (response.status.isSuccess()) {
                    CallResult.Success(response.body())
                } else {
                    CallResult.Error(AmadeusError.fromErrorJsonString(response.bodyAsText()))
                }
            } catch (th: Throwable) {
                th.printStackTrace()
                CallResult.Error(AmadeusError.fromException(th))
            }
        }
    }

    companion object {
        private val hotelBookingUrl = "${Envs.TEST.hostUrl}/flights/bookings"
    }
}