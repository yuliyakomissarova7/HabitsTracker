package com.example.domain.entities

import androidx.lifecycle.LiveData

data class Filter(
    val habits: LiveData<List<Habit>>,
    var priorities: MutableSet<Priority>,
    var colors: MutableSet<HabitColor>,
    var sortCriterion: SortCriterion,
    var searchQuery: String,
)