package com.example.habitstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.entities.Habit
import com.example.habitstracker.db.HabitRepository
import kotlinx.coroutines.*

class HabitEditingViewModel(private val repository: HabitRepository, private val  habitId: Long?) : ViewModel() {
    private var mutableHabit: MutableLiveData<Habit?> = MutableLiveData()

    val habit: LiveData<Habit?> = repository.getHabit(habitId)

    init {
        load()
    }

    private fun load() {
        mutableHabit = MutableLiveData(repository.getHabit(habitId).value)
    }

    fun createOrUpdate(newHabit: Habit) = viewModelScope.launch(Dispatchers.IO) { repository.createOrUpdate(newHabit) }

    fun delete(newHabit: Habit) = viewModelScope.launch(Dispatchers.IO) { repository.delete(newHabit) }
}