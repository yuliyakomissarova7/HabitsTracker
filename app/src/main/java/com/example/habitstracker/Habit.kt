package com.example.habitstracker

import android.content.Context

data class Habit(val title: String,
                 val type: HabitType,
                 val priority: HabitPriority,
                 val repetitionTimes: Int,
                 val repetitionPeriod: Period,
                 val description: String?,
                 val colorId: Int): java.io.Serializable

enum class HabitType(val textId: Int, val radioButtonId: Int){
    BAD(R.string.bad_habit, R.id.bad_habit_button),
    GOOD(R.string.good_habit, R.id.good_habit_button);

    companion object {
        infix fun from(radioButtonId: Int?): HabitType =
            HabitType.values().firstOrNull { it.radioButtonId == radioButtonId } ?: GOOD
    }
}

enum class HabitPriority(val textId: Int){
    HIGH(R.string.high_priority),
    MEDIUM(R.string.medium_priority),
    LOW(R.string.low_priority);

    companion object {
        fun strings(context: Context): List<String> =
            HabitPriority.values().map { context.getString(it.textId) }

        infix fun at (position: Int?): HabitPriority = HabitPriority.values()[position ?: 0]
    }
}

enum class Period(val textId: Int) {
    DAY(R.string.day),
    WEEK (R.string.week),
    MONTH (R.string.month),
    YEAR (R.string.year);

    companion object {
        fun strings(context: Context): List<String> =
            Period.values().map { context.getString(it.textId) }

        infix fun at (position: Int?): Period = Period.values()[position ?: 0]
    }
}
