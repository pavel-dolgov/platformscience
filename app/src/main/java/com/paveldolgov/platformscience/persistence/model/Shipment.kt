package com.paveldolgov.platformscience.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shipment")
data class Shipment(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: ShipmentId,
    @Embedded(prefix = "address_")
    val address: Address
)

data class Address(
    @ColumnInfo(name = "street_number")
    val streetNumber: Int,
    @ColumnInfo(name = "street_name")
    val streetName: String,
    @ColumnInfo(name = "unit")
    val unit: String?
)
