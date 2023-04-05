package com.example.habitstracker.fragments

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.habitstracker.entities.HabitType

class HabitsListAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = HabitType.values().size


    override fun createFragment(position: Int): Fragment {
        return HabitsListFragment.newInstance(HabitType.values()[position])
    }
}
