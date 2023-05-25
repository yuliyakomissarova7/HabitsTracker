package com.example.data.repository

import com.example.data.db.HabitDao
import com.example.data.db.HabitService
import com.example.domain.HabitRepository
import com.example.domain.entities.DeleteHabitBody
import com.example.domain.entities.Filter
import com.example.domain.entities.Habit
import com.example.domain.entities.DoneHabitBody
import com.example.domain.entities.SortCriterion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date

class HabitRepositoryImpl(private val habitDao: HabitDao, private val service: HabitService):
    HabitRepository {
    override var habits: Flow<List<Habit>> = habitDao.getAll()

    override suspend fun createOrUpdate(habit: Habit) =
        withContext(Dispatchers.IO) {
            service.addOrUpdateHabit(habit)
            getHabitsFromServer()
        }

    override suspend fun delete(habitId: String) =
        withContext(Dispatchers.IO) {
            habitDao.delete(habitId)
            service.deleteHabit(DeleteHabitBody(habitId))
        }

    override fun getHabit(id: String?): Habit? =
        habitDao.getHabit(id)

    override suspend fun getHabitsFromServer() =
        withContext(Dispatchers.IO) {
            val habits = service.getAllHabits()
            habitDao.clear()
            habitDao.insert(habits)
        }

    override fun applyFilters(filter: Filter): Flow<List<Habit>> =
        when (filter.sortCriterion) {
            SortCriterion.CREATION_DATE_DESCENDING, SortCriterion.CREATION_DATE_ASCENDING ->
                habitDao.getByCreationDate(filter.priorities, filter.colors,
                    filter.sortCriterion.isAscending, filter.searchQuery)
        }

    override suspend fun checkHabit(date: Date, habitId: String) =
        withContext(Dispatchers.IO) {
            service.checkHabit(DoneHabitBody(date, habitId))
            val updatedHabit = service.getAllHabits().find { it.id == habitId }
            if (updatedHabit != null) {
                habitDao.createOrUpdate(updatedHabit)
            }
        }
}