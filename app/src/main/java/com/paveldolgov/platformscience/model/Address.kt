package com.paveldolgov.platformscience.model

data class Address(
    val streetNumber: Int,
    val streetName: String,
    val unit: String? = null
) {
    val fullAddress: String by lazy {
        val unitFormatted = if (unit != null) {
            " $unit"
        } else {
            ""
        }
        "$streetNumber $streetName${unitFormatted}"
    }
}