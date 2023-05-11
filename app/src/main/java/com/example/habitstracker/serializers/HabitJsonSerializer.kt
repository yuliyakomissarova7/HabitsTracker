package com.example.habitstracker.serializers

import com.example.habitstracker.entities.Habit
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type


class HabitJsonSerializer: JsonSerializer<Habit> {
    override fun serialize(src: Habit, typeOfSrc: Type, context: JsonSerializationContext
    ): JsonElement =
        JsonObject().apply {
            addProperty("color", src.color.colorId)
            addProperty("count", src.repetitionTimes)
            addProperty("date", src.creationDate.time)
            if (src.description == null || src.description.isEmpty()) {
                addProperty("description", " ")
            } else {
                addProperty("description", src.description)
            }
            addProperty("frequency", src.repetitionPeriod.ordinal)
            addProperty("priority", src.priority.value)
            addProperty("title", src.title)
            addProperty("type", src.type.ordinal)
            if (src.id.isNotEmpty()) {
                addProperty("uid", src.id)
            }
        }
}