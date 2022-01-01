package com.saklayen.currencyconverter.model
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class CurrencyRate(
    @Json(name = "base")
    val base: String?,
    @Json(name = "date")
    val date: String?,
    @Json(name = "rates")
    val rates: Rates?
) {
    @JsonClass(generateAdapter = true)
    data class Rates(
        @Json(name = "GBP")
        val gBP: Double?,
        @Json(name = "JPY")
        val jPY: Double?,
        @Json(name = "USD")
        val uSD: Double?
    )
}