package com.paveldolgov.platformscience.client.adapter.moshi

import com.paveldolgov.platformscience.client.model.Shipment
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

internal class ShipmentAdapter {
    @FromJson
    fun fromJson(text: String): Shipment {
        return Shipment(text)
    }

    @ToJson
    fun toJson(shipment: Shipment): String {
        return shipment.address
    }
}