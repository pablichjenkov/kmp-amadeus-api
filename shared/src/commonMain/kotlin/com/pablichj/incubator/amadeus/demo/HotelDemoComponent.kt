package com.pablichj.incubator.amadeus.demo

import QueryParam
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pablichj.incubator.amadeus.Database
import com.pablichj.incubator.amadeus.common.CallResult
import com.pablichj.incubator.amadeus.common.DefaultTimeProvider
import com.pablichj.incubator.amadeus.common.ITimeProvider
import com.pablichj.incubator.amadeus.endpoint.accesstoken.*
import com.pablichj.incubator.amadeus.endpoint.booking.hotel.HotelBookingRequest
import com.pablichj.incubator.amadeus.endpoint.booking.hotel.HotelBookingUseCase
import com.pablichj.incubator.amadeus.endpoint.city.CitySearchRequest
import com.pablichj.incubator.amadeus.endpoint.city.CitySearchUseCase
import com.pablichj.incubator.amadeus.endpoint.hotels.HotelsByCityRequest
import com.pablichj.incubator.amadeus.endpoint.hotels.HotelsByCityUseCase
import com.pablichj.incubator.amadeus.endpoint.offers.*
import com.pablichj.incubator.amadeus.endpoint.offers.hotel.*
import com.pablichj.templato.component.core.Component
import com.pablichj.templato.component.platform.LocalSafeAreaInsets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HotelDemoComponent(
    database: Database
) : Component() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val timeProvider: ITimeProvider = DefaultTimeProvider()
    private val accessTokenDao = AccessTokenDaoDelight(
        database,
        timeProvider
    )

    private val console = mutableStateOf("")

    override fun start() {
        super.start()
        println("AmadeusDemoComponent::start()")
        output("AmadeusDemoComponent::start()")
    }

    override fun stop() {
        super.start()
        println("AmadeusDemoComponent::stop()")
        output("AmadeusDemoComponent::stop()")
    }

    private fun getAccessToken() {
        coroutineScope.launch {
            val callResult = GetAccessTokenUseCase(Dispatchers).doWork(
                GetAccessTokenRequest(
                    ApiCredentials.apiKey,
                    ApiCredentials.apiSecret,
                    GetAccessTokenUseCase.AccessTokenGrantType
                )
            )
            when (callResult) {
                is CallResult.Error -> {
                    output("Error fetching access token: ${callResult.error}")
                }
                is CallResult.Success -> {
                    val accessToken = callResult.responseBody
                    accessTokenDao.insert(accessToken)
                    output("SQDelight Insert Token Success: $accessToken")
                }
            }
        }
    }

    private fun getCitiesByKeyword() {
        coroutineScope.launch {
            val accessToken = ResolveAccessTokenUseCaseSource(
                Dispatchers,
                accessTokenDao
            ).doWork()

            if (accessToken == null) {
                output("No saved token")
                return@launch
            } else {
                output("Using saved token: ${accessToken.accessToken}")
            }

            val callResult = CitySearchUseCase(
                Dispatchers
            ).doWork(
                //?countryCode=FR&keyword=PARIS&max=10
                CitySearchRequest(
                    accessToken,
                    listOf(
                        QueryParam.CountryCode("US"),// todo remove hardcoded values
                        QueryParam.Max("5"),
                        QueryParam.Keyword("Miami")
                    )
                )
            )

            when (callResult) {
                is CallResult.Error -> {
                    output("Error in city search: ${callResult.error}")
                }
                is CallResult.Success -> {
                    callResult.responseBody.data.forEach {
                        output(
                            """
                            City Name = ${it.name}
                            City Address = ${it.address}
                            City Type = ${it.type}
                            City Subtype = ${it.subType}
                        """.trimIndent()
                        )
                    }
                }
            }

        }
    }

    private fun getHotelsByCity() {
        coroutineScope.launch {
            val accessToken = ResolveAccessTokenUseCaseSource(
                Dispatchers,
                accessTokenDao
            ).doWork()

            if (accessToken == null) {
                output("No saved token")
                return@launch
            } else {
                output("Using saved token: ${accessToken.accessToken}")
            }

            val callResult = HotelsByCityUseCase(
                Dispatchers
            ).doWork(
                //?cityCode=PAR&radius=1&radiusUnit=KM&hotelSource=ALL
                HotelsByCityRequest(
                    accessToken,
                    listOf(
                        QueryParam.CityCode("PAR"),// todo remove hardcoded values
                        QueryParam.Radius("1"),
                        QueryParam.RadiusUnit("KM"),
                        QueryParam.HotelSource("ALL")
                    )
                )
            )

            when (callResult) {
                is CallResult.Error -> {
                    output("Error in hotels by city: ${callResult.error}")
                }
                is CallResult.Success -> {
                    callResult.responseBody.data.forEach {
                        output(
                            """
                                Hotel ID: ${it.hotelId}
                                Geocode: ${it.geoCode}
                                Dupe ID: ${it.dupeId}
                                -----
                            """.trimMargin()
                        )
                    }
                }
            }

        }
    }

    private fun getMultiHotelsOffers() {
        coroutineScope.launch {
            val accessToken = ResolveAccessTokenUseCaseSource(
                Dispatchers,
                accessTokenDao
            ).doWork()

            if (accessToken == null) {
                output("No saved token")
                return@launch
            } else {
                output("Using saved token: ${accessToken.accessToken}")
            }

            val callResult = MultiHotelOffersUseCase(
                Dispatchers
            ).doWork(
                //?hotelIds=MCLONGHM&adults=1&checkInDate=2023-11-22&roomQuantity=1&paymentPolicy=NONE&bestRateOnly=true
                MultiHotelOffersRequest(
                    accessToken,
                    listOf(
                        QueryParam.HotelIds("MCLONGHM"),// ACPAR243 todo remove hardcoded values
                        QueryParam.Adults("1"),
                        QueryParam.CheckInDate("2023-11-22"),
                        QueryParam.RoomQuantity("1"),
                        QueryParam.PaymentPolicy("NONE"),
                        QueryParam.BestRateOnly("false")
                    )
                )
            )

            when (callResult) {
                is CallResult.Error -> {
                    output("Error in multi hotel offers: ${callResult.error}")
                }
                is CallResult.Success -> {
                    callResult.responseBody.data.forEach {
                        output(
                            """
                                Hotel ID: ${it.hotel.hotelId}
                                Available: ${it.available}
                                Type: ${it.type}
                            """.trimMargin()
                        )
                        output("Offers: ")
                        it.offers.forEach {
                            output(
                                """Offer ID: ${it.id}
                                   Base Price: ${it.price.base}
                                   Checkin: ${it.checkInDate}
                            """.trimMargin()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getOffer() {
        coroutineScope.launch {
            val accessToken = ResolveAccessTokenUseCaseSource(
                Dispatchers,
                accessTokenDao
            ).doWork()

            if (accessToken == null) {
                output("No saved token")
                return@launch
            } else {
                output("Using saved token: ${accessToken.accessToken}")
            }

            val callResult = GetOfferUseCase(
                Dispatchers
            ).doWork(
                GetOfferRequest(
                    accessToken,
                    "TSXOJ6LFQ2"
                )
            )

            when (callResult) {
                is CallResult.Error -> {
                    output("Error in get offer by id: ${callResult.error}")
                }
                is CallResult.Success -> {
                    output("Offer in Hotel: ${callResult.responseBody.data.hotel.name}")
                    callResult.responseBody.data.offers.forEach {
                        output(
                            """
                            Offer Id: ${it.id}
                            CheckInDate: ${it.checkInDate}
                            CheckOutDate: ${it.checkOutDate}
                            Guests: ${it.guests}
                            Base Price: ${it.price.base}
                        """.trimIndent()
                        )
                    }
                }
            }
        }
    }

    private fun hotelBook() {
        coroutineScope.launch {
            val accessToken = ResolveAccessTokenUseCaseSource(
                Dispatchers,
                accessTokenDao
            ).doWork()

            if (accessToken == null) {
                output("No saved token")
                return@launch
            } else {
                output("Using saved token: ${accessToken.accessToken}")
            }

            val callResult = HotelBookingUseCase(
                Dispatchers
            ).doWork(
                HotelBookingRequest(
                    accessToken,
                    TestData.hotelBookingRequestBody
                )
            )

            when (callResult) {
                is CallResult.Error -> {
                    output("Error in hotel booking: ${callResult.error}")
                }
                is CallResult.Success -> {
                    callResult.responseBody.data.forEach {
                        output(
                            """
                            Booking Confirmation Id: ${it.id}
                            Provider Confirmation Id: ${it.providerConfirmationId}
                            Booking Confirmation Type: ${it.type}
                            Booking Associated Records:
                        """.trimIndent()
                        )
                        it.associatedRecords.forEach {
                            output(
                                """
                                Record Reference: ${it.reference}
                                Record Origin System Code: ${it.originSystemCode}
                            """.trimIndent()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun output(text: String) {
        console.value += "\n$text"
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Content(modifier: Modifier) {
        val safeAreaInsets = LocalSafeAreaInsets.current
        Column(
            modifier.fillMaxSize().padding(top = safeAreaInsets.top.dp)
        ) {
            Spacer(Modifier.fillMaxWidth().height(24.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Welcome to Amadeus Hotel Booking API",
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top,
            ) {
                Button(onClick = {
                    getAccessToken()
                }) {
                    Text("Get Access Token")
                }
                Button(onClick = {
                    getCitiesByKeyword()
                }) {
                    Text("City Search")
                }
                Button(onClick = {
                    getHotelsByCity()
                }) {
                    Text("Get Hotels By City")
                }
                Button(onClick = {
                    getMultiHotelsOffers()
                }) {
                    Text("Get Multi Hotel Offers")
                }
                Button(onClick = {
                    getOffer()
                }) {
                    Text("Get Offer")
                }
                Button(onClick = {
                    hotelBook()
                }) {
                    Text("Book a Hotel")
                }
                Button(onClick = {
                    console.value = ""
                }) {
                    Text("Clear")
                }
            }
            Text(
                modifier = Modifier.fillMaxSize().padding(8.dp)
                    .verticalScroll(rememberScrollState()).background(Color.White),
                text = console.value
            )
        }
    }

}
