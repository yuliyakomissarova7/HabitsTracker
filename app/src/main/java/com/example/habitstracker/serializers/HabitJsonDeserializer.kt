package com.example.habitstracker.serializers

import com.example.habitstracker.entities.*
import com.google.gson.*
import java.lang.reflect.Type
import java.util.*


class HabitJsonDeserializer: JsonDeserializer<Habit> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?
    ): Habit {
        val jsonObject = json.asJsonObject
        val description =
            if (jsonObject.get("description").asString == " ") {
                null
            } else {
                jsonObject.get("description").asString
            }
        return Habit(
            jsonObject.get("title").asString,
            HabitType.values().getOrElse(jsonObject.get("type").asInt) { HabitType.GOOD},
            Priority.values().find { it.value == jsonObject.get("priority").asInt } ?: Priority.HIGH,
            jsonObject.get("count").asInt,
            Period.values().getOrElse(jsonObject.get("frequency").asInt) { Period.DAY},
            description,
            HabitColor.values().find { it.colorId == jsonObject.get("color").asInt } ?: HabitColor.GRADIENT_COLOR1,
            Date(jsonObject.get("date").asLong),
            jsonObject.get("uid").asString,
        )
    }
}