package com.example.habitstracker.habitAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.ListAdapter
import com.example.habitstracker.databinding.HabitCardBinding
import com.example.domain.entities.Habit
import com.example.habitstracker.viewmodel.HabitEditingViewModel

class HabitAdapter(private val navController: NavController,
                   private val habitEditingViewModel: HabitEditingViewModel)
    : ListAdapter<Habit, HabitViewHolder>(HabitDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = HabitCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitViewHolder(binding, navController, habitEditingViewModel)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
