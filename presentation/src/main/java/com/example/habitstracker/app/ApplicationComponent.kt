package com.example.habitstracker.app

import com.example.habitstracker.view.FilterBottomSheetFragment
import com.example.habitstracker.view.HabitEditingFragment
import com.example.habitstracker.view.HabitsListFragment
import com.example.habitstracker.view.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DaggerModule::class])
interface ApplicationComponent {
    fun inject(mainFragment: MainFragment)
    fun inject(habitsListFragment: HabitsListFragment)
    fun inject(filterBottomSheetFragment: FilterBottomSheetFragment)
    fun inject(editHabitFragment: HabitEditingFragment)
}