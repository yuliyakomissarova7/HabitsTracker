package com.example.data.db

import androidx.room.TypeConverter
import com.example.domain.entities.Priority
import java.util.*

class Converter {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return if (dateLong == null) null else Date(dateLong)
    }

    @TypeConverter
    fun fromPriority(value: Priority) = value.value.toString()

    @TypeConverter
    fun toPriority(value: String) = Priority.values().find { it.value.toString() == value } ?: Priority.HIGH

    @TypeConverter
    fun toDatesString(dates: String): List<Date> =
        if (dates.isEmpty()) listOf() else dates.split(";").map { Date(it.toLong()) }

    @TypeConverter
    fun fromListDate(dates: List<Date>): String =
        dates.joinToString(";") { it.time.toString() }
}