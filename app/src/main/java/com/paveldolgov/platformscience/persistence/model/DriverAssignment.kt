package com.paveldolgov.platformscience.persistence.model

import androidx.room.*

@Entity(
    tableName = "driver_assignment", primaryKeys = ["driver_id", "shipment_id"],
    foreignKeys = [ForeignKey(
        entity = Driver::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("driver_id"),
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = Shipment::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("shipment_id"),
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index("driver_id"), Index("shipment_id")]
)
data class DriverAssignment(
    @ColumnInfo(name = "driver_id")
    val driverId: DriverId,
    @ColumnInfo(name = "shipment_id")
    val shipmentId: ShipmentId,
    @ColumnInfo(name = "suitability_score")
    val suitabilityScore: Double
)

data class DriverAssignmentDetails(
    @Embedded
    val driver: Driver,
    @Relation(entity = DriverAssignment::class, parentColumn = "id", entityColumn = "driver_id")
    val assignmentWithShipment: DriverAssignmentWithShipment?,
)

data class DriverAssignmentWithShipment(
    @Embedded
    val assignment: DriverAssignment,
    @Relation(entity = Shipment::class, parentColumn = "shipment_id", entityColumn = "id")
    val shipment: Shipment,
)