package com.example.data.db

import androidx.room.*
import com.example.domain.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Upsert
    suspend fun createOrUpdate(habit: Habit)

    @Query("DELETE FROM habit WHERE id LIKE :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM habit WHERE id LIKE :id")
    fun getHabit(id: String?): Habit?

    @Query("SELECT * FROM habit")
    fun getAll(): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE priority IN (:selectedPriorities) " +
            "AND color IN (:selectedColors) AND title LIKE '%' || :searchQuery || '%' " +
            "ORDER BY CASE WHEN :isAscending = 1 THEN creationDate END ASC," +
            "CASE WHEN :isAscending = 0 THEN creationDate END DESC")
    fun getByCreationDate(
        selectedPriorities: Set<Priority>,
        selectedColors: Set<HabitColor>,
        isAscending: Boolean,
        searchQuery: String): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE priority IN (:selectedPriorities) " +
            "AND color IN (:selectedColors) AND title LIKE '%' || :searchQuery || '%' " +
            "ORDER BY CASE WHEN :isAscending = 1 THEN priority END ASC," +
            "CASE WHEN :isAscending = 0 THEN priority END DESC")
    fun getByPriority(
        selectedPriorities: Set<Priority>,
        selectedColors: Set<HabitColor>,
        isAscending: Boolean,
        searchQuery: String): Flow<List<Habit>>

    @Insert
    fun insert(habits: List<Habit>)

    @Query("DELETE FROM habit")
    fun clear()
}