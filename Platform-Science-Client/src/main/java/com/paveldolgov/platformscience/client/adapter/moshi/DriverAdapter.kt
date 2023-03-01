package com.paveldolgov.platformscience.client.adapter.moshi

import com.paveldolgov.platformscience.client.model.Driver
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

internal class DriverAdapter {
    @FromJson
    fun fromJson(text: String): Driver {
        return Driver(text)
    }

    @ToJson
    fun toJson(driver: Driver): String {
        return driver.fullName
    }
}