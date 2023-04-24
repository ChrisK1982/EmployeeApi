package com.example.employee_api.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.time.LocalDate

class LocalDateTypeAdapter : TypeAdapter<LocalDate>() {

    override fun write(out: JsonWriter?, value: LocalDate?) {
        if (value == null) {
            out?.nullValue()
        } else {
            out?.value(value.toString())
        }
    }

    override fun read(`in`: JsonReader?): LocalDate? {
        if (`in`?.peek() == JsonToken.NULL) {
            `in`.nextNull()
            return null
        }
        val dateString = `in`?.nextString()
        return LocalDate.parse(dateString)
    }
}
