package com.example.habitstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.usecases.HabitEditingUseCase

class HabitEditingViewModelFactory(private val habitEditingUseCase: HabitEditingUseCase)
    : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HabitEditingViewModel(habitEditingUseCase) as T
    }
}