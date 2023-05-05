package com.pablichj.incubator.amadeus.endpoint.offers.hotel

import AmadeusError
import com.pablichj.incubator.amadeus.common.CallResult
import com.pablichj.incubator.amadeus.common.Envs
import com.pablichj.incubator.amadeus.common.SingleUseCase
import com.pablichj.incubator.amadeus.endpoint.offers.hotel.model.GetOfferResponseBody
import com.pablichj.incubator.amadeus.httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetOfferUseCase(
    private val dispatcher: Dispatchers
) : SingleUseCase<GetOfferRequest, CallResult<GetOfferResponseBody>> {
    override suspend fun doWork(params: GetOfferRequest): CallResult<GetOfferResponseBody> {
        return withContext(dispatcher.Unconfined) {
            try {
                val response = httpClient.get(getOfferUrl) {
                    url {
                        appendEncodedPathSegments("/hotel-offers/${params.offerId}")
                    }
                    header(HttpHeaders.Authorization, params.accessToken.authorization)
                }
                if (response.status.isSuccess()) {
                    CallResult.Success<GetOfferResponseBody>(response.body())
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
        private val getOfferUrl = "${Envs.TEST.hostUrl}/v3/shopping"
    }
}