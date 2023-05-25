package com.example.habitstracker.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entities.HabitType
import com.example.domain.usecases.HabitEditingUseCase
import com.example.domain.usecases.SearchAndFilterHabitUseCase
import com.example.habitstracker.*
import com.example.habitstracker.app.HabitTrackerApplication
import com.example.habitstracker.extensions.customGetSerializable
import com.example.habitstracker.habitAdapter.HabitAdapter
import com.example.habitstracker.viewmodel.HabitEditingViewModel
import com.example.habitstracker.viewmodel.HabitEditingViewModelFactory
import com.example.habitstracker.viewmodel.HabitsListViewModel
import com.example.habitstracker.viewmodel.HabitsListViewModelFactory
import javax.inject.Inject

private const val ARG_TYPE = "type"

class HabitsListFragment : Fragment(R.layout.fragment_habits_list) {
    private lateinit var habitsListViewModel: HabitsListViewModel
    private lateinit var type: HabitType
    private lateinit var habitEditingViewModel: HabitEditingViewModel
    @Inject
    lateinit var searchAndFilterHabitUseCase: SearchAndFilterHabitUseCase
    @Inject
    lateinit var habitEditingUseCase: HabitEditingUseCase

    companion object {
        fun newInstance(type: HabitType) =
            HabitsListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TYPE, type)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            type = it.customGetSerializable(ARG_TYPE, HabitType::class.java) ?: HabitType.GOOD
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as HabitTrackerApplication).applicationComponent.inject(this)

        habitsListViewModel = ViewModelProvider(activity as ViewModelStoreOwner,
            HabitsListViewModelFactory(searchAndFilterHabitUseCase))[HabitsListViewModel::class.java]

        habitEditingViewModel = ViewModelProvider(activity as ViewModelStoreOwner,
            HabitEditingViewModelFactory(habitEditingUseCase))[HabitEditingViewModel::class.java]

        val recyclerView: RecyclerView = view.findViewById(R.id.habits_list)
        val navController: NavController = findNavController()
        val habitAdapter = HabitAdapter(navController, habitEditingViewModel)

        val noHabitsMessage: TextView = view.findViewById(R.id.no_habits_message)
        noHabitsMessage.text = when (type) {
            HabitType.GOOD -> getString(R.string.habits_list_no_good_habits)
            HabitType.BAD -> getString(R.string.habits_list_no_bad_habits)
        }

        habitsListViewModel.habits.observe(viewLifecycleOwner) { habits ->
            val habitsOfType = habits.filter { it.type === type }
            habitAdapter.submitList(habitsOfType)
            if (habitsOfType.isEmpty()) {
                noHabitsMessage.visibility = View.VISIBLE
            } else {
                noHabitsMessage.visibility = View.GONE
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(activity as Context)
        recyclerView.adapter = habitAdapter
    }
}