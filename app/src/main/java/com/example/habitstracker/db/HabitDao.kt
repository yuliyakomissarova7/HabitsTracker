package com.example.habitstracker.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.habitstracker.entities.Habit
import com.example.habitstracker.entities.HabitColor
import com.example.habitstracker.entities.Priority
import com.example.habitstracker.entities.*

@Dao
interface HabitDao {

    @Upsert
    suspend fun createOrUpdate(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("SELECT * FROM habit WHERE id LIKE :id")
    fun getHabit(id: Long?): LiveData<Habit?>

    @Query("SELECT * FROM habit")
    fun getAll(): LiveData<List<Habit>>

    @Query("SELECT * FROM habit WHERE priority IN (:selectedPriorities) " +
            "AND color IN (:selectedColors) AND title LIKE '%' || :searchQuery || '%' " +
            "ORDER BY CASE WHEN :isAscending = 1 THEN creationDate END ASC," +
            "CASE WHEN :isAscending = 0 THEN creationDate END DESC")
    fun getByCreationDate(
        selectedPriorities: Set<Priority>,
        selectedColors: Set<HabitColor>,
        isAscending: Boolean,
        searchQuery: String): LiveData<List<Habit>>

    @Query("SELECT * FROM habit WHERE priority IN (:selectedPriorities) " +
            "AND color IN (:selectedColors) AND title LIKE '%' || :searchQuery || '%' " +
            "ORDER BY CASE WHEN :isAscending = 1 THEN priority END ASC," +
            "CASE WHEN :isAscending = 0 THEN priority END DESC")
    fun getByPriority(
        selectedPriorities: Set<Priority>,
        selectedColors: Set<HabitColor>,
        isAscending: Boolean,
        searchQuery: String): LiveData<List<Habit>>
}