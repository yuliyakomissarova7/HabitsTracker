package com.example.habitstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habitstracker.db.HabitRepository

class HabitEditingViewModelFactory(private val repository: HabitRepository, private val habitId: String?) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HabitEditingViewModel(repository, habitId) as T
    }
}