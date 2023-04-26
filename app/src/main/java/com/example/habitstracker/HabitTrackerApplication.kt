package com.example.habitstracker

import android.app.Application
import com.example.habitstracker.model.HabitDatabase
import com.example.habitstracker.model.HabitRepository

class HabitTrackerApplication : Application() {
    private val database by lazy { HabitDatabase.getDatabase(this) }
    val repository by lazy { HabitRepository(database.habitDao()) }
}