package com.paveldolgov.platformscience.model

sealed class DriversAndShipmentsError {
    object Retryable : DriversAndShipmentsError()
    object NonRetryable : DriversAndShipmentsError()
}