package com.example.habitstracker.habitAdapter

import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entities.Habit
import com.example.domain.entities.HabitType
import com.example.domain.entities.Period
import com.example.habitstracker.*
import com.example.habitstracker.databinding.HabitCardBinding
import com.example.habitstracker.view.MainFragmentDirections
import com.example.habitstracker.viewmodel.HabitEditingViewModel

class HabitViewHolder(private val binding: HabitCardBinding,
                      private val navController: NavController,
                      private val habitEditingViewModel: HabitEditingViewModel): RecyclerView.ViewHolder(binding.root) {

    fun bind(habit: Habit) {
        binding.title.text = habit.title
        binding.repeatInformation.text = getRepetitionInformation(habit.repetitionTimes, habit.repetitionPeriod)
        binding.habitCardColor.backgroundTintList = ContextCompat.getColorStateList(itemView.context, habit.color.colorId)

        itemView.setOnClickListener {
            navController.navigate(MainFragmentDirections.actionMainFragmentToEditHabitFragment(habit.id))
        }

        setOnCheckListener(habit)
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

    private fun setOnCheckListener(habit: Habit) {
        binding.habitDoneButton.setOnClickListener {
            habitEditingViewModel.check(habit.id)
            val (isHabitDone, doneTimes) = habitEditingViewModel.isHabitDone(habit)
            val message: String = when (isHabitDone) {
                true -> {
                    when (habit.type) {
                        HabitType.GOOD -> itemView.context.getString(R.string.good_habit_done)
                        HabitType.BAD -> itemView.context.getString(R.string.bad_habit_not_ok)
                    }
                }

                false -> {
                    when (habit.type) {
                        HabitType.GOOD -> itemView.context.getString(R.string.good_habit_not_done) + addTextToToast(habit.repetitionTimes-doneTimes)
                        HabitType.BAD -> itemView.context.getString(R.string.bad_habit_ok) + addTextToToast(habit.repetitionTimes-doneTimes)
                    }
                }
            }
            Toast
                .makeText(itemView.context, message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun addTextToToast(count: Int): String {
        return if (count in 0..1 || count in 5..20){
            " $count раз"
        } else {
            " $count раза"
        }
    }
}