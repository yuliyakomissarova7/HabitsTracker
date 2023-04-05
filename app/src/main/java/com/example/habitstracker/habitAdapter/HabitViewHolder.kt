package com.example.habitstracker.habitAdapter

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.*
import com.example.habitstracker.entities.*
import com.example.habitstracker.fragments.MainFragmentDirections

class HabitViewHolder(itemView: View, private val context: Context,
                      private val navController: NavController, private val habitType: HabitType
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val title: TextView = itemView.findViewById(R.id.title)
    private val repeatInformation: TextView = itemView.findViewById(R.id.repeat_information)
    val color: View = itemView.findViewById(R.id.habit_card_color)

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        navController.navigate(MainFragmentDirections.actionMainFragmentToEditHabitFragment(adapterPosition,
            MainActivity.habits[habitType]!![adapterPosition]))
    }

    fun bind(habit: Habit) {
        title.text = habit.title
        repeatInformation.text = getRepetitionInformation(habit.repetitionTimes, habit.repetitionPeriod)
        color.backgroundTintList = ContextCompat.getColorStateList(context, habit.colorId)
    }

    private fun getRepetitionInformation(repetitionTimes: Int, repetitionPeriod: Period): String {
        if (repetitionTimes == 0) {
            return context.getString(R.string.never)
        }
        if (repetitionTimes == 1 && repetitionPeriod == Period.DAY) {
            return context.getString(R.string.every_day)
        }

        return "$repetitionTimes ${context.resources.getQuantityString(R.plurals.times, repetitionTimes)} ${context.getString(repetitionPeriod.textId)}"
    }
}