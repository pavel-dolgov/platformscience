package com.paveldolgov.platformscience.model

data class OptionalDriverAssignment(
    val driver: Driver,
    val assignment: ShipmentWithSuitabilityScore?,
)
