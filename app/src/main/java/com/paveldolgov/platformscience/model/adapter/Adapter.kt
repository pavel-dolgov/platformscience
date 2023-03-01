package com.paveldolgov.platformscience.model.adapter

import com.paveldolgov.platformscience.model.Driver
import com.paveldolgov.platformscience.model.DriverId
import com.paveldolgov.platformscience.model.Shipment
import com.paveldolgov.platformscience.model.ShipmentId
import com.paveldolgov.platformscience.model.adapter.Parser.fullNameToFirstAndLast
import com.paveldolgov.platformscience.model.adapter.Parser.stringToAddress
import org.apache.commons.codec.digest.MurmurHash3
import com.paveldolgov.platformscience.client.model.Driver as ClientDriver
import com.paveldolgov.platformscience.client.model.Shipment as ClientShipment


fun String.toMurmurHash3Id(): String = MurmurHash3.hash32x86(this.toByteArray()).toString()


fun ClientDriver.toAppModel(): Driver {
    val firstAndLast = fullNameToFirstAndLast(fullName)
    val id = DriverId(fullName.toMurmurHash3Id())
    return Driver(id, firstAndLast.first, firstAndLast.last)
}

fun ClientShipment.toAppModel(): Shipment {
    val id = ShipmentId(address.toMurmurHash3Id())
    return Shipment(id, stringToAddress(address))
}
