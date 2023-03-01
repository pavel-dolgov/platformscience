package com.paveldolgov.platformscience.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driver")
data class Driver(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: DriverId,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
)
