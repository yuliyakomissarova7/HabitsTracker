package com.example.habitstracker.view

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.habitstracker.HabitTrackerApplication
import com.example.habitstracker.R
import com.example.habitstracker.databinding.FragmentFilterBottomSheetBinding
import com.example.habitstracker.entities.FilterCriterion
import com.example.habitstracker.entities.HabitColor
import com.example.habitstracker.entities.Priority
import com.example.habitstracker.entities.SortCriterion
import com.example.habitstracker.viewmodel.HabitsListViewModel
import com.example.habitstracker.viewmodel.HabitsListViewModelFactory


class MultiSelectOptions<T>(
    var values: Array<T>,
    var titles: Array<CharSequence>,
    var areChecked: BooleanArray
)

class FilterBottomSheetFragment : Fragment(R.layout.fragment_filter_bottom_sheet) {
    private lateinit var viewModel: HabitsListViewModel
    private lateinit var activityContext: Context
    private lateinit var binding: FragmentFilterBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBottomSheetBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(activity as ViewModelStoreOwner,
            HabitsListViewModelFactory((activity?.application as HabitTrackerApplication).repository))[HabitsListViewModel::class.java]

        activityContext = activity as Context

        search()
        sort()

        filter(view, FilterCriterion.PRIORITY, R.id.filter_by_priority,
            MultiSelectOptions(
                Priority.values(),
                Priority.values().map { activityContext.getString(it.textId) }.toTypedArray(),
                viewModel.getCheckedPriorities()
            ),
            R.string.bottom_sheet_priority_title)

        filter(view, FilterCriterion.COLOR, R.id.filter_by_color,
            MultiSelectOptions(
                HabitColor.values(),
                HabitColor.values().map { activityContext.getString(it.colorId) }.toTypedArray(),
                viewModel.getCheckedColors()
            ),
            R.string.bottom_sheet_color_title)
    }

    private fun <T> filter(view: View, filterCriterion: FilterCriterion, filterViewId: Int,
                           options: MultiSelectOptions<T>, titleId: Int) {
        val filterView: LinearLayout = view.findViewById(filterViewId)
        setMultiChoice(options, filterCriterion, titleId, filterView)

        when (filterCriterion) {
            FilterCriterion.PRIORITY -> {
                viewModel.selectedPriorities.observe(viewLifecycleOwner) {
                    run {
                        options.areChecked = viewModel.getCheckedPriorities()
                        setMultiChoice(options, FilterCriterion.PRIORITY,
                            R.string.bottom_sheet_priority_title, filterView)
                    }
                }
            }
            FilterCriterion.COLOR -> {
                val colorViews: List<View> = listOf(
                    binding.color1,
                    binding.color2,
                    binding.color3)
                val moreColorsNumber: TextView = binding.moreColorsNumber

                viewModel.selectedColors.observe(viewLifecycleOwner) {colors ->
                    run {
                        updateColorFilter(colors, moreColorsNumber, colorViews)
                        options.areChecked = viewModel.getCheckedColors()
                        setMultiChoice(options, FilterCriterion.COLOR,
                            R.string.bottom_sheet_color_title, filterView)
                    }
                }
            }
        }
    }

    private fun updateColorFilter(selectedColors: MutableSet<HabitColor>,
                                  moreColorsNumber: TextView, colorViews: List<View>) {
        val colorsList = selectedColors.toList()

        if (colorsList.size <= 3) {
            moreColorsNumber.visibility = View.GONE

            for (i in colorsList.indices) {
                colorViews[i].backgroundTintList = ColorStateList.valueOf(activityContext.getColor(colorsList[i].colorId))
                colorViews[i].visibility = View.VISIBLE
            }
            for (i in colorsList.size..2) {
                colorViews[i].visibility = View.GONE
            }
        } else {
            moreColorsNumber.visibility = View.VISIBLE
            val text = "+${colorsList.size - 3}"
            moreColorsNumber.text = text

            for (i in 0..2) {
                colorViews[i].backgroundTintList = ColorStateList.valueOf(activityContext.getColor(colorsList[i].colorId))
                colorViews[i].visibility = View.VISIBLE
            }
        }
    }

    private fun <T> setMultiChoice(options: MultiSelectOptions<T>, filterCriterion: FilterCriterion,
                                   titleId: Int, filterView: View) {
        val dialogBuilder = AlertDialog.Builder(activityContext)

        dialogBuilder.setMultiChoiceItems(options.titles, options.areChecked) { _, index, isChecked ->
            if (!isChecked) {
                viewModel.removeFromFilter(options.values[index], filterCriterion)
            } else {
                viewModel.addToFilter(options.values[index], filterCriterion)
            }
        }.setTitle(titleId)

        filterView.setOnClickListener {
            dialogBuilder.show()
        }
    }

    private fun search() {
        val search:androidx.appcompat.widget.SearchView = binding.search

        search.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.search(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.search(newText)
                return false
            }
        })
    }

    private fun sort() {
        val sort: Spinner = binding.sort
        sort.adapter = ArrayAdapter(
            activity as Context,
            android.R.layout.simple_spinner_item,
            SortCriterion.values().map { getString(it.textId) }
        )
        sort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.sortBy(SortCriterion.values()[position])
            }
        }
    }
}