package com.example.habitstracker

import android.content.Context

data class Habit(val title: String,
                 val type: HabitType,
                 val priority: HabitPriority,
                 val repetitionTimes: Int,
                 val repetitionPeriod: Period,
                 val description: String?,
                 val colorId: Int): java.io.Serializable

enum class HabitType(val textId: Int){
    BAD(R.string.bad_habit),
    GOOD(R.string.good_habit);
}


enum class Period(val textId: Int) {
    DAY(R.string.day),
    WEEK (R.string.week),
    MONTH (R.string.month),
    YEAR (R.string.year);
}

enum class HabitPriority(val textId: Int){
    HIGH(R.string.high_priority),
    MEDIUM(R.string.medium_priority),
    LOW(R.string.low_priority);
}
