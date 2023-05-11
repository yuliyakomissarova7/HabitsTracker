package com.example.habitstracker.db

import androidx.lifecycle.LiveData
import com.example.habitstracker.entities.DeleteHabitBody
import com.example.habitstracker.entities.Habit
import com.example.habitstracker.entities.SortCriterion
import com.example.habitstracker.viewmodel.Filter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitRepository(private val habitDao: HabitDao, private val service: HabitService) {

    var habits: LiveData<List<Habit>> = habitDao.getAll()

    suspend fun createOrUpdate(habit: Habit) =
        withContext(Dispatchers.IO) {
            service.addOrUpdateHabit(habit)
            getHabitsFromServer()
        }

    suspend fun delete(habit: Habit) =
        withContext(Dispatchers.IO) {
            service.deleteHabit(DeleteHabitBody(habit.id))
            getHabitsFromServer()
        }

    fun getHabit(id: String?): LiveData<Habit?> =
        habitDao.getHabit(id)

    suspend fun getHabitsFromServer() =
        withContext(Dispatchers.IO) {
            val habits = service.getAllHabits()
            habitDao.clear()
            habitDao.insert(habits)
        }

    fun applyFilters(filter: Filter): LiveData<List<Habit>> =
        when (filter.sortCriterion) {
            SortCriterion.CREATION_DATE_DESCENDING, SortCriterion.CREATION_DATE_ASCENDING ->
                habitDao.getByCreationDate(filter.priorities, filter.colors,
                    filter.sortCriterion.isAscending, filter.searchQuery)
        }
}
