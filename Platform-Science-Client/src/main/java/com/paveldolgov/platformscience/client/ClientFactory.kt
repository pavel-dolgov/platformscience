package com.paveldolgov.platformscience.client

import com.paveldolgov.platformscience.client.adapter.moshi.DriverAdapter
import com.paveldolgov.platformscience.client.adapter.moshi.ShipmentAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

internal class ClientFactory {

    internal fun createMoshi(): Moshi {
        return Moshi.Builder()
            .add(DriverAdapter())
            .add(ShipmentAdapter())
            .addLast(KotlinJsonAdapterFactory()).build()
    }
}