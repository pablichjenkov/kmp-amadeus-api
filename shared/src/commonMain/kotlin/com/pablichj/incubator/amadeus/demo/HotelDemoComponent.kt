package com.pablichj.incubator.amadeus.demo

import FormParam
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
import com.pablichj.incubator.amadeus.endpoint.offers.hotel.model.HotelOfferSearch
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
    private var hotelOffers: List<HotelOfferSearch>? = null

    override fun onStart() {
        println("AmadeusDemoComponent::start()")
        output("AmadeusDemoComponent::start()")
    }

    override fun onStop() {
        println("AmadeusDemoComponent::stop()")
        output("AmadeusDemoComponent::stop()")
    }

    private fun getAccessToken() {
        coroutineScope.launch {
            val callResult = GetAccessTokenUseCase(Dispatchers).doWork(
                GetAccessTokenRequest(
                    listOf(
                        FormParam.ClientId(ApiCredentials.apiKey),
                        FormParam.ClientSecret(ApiCredentials.apiSecret),
                        FormParam.GrantType(GetAccessTokenUseCase.AccessTokenGrantType),
                    )
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
                        QueryParam.CountryCode("US"),
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
                        QueryParam.CityCode("PAR"),
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
                //hotelIds=HHMIA500&adults=1&checkInDate=2023-12-31&roomQuantity=1&bestRateOnly=false
                MultiHotelOffersRequest(
                    accessToken,
                    listOf(
                        QueryParam.HotelIds("HHMIA500"),
                        QueryParam.Adults("1"),
                        QueryParam.CheckInDate("2023-12-20"),
                        QueryParam.RoomQuantity("1"),
                        QueryParam.BestRateOnly("false")
                    )
                )
            )

            when (callResult) {
                is CallResult.Error -> {
                    output("Error in multi hotel offers: ${callResult.error}")
                }

                is CallResult.Success -> {
                    hotelOffers = callResult.responseBody.data
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

    private fun getFullOfferDetails(offerId: String) {
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
                    offerId
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
                            Guests Adults: ${it.guests?.adults}
                            Guests Kids: ${it.guests?.childAges?.size}
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
                    if (hotelOffers?.size == 0) {
                        output("hotelOffers.size == 0. Do a successful hotel offer search before calling this function.")
                        return@Button
                    }
                    val offers = hotelOffers?.get(0)?.offers
                    if (offers.isNullOrEmpty()) {
                        output("offers.size == 0. This Hotel Offer has zero offers")
                        return@Button
                    }
                    val offerId = offers[0].id
                    getFullOfferDetails(offerId)
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
