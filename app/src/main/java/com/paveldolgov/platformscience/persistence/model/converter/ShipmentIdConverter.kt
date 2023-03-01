package com.paveldolgov.platformscience.persistence.model.converter

import androidx.room.TypeConverter
import com.paveldolgov.platformscience.persistence.model.ShipmentId

class ShipmentIdConverter {
    @TypeConverter
    fun fromStringId(stringId: String?): ShipmentId? {
        return stringId?.let { ShipmentId(it) }
    }

    @TypeConverter
    fun toStringId(id: ShipmentId?): String? {
        return id?.value
    }
}
