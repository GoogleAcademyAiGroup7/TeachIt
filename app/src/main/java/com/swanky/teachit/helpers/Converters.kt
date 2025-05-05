package com.swanky.teachit.helpers

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromSuggestionsList(value: List<String>): String {
        return value.joinToString(separator = "||")
    }

    @TypeConverter
    fun toSuggestionsList(value: String): List<String> {
        return value.split("||")
    }
}