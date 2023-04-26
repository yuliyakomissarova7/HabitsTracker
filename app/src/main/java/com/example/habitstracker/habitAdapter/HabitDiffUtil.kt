package com.example.habitstracker.habitAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.habitstracker.entities.Habit

class HabitDiffUtil: DiffUtil.ItemCallback<Habit>() {
    override fun areItemsTheSame(oldItem: Habit, newItem: Habit) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit) =
        oldItem == newItem
}