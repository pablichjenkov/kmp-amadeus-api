package com.pablichj.incubator.amadeus.common.model

import kotlinx.serialization.Serializable

@Serializable
data class OfferPoliciesInHotels (
    val paymentType: String,
    val cancellations: List<OfferCancellation>
)