package com.example.habitstracker.db

import com.example.habitstracker.entities.DeleteHabitBody
import com.example.habitstracker.entities.Habit
import retrofit2.http.*

interface HabitService {
    @GET("habit")
    suspend fun getAllHabits(): List<Habit>

    @PUT("habit")
    suspend fun addOrUpdateHabit(@Body habit: Habit)

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(@Body uid: DeleteHabitBody)
}