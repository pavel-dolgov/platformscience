package com.paveldolgov.platformscience.client.model.error

sealed class ShipmentRequestError(val message: String) {
    data class IOException(val description: String) : ShipmentRequestError(description)
    data class JsonException(val description: String) : ShipmentRequestError(description)
}