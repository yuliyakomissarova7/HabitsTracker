package com.example.domain.usecases

import com.example.domain.HabitRepository
import com.example.domain.entities.Filter
import com.example.domain.entities.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SearchAndFilterHabitUseCase(private val repository: HabitRepository) {
    val habits = repository.habits

    suspend fun getHabitsFromServer() =
        withContext(Dispatchers.IO) {
            repository.getHabitsFromServer()
        }

    fun applyFilters(filter: Filter): Flow<List<Habit>> =
        repository.applyFilters(filter)
}