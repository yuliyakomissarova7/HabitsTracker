package com.example.habitstracker.entities

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
                 @PrimaryKey(autoGenerate = true) val id: Long = 0,): java.io.Serializable
