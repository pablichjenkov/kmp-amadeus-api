package com.pablichj.incubator.amadeus.common.model

import kotlinx.serialization.Serializable

@Serializable
data class Distance(
    val value: Double,
    val unit: String,
    val displayValue: String? = null,
    val isUnlimited: String? = null
)
