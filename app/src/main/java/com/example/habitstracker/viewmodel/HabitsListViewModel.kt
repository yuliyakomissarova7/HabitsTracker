package com.example.habitstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.habitstracker.entities.Habit
import com.example.habitstracker.entities.*
import com.example.habitstracker.db.HabitRepository

data class Filter(
    val habits: LiveData<List<Habit>>,
    var priorities: MutableSet<Priority>,
    var colors: MutableSet<HabitColor>,
    var sortCriterion: SortCriterion,
    var searchQuery: String,
)

class HabitsListViewModel(private val repository: HabitRepository): ViewModel() {
    private var _sortCriterion: SortCriterion = SortCriterion.CREATION_DATE_DESCENDING
    private var _selectedPriorities: MutableSet<Priority> = mutableSetOf()
    private var _selectedColors: MutableSet<HabitColor> = mutableSetOf()
    private var _searchQuery: String = ""

    private var filter: MutableLiveData<Filter> = MutableLiveData<Filter>(Filter(
        repository.habits,
        _selectedPriorities,
        _selectedColors,
        _sortCriterion,
        _searchQuery,
    ))

    var habits: LiveData<List<Habit>> = repository.habits

    private var selectedPrioritiesMutableLiveData: MutableLiveData<MutableSet<Priority>> = MutableLiveData(_selectedPriorities)
    private var selectedColorsMutableLiveData: MutableLiveData<MutableSet<HabitColor>> = MutableLiveData(_selectedColors)

    val selectedPriorities: LiveData<MutableSet<Priority>> = selectedPrioritiesMutableLiveData
    val selectedColors: LiveData<MutableSet<HabitColor>> = selectedColorsMutableLiveData

    init {
        resetFilters()
        applyFilters()

        habits = Transformations.switchMap(filter) { filter ->
            repository.applyFilters(filter)
        }
    }

    fun <T> removeFromFilter(option: T, filterType: FilterCriterion) {
        when (filterType) {
            FilterCriterion.PRIORITY -> {
                _selectedPriorities.remove(option as Priority)
                selectedPrioritiesMutableLiveData.value = _selectedPriorities
            }
            FilterCriterion.COLOR -> {
                _selectedColors.remove(option as HabitColor)
                selectedColorsMutableLiveData.value = _selectedColors
            }
        }
        applyFilters()
    }

    fun <T> addToFilter(option: T, filterType: FilterCriterion) {
        when (filterType) {
            FilterCriterion.PRIORITY -> {
                _selectedPriorities.add(option as Priority)
                selectedPrioritiesMutableLiveData.value = _selectedPriorities
            }
            FilterCriterion.COLOR -> {
                _selectedColors.add(option as HabitColor)
                selectedColorsMutableLiveData.value = _selectedColors
            }
        }
        applyFilters()
    }

    fun search(query: String) {
        resetFilters()
        _searchQuery = query
        applyFilters()
    }

    fun sortBy(sortType: SortCriterion) {
        _sortCriterion = sortType
        applyFilters()
    }

    private fun resetFilters() {
        _sortCriterion = SortCriterion.CREATION_DATE_DESCENDING
        _selectedPriorities.addAll(Priority.values())
        selectedPrioritiesMutableLiveData.value = _selectedPriorities
        _selectedColors.addAll(HabitColor.values())
        selectedColorsMutableLiveData.value = _selectedColors
        _searchQuery = ""
    }

    private fun applyFilters() {
        filter.value = Filter(
            repository.habits,
            _selectedPriorities,
            _selectedColors,
            _sortCriterion,
            _searchQuery
        )
    }

    private fun isFilteredByPriority(priority: Priority): Boolean =
        _selectedPriorities.contains(priority)

    private fun isFilteredByColor(color: HabitColor): Boolean =
        _selectedColors.contains(color)

    fun getCheckedPriorities(): BooleanArray =
        BooleanArray(Priority.values().size) { index: Int ->
            isFilteredByPriority(
                Priority.values()[index]
            )
        }

    fun getCheckedColors(): BooleanArray =
        BooleanArray(HabitColor.values().size) { index: Int ->
            isFilteredByColor(
                HabitColor.values()[index]
            )
        }
}