package com.paveldolgov.platformscience.model

data class DriverAssignment(
    val driver: Driver,
    val shipment: Shipment,
    val suitabilityScore: Double
)
