package com.example.habitstracker.habitAdapter

import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.*
import com.example.habitstracker.databinding.HabitCardBinding
import com.example.habitstracker.entities.*
import com.example.habitstracker.view.MainFragmentDirections

class HabitViewHolder(private val binding: HabitCardBinding,
                      private val navController: NavController): RecyclerView.ViewHolder(binding.root) {

    fun bind(habit: Habit) {
        binding.title.text = habit.title
        binding.repeatInformation.text = getRepetitionInformation(habit.repetitionTimes, habit.repetitionPeriod)
        binding.habitCardColor.backgroundTintList = ContextCompat.getColorStateList(itemView.context, habit.color.colorId)

        itemView.setOnClickListener {
            navController.navigate(MainFragmentDirections.actionMainFragmentToEditHabitFragment(habit.id))
        }
    }

    private fun getRepetitionInformation(repetitionTimes: Int, repetitionPeriod: Period): String {
        if (repetitionTimes == 0) {
            return itemView.context.getString(R.string.never)
        }
        if (repetitionTimes == 1 && repetitionPeriod == Period.DAY) {
            return itemView.context.getString(R.string.every_day)
        }

        return "$repetitionTimes ${itemView.context.resources.getQuantityString(R.plurals.times, repetitionTimes)} ${itemView.context.getString(repetitionPeriod.textId)}"
    }
}