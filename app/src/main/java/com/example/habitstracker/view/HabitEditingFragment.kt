package com.example.habitstracker.view


import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.TextViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.habitstracker.*
import com.example.habitstracker.databinding.FragmentHabitEditingBinding
import com.example.habitstracker.entities.*
import com.example.habitstracker.extensions.customGetSerializable
import com.example.habitstracker.extensions.toHex
import com.example.habitstracker.viewmodel.HabitEditingViewModelFactory
import com.example.habitstracker.viewmodel.HabitEditingViewModel
import java.util.*

const val ARG_HABIT_ID = "habitId"

const val REQUEST_KEY = "setSelectedColorRequest"
const val BUNDLE_KEY = "selectedColor"

class HabitEditingFragment : Fragment() {
    private lateinit var viewModel: HabitEditingViewModel
    private lateinit var binding: FragmentHabitEditingBinding
    private var habitId: Long? = -1
    private var selectedColor: HabitColor = HabitColor.defaultColor()
    private var titleIsRequiredMessage: TextView? = null
    private lateinit var activityContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habitId = if (it.getLong(ARG_HABIT_ID) == -1L) { null } else {
                it.getLong(ARG_HABIT_ID)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHabitEditingBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this,
            HabitEditingViewModelFactory((activity?.application as HabitTrackerApplication).repository, habitId)
        )[HabitEditingViewModel::class.java]


        activityContext = activity as Context

        viewModel.habit.observe(viewLifecycleOwner) {
                habit ->
            if (habit !== null) {
                getParams(habit)

                binding.deleteButton.visibility = View.VISIBLE
                binding.deleteButton.setOnClickListener {
                    viewModel.delete(habit)
                    findNavController().popBackStack()
                }
            } else {
                binding.deleteButton.visibility = View.GONE
            }
        }

        onTitleEditText()
        onPrioritySpinner()
        onRepetitionTimesEditText()
        onRepetitionPeriodSpinner()
        onColorValue()
        setListenerOnCloseButton()
        onSaveButton()
    }

    private fun getParams(habit: Habit) {
        binding.habitsTitle.setText(habit.title)
        binding.type.check(when (habit.type) {
            HabitType.GOOD -> R.id.good_habit_button
            HabitType.BAD -> R.id.bad_habit_button
        })
        binding.priority.setSelection(Priority.values().indexOf(habit.priority))
        binding.repetitionTimes.setText(habit.repetitionTimes.toString())
        binding.repetitionPeriod.setSelection(Period.values().indexOf(habit.repetitionPeriod))
        setSelectedColor(habit.color)
        binding.description.setText(habit.description)
    }

    private fun setListenerOnCloseButton() {
        val closeButton: ImageButton = binding.closeButton
        closeButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onTitleEditText() {
        binding.habitsTitle.doOnTextChanged { _, _, _, _ ->
            titleIsRequiredMessage?.visibility = View.GONE}
    }


    private fun onPrioritySpinner() {
        binding.priority.adapter = ArrayAdapter(
            activityContext,
            android.R.layout.simple_spinner_item,
            Priority.values().map { getString(it.textId) }
        )
    }

    private fun onRepetitionTimesEditText() {
        val repeatTimes: TextView = binding.repeatTimes

        binding.repetitionTimes.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.repetitionTimes.text.toString().isEmpty()) {
                binding.repetitionTimes.setText(0.toString())
            }
        }

        binding.repetitionTimes.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                repeatTimes.text = resources.getQuantityString(R.plurals.times, it.toString().toInt())
            }
        }
    }

    private fun onRepetitionPeriodSpinner() {
        binding.repetitionPeriod.adapter = ArrayAdapter(
            activityContext,
            android.R.layout.simple_spinner_item,
            Period.values().map {getString(it.textId)}
        )
    }

    private fun onColorValue() {
        binding.colorValue.setOnClickListener {
            val colorPicker: DialogFragment = ColorPickerFragment.newInstance(selectedColor)
            parentFragmentManager.setFragmentResultListener(REQUEST_KEY, this) { _, bundle ->
                val selectedColorId = bundle.customGetSerializable(BUNDLE_KEY, HabitColor::class.java)
                    ?: HabitColor.defaultColor()
                setSelectedColor(selectedColorId)
            }
            colorPicker.show(parentFragmentManager, ColorPickerFragment::class.java.name) }
    }

    private fun setSelectedColor(_color: HabitColor) {
        selectedColor = _color

        val colorValue = activityContext.getColor(_color.colorId)
        TextViewCompat.setCompoundDrawableTintList(
            binding.colorValue,
            ColorStateList.valueOf(colorValue)
        )
        binding.colorValue.setTextColor(colorValue)
        binding.colorValue.text = colorValue.toHex()
    }

    private fun onSaveButton() {
        val saveButton: Button = binding.saveButton

        saveButton.setOnClickListener {
            if (binding.habitsTitle.text.toString().isEmpty()) {
                binding.titleIsRequiredMessage.visibility = View.VISIBLE
            } else {
                val newHabit = getNewHabit()
                viewModel.createOrUpdate(newHabit)
                findNavController().popBackStack()
            }
        }
    }

    private fun getNewHabit(): Habit {
        val type: HabitType = when (binding.type.checkedRadioButtonId) {
            R.id.bad_habit_button -> HabitType.BAD
            else -> HabitType.GOOD
        }
        val title = binding.habitsTitle.text.toString()
        val priority: Priority = Priority.values()[binding.priority.selectedItemPosition]
        val repetitionTimes: Int = binding.repetitionTimes.text.toString().toInt()
        val repetitionPeriod: Period = Period.values()[binding.repetitionPeriod.selectedItemPosition]
        val description: String = binding.description.text.toString()

        return Habit(
            title,
            type,
            priority,
            repetitionTimes,
            repetitionPeriod,
            description,
            selectedColor,
            Date(),
            habitId ?: 0
        )
    }
}