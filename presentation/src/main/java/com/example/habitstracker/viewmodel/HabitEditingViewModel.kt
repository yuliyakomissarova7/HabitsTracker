package com.example.habitstracker.viewmodel

import androidx.lifecycle.ViewModel
import com.example.domain.entities.Habit
import com.example.domain.usecases.HabitEditingUseCase

class HabitEditingViewModel(private val habitEditingUseCase: HabitEditingUseCase) : ViewModel() {

    fun getHabit(habitId: String?): Habit? =
        habitEditingUseCase.getHabit(habitId)

    fun createOrUpdate(newHabit: Habit) = habitEditingUseCase.createOrUpdateHabit(newHabit)

    fun delete(habitId: String) = habitEditingUseCase.deleteHabit(habitId)

    fun check(habitId: String) =
        habitEditingUseCase.checkHabit(habitId)

    fun isHabitDone(habit: Habit): Pair<Boolean, Int> =
        habitEditingUseCase.isHabitDone(habit)
}