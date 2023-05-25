package com.example.domain

import com.example.domain.entities.Filter
import com.example.domain.entities.Habit
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface HabitRepository {

    var habits: Flow<List<Habit>>

    suspend fun createOrUpdate(habit: Habit)

    suspend fun delete(habitId: String)

    fun getHabit(id: String?): Habit?

    suspend fun getHabitsFromServer()

    suspend fun checkHabit(date: Date, habitId: String)

    fun applyFilters(filter: Filter): Flow<List<Habit>>
}
