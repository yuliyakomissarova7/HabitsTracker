package com.example.habitstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habitstracker.entities.Habit
import com.example.habitstracker.model.HabitRepository

class HabitEditingViewModel(private val repository: HabitRepository, private val  habitId: Long?) : ViewModel() {
    private var mutableHabit: MutableLiveData<Habit?> = MutableLiveData()

    val habit: LiveData<Habit?> = repository.getHabit(habitId)

    init {
        load()
    }

    private fun load() {
        mutableHabit = MutableLiveData(repository.getHabit(habitId).value)
    }

    fun createOrUpdate(newHabit: Habit) {
        repository.createOrUpdate(newHabit)
    }

    fun delete(newHabit: Habit) {
        repository.delete(newHabit)
    }
}