package com.example.coursefinderapp.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return if (value == null) null else Gson().fromJson(value, object : TypeToken<List<Int>>() {}.type)
    }
}