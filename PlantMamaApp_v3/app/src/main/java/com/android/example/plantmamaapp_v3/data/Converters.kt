package com.android.example.plantmamaapp_v3.data

import androidx.room.TypeConverter
import java.util.Date

//used to migrate old age int into a date for the updated database
class Converters{
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let{Date(it)}
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long?{
        return date?.time
    }
}