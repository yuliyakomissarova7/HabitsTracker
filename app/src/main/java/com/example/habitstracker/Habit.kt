package com.example.habitstracker

import com.example.habitstracker.entities.HabitType
import com.example.habitstracker.entities.Period
import com.example.habitstracker.entities.Priority


data class Habit(val title: String,
                 val type: HabitType,
                 val priority: Priority,
                 val repetitionTimes: Int,
                 val repetitionPeriod: Period,
                 val description: String?,
                 val colorId: Int): java.io.Serializable

