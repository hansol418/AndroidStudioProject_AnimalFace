package com.project.animalface_app

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializationContext
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.google.gson.JsonParseException

class LocalDateDeserializer : JsonDeserializer<LocalDate> {
    @SuppressLint("NewApi")
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDate {
        return try {
            LocalDate.parse(json.asString, formatter)
        } catch (e: Exception) {
            throw JsonParseException("Failed to parse date: ${json.asString}", e)
        }
    }
}

