package com.paveldolgov.platformscience.persistence.model.converter

import androidx.room.TypeConverter
import com.paveldolgov.platformscience.persistence.model.DriverId

class DriverIdConverter {
    @TypeConverter
    fun fromStringId(stringId: String?): DriverId? {
        return stringId?.let { DriverId(it) }
    }

    @TypeConverter
    fun toStringId(id: DriverId?): String? {
        return id?.value
    }
}
