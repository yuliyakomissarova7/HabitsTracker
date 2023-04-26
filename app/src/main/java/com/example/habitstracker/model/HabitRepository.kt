package com.example.habitstracker.model

import androidx.lifecycle.LiveData
import com.example.habitstracker.entities.Habit
import com.example.habitstracker.entities.SortCriterion
import com.example.habitstracker.viewmodel.Filter

class HabitRepository(private val habitDao: HabitDao) {

    var habits: LiveData<List<Habit>> = habitDao.getAll()

    fun createOrUpdate(habit: Habit) =
        habitDao.createOrUpdate(habit)

    fun delete(habit: Habit) =
        habitDao.delete(habit)

    fun getHabit(id: Long?): LiveData<Habit?> =
        habitDao.getHabit(id)

    fun applyFilters(filter: Filter): LiveData<List<Habit>> =
        when (filter.sortCriterion) {
            SortCriterion.CREATION_DATE_DESCENDING, SortCriterion.CREATION_DATE_ASCENDING ->
                habitDao.getByCreationDate(filter.priorities, filter.colors,
                    filter.sortCriterion.isAscending, filter.searchQuery)
        }
}
