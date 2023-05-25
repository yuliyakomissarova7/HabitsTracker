package com.example.data.serializers

import com.example.domain.entities.DoneHabitBody
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class HabitDoneBodySerializer: JsonSerializer<DoneHabitBody> {
    override fun serialize(
        src: DoneHabitBody,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement =
        JsonObject().apply {
            addProperty("date", src.date.time)
            addProperty("habit_uid", src.uid)
        }

}