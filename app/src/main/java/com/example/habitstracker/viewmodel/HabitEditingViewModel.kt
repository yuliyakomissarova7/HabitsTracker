package com.example.habitstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habitstracker.entities.Habit
import com.example.habitstracker.model.HabitRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HabitEditingViewModel(private val repository: HabitRepository, private val  habitId: Long?) : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job + CoroutineExceptionHandler { _, e -> throw e }

    private var mutableHabit: MutableLiveData<Habit?> = MutableLiveData()

    val habit: LiveData<Habit?> = repository.getHabit(habitId)

    init {
        load()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancelChildren()
    }

    private fun load() {
        mutableHabit = MutableLiveData(repository.getHabit(habitId).value)
    }

    fun createOrUpdate(newHabit: Habit) = launch { repository.createOrUpdate(newHabit) }

    fun delete(newHabit: Habit) = launch { repository.delete(newHabit) }
}