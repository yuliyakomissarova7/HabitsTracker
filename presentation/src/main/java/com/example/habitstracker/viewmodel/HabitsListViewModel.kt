package com.example.habitstracker.viewmodel

import androidx.lifecycle.*
import com.example.domain.entities.Habit
import com.example.domain.entities.*
import com.example.domain.usecases.SearchAndFilterHabitUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitsListViewModel(private val searchAndFilterHabitUseCase: SearchAndFilterHabitUseCase): ViewModel() {
    private var _sortCriterion: SortCriterion = SortCriterion.CREATION_DATE_DESCENDING
    private var _selectedPriorities: MutableSet<Priority> = mutableSetOf()
    private var _selectedColors: MutableSet<HabitColor> = mutableSetOf()
    private var _searchQuery: String = ""

    private var filter: MutableLiveData<Filter> = MutableLiveData<Filter>(Filter(
        searchAndFilterHabitUseCase.habits.asLiveData(),
        _selectedPriorities,
        _selectedColors,
        _sortCriterion,
        _searchQuery,
    ))

    var habits: LiveData<List<Habit>>

    private var selectedPrioritiesMutableLiveData: MutableLiveData<MutableSet<Priority>> = MutableLiveData(_selectedPriorities)
    private var selectedColorsMutableLiveData: MutableLiveData<MutableSet<HabitColor>> = MutableLiveData(_selectedColors)

    val selectedPriorities: LiveData<MutableSet<Priority>> = selectedPrioritiesMutableLiveData
    val selectedColors: LiveData<MutableSet<HabitColor>> = selectedColorsMutableLiveData

    init {
        resetFilters()
        applyFilters()

        habits = filter.switchMap { filter ->
            searchAndFilterHabitUseCase.applyFilters(filter).asLiveData()
        }

        viewModelScope.launch(Dispatchers.IO) {
            searchAndFilterHabitUseCase.getHabitsFromServer() }
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
            searchAndFilterHabitUseCase.habits.asLiveData(),
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