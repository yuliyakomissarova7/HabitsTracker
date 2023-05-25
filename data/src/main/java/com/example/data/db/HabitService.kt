package com.example.data.db

import com.example.domain.entities.DeleteHabitBody
import com.example.domain.entities.Habit
import com.example.domain.entities.DoneHabitBody
import retrofit2.http.*

interface HabitService {
    @GET("habit")
    suspend fun getAllHabits(): List<Habit>

    @PUT("habit")
    suspend fun addOrUpdateHabit(@Body habit: Habit)

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(@Body uid: DeleteHabitBody)

    @POST("habit_done")
    suspend fun checkHabit(@Body habitDone: DoneHabitBody)
}