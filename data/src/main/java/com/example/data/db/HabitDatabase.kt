package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.domain.entities.Habit

@Database(entities = [Habit::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao() : HabitDao

    companion object {
        private var INSTANCE: HabitDatabase? = null

        fun getDatabase(context: Context): HabitDatabase {
            return INSTANCE ?: run {
                val instance = Room.databaseBuilder(
                    context,
                    HabitDatabase::class.java, "habits_db"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}