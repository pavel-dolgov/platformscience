package com.paveldolgov.platformscience.model

data class Driver(
    val id: DriverId,
    val firstName: String,
    val lastName: String,
) {
    val fullName: String
        get() = "$firstName $lastName"
}
