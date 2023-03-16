package com.example.habitstracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class HabitAdapter(private val habits: List<Habit>,
                   private val context: Context,
                   private val onHabitCardListener: OnHabitCardListener)
    : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    class HabitViewHolder(itemView: View, private val onHabitCardListener: OnHabitCardListener) : RecyclerView.ViewHolder(itemView), OnClickListener {
        val title: TextView = itemView.findViewById(R.id.title)
        val repeatInformation: TextView = itemView.findViewById(R.id.repeat_information)
        val color: View = itemView.findViewById(R.id.habit_card_color)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            onHabitCardListener.onHabitCardClick(adapterPosition)
        }
    }


    interface OnHabitCardListener{
        fun onHabitCardClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.habit_card, parent,
            false
        )
        return HabitViewHolder(itemView, onHabitCardListener)
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]

        holder.title.text = habit.title
        holder.repeatInformation.text = getRepetitionInformation(habit.repetitionTimes, habit.repetitionPeriod)
        holder.color.backgroundTintList = ContextCompat.getColorStateList(context, habit.colorId)
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
