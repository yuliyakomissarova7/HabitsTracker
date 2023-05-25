package com.example.domain.entities

import java.util.*
import androidx.room.*

@Entity
data class Habit(val title: String,
                 val type: HabitType,
                 val priority: Priority,
                 val repetitionTimes: Int,
                 val repetitionPeriod: Period,
                 val description: String?,
                 val color: HabitColor,
                 val creationDate: Date,
                 val doneDates: List<Date>,
                 val doneTimes: Int,
                 @PrimaryKey val id: String = "",): java.io.Serializable
