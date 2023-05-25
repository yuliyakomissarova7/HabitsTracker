package com.example.habitstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.usecases.SearchAndFilterHabitUseCase

class HabitsListViewModelFactory(private val searchAndFilterHabitUseCase:
                                 SearchAndFilterHabitUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HabitsListViewModel(searchAndFilterHabitUseCase) as T
    }
}