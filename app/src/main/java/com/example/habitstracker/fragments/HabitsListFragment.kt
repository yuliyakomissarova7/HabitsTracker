package com.example.habitstracker.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.*
import com.example.habitstracker.entities.*
import com.example.habitstracker.extensions.customGetSerializable
import com.example.habitstracker.habitAdapter.HabitAdapter

private const val ARG_TYPE = "type"

class HabitsListFragment : Fragment(R.layout.fragment_habits_list) {

    private var type: HabitType = HabitType.GOOD
    private var habits: List<Habit> = listOf()

    companion object {
        fun newInstance(type: HabitType) =
            HabitsListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TYPE, type)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            type = it.customGetSerializable(ARG_TYPE, HabitType::class.java) ?: HabitType.GOOD
            habits = MainActivity.habits[type] ?: listOf()
        }

        val noHabitsMessage: TextView = view.findViewById(R.id.no_habits_message)
        noHabitsMessage.text = when (type) {
            HabitType.GOOD -> getString(R.string.habits_list_no_good_habits)
            HabitType.BAD -> getString(R.string.habits_list_no_bad_habits)
        }
        if (habits.isEmpty()) {
            noHabitsMessage.visibility = View.VISIBLE
        } else {
            noHabitsMessage.visibility = View.GONE
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.habits_list)
        val navController: NavController = findNavController()
        recyclerView.layoutManager = LinearLayoutManager(activity as Context)
        recyclerView.adapter = HabitAdapter(habits, type, activity as Context, navController)
    }
}