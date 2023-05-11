package com.example.habitstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.habitstracker.entities.Habit
import com.example.habitstracker.db.HabitRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HabitEditingViewModel(private val repository: HabitRepository, habitId: String?) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler {
                _, throwable -> throw throwable
        }

    val habit: LiveData<Habit?> = repository.getHabit(habitId)

    fun createOrUpdate(newHabit: Habit) = launch { repository.createOrUpdate(newHabit) }

    fun delete(newHabit: Habit) = launch { repository.delete(newHabit) }
}