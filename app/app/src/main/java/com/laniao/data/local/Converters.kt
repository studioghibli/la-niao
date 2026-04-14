package com.laniao.data.local

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

/**
 * Room type converters for custom types.
 */
class Converters {
    @TypeConverter
    fun fromInstant(instant: Instant?): Long? = instant?.toEpochMilli()

    @TypeConverter
    fun toInstant(epochMilli: Long?): Instant? = epochMilli?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? = dateString?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? = time?.toString()

    @TypeConverter
    fun toLocalTime(timeString: String?): LocalTime? = timeString?.let { LocalTime.parse(it) }
}
