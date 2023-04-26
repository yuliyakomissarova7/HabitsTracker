package com.example.habitstracker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.habitstracker.HabitTrackerApplication
import com.example.habitstracker.R
import com.example.habitstracker.databinding.FragmentMainBinding
import com.example.habitstracker.entities.HabitType
import com.example.habitstracker.viewmodel.HabitsListViewModel
import com.example.habitstracker.viewmodel.HabitsListViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment: Fragment() {
    private lateinit var viewModel: HabitsListViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(activity as ViewModelStoreOwner,
            HabitsListViewModelFactory((activity?.application as HabitTrackerApplication).repository))[HabitsListViewModel::class.java]


        val viewPager: ViewPager2 = binding.typeHabitsList
        val habitsListAdapter = HabitsListAdapter(this)
        viewPager.adapter = habitsListAdapter

        val tabLayout: TabLayout = binding.typeTabs
        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
            val type: HabitType = HabitType.values()[position]
            tab.text = when (type) {
                HabitType.GOOD -> getString(R.string.good_habits_title)
                HabitType.BAD -> getString(R.string.bad_habits_title)
            }
        }.attach()

        startFilterBottomSheet(savedInstanceState)
        setListenerOnAddHabitButton()
    }

    private fun setListenerOnAddHabitButton() {
        val addHabitButton: FloatingActionButton = binding.addHabitButton
        addHabitButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToEditHabitFragment())
        }
    }

    private fun startFilterBottomSheet(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            parentFragmentManager.beginTransaction().add(R.id.filter_bottom_sheet,
                FilterBottomSheetFragment()).commit()
        }
    }
}