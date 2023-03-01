package com.paveldolgov.platformscience.persistence.adapter

import com.paveldolgov.platformscience.persistence.model.*
import com.paveldolgov.platformscience.model.Address as AppAddress
import com.paveldolgov.platformscience.model.Driver as AppDriver
import com.paveldolgov.platformscience.model.DriverAssignment as AppDriverAssignment
import com.paveldolgov.platformscience.model.DriverId as AppDriverId
import com.paveldolgov.platformscience.model.OptionalDriverAssignment as AppOptionalDriverAssignment
import com.paveldolgov.platformscience.model.Shipment as AppShipment
import com.paveldolgov.platformscience.model.ShipmentId as AppShipmentId
import com.paveldolgov.platformscience.model.ShipmentWithSuitabilityScore as AppShipmentWithSuitabilityScore

fun AppDriverId.toDbModel(): DriverId {
    return DriverId(value = value)
}

fun DriverId.toAppModel(): AppDriverId {
    return AppDriverId(value = value)
}

fun AppShipmentId.toDbModel(): ShipmentId {
    return ShipmentId(value = value)
}

fun ShipmentId.toAppModel(): AppShipmentId {
    return AppShipmentId(value = value)
}

fun AppDriver.toDbModel(): Driver {
    return Driver(id = id.toDbModel(), firstName = firstName, lastName = lastName)
}

fun Driver.toAppModel(): AppDriver {
    return AppDriver(id = id.toAppModel(), firstName = firstName, lastName = lastName)
}

fun AppAddress.toDbModel(): Address {
    return Address(streetNumber = streetNumber, streetName = streetName, unit = unit)
}

fun Address.toAppModel(): AppAddress {
    return AppAddress(streetNumber = streetNumber, streetName = streetName, unit = unit)
}

fun AppShipment.toDbModel(): Shipment {
    return Shipment(id = id.toDbModel(), address = address.toDbModel())
}

fun Shipment.toAppModel(): AppShipment {
    return AppShipment(id = id.toAppModel(), address = address.toAppModel())
}

fun AppDriverAssignment.toDbModel(): DriverAssignment {
    return DriverAssignment(
        driverId = driver.id.toDbModel(),
        shipmentId = shipment.id.toDbModel(),
        suitabilityScore
    )
}

fun DriverAssignmentWithShipment.toAppModel(): AppShipmentWithSuitabilityScore {
    return AppShipmentWithSuitabilityScore(shipment.toAppModel(), assignment.suitabilityScore)
}

fun DriverAssignmentDetails.toAppModel(): AppOptionalDriverAssignment {
    return AppOptionalDriverAssignment(
        driver = driver.toAppModel(),
        assignment = assignmentWithShipment?.toAppModel()
    )
}
