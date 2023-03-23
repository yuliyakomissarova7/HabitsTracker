package com.example.habitstracker.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.habitstracker.*
import com.example.habitstracker.extensions.customGetSerializable
import com.example.habitstracker.extensions.toHex

const val ARG_HABIT = "habit"
const val ARG_HABIT_POSITION = "habitPosition"

const val REQUEST_KEY = "setSelectedColorRequest"
const val BUNDLE_KEY = "selectedColorId"

class HabitEditingFragment : Fragment(R.layout.fragment_habit_editing) {

    private var title: EditText? = null
    private var type: RadioGroup? = null
    private var priority: Spinner? = null
    private var repetitionTimes: EditText? = null
    private var repetitionPeriod: Spinner? = null
    private var color: TextView? = null
    private var description: EditText? = null
    private var selectedColor: Int = R.color.default_color

    private var titleIsRequiredMessage: TextView? = null

    private var habit: Habit? = null
    private var habitPosition: Int = -1

    private lateinit var activityContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habit = it.customGetSerializable(ARG_HABIT, Habit::class.java)
            habitPosition = it.getInt(ARG_HABIT_POSITION)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityContext = activity as Context

        findViews(view)

        if (habit !== null) {
            getParams(habit!!)
        }

        OnTitleEditText()
        onPrioritySpinner()
        onRepetitionTimesEditText(view)
        onRepetitionPeriodSpinner()

        onColorValue()

        setListenerOnCloseButton(view)
        onSaveButton(view)
    }

    private fun getParams(habit: Habit) {
        title?.setText(habit.title)
        type?.check(when (habit.type) {
            HabitType.GOOD -> R.id.good_habit_button
            HabitType.BAD -> R.id.bad_habit_button
        })
        priority?.setSelection(HabitPriority.values().indexOf(habit.priority))
        repetitionTimes?.setText(habit.repetitionTimes.toString())
        repetitionPeriod?.setSelection(Period.values().indexOf(habit.repetitionPeriod))
        setSelectedColor(habit.colorId)
        description?.setText(habit.description)
    }

    private fun setListenerOnCloseButton(view: View) {
        val closeButton: ImageButton = view.findViewById(R.id.close_button)
        closeButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun findViews(view: View) {
        title = view.findViewById(R.id.habits_name)
        type = view.findViewById(R.id.radio_group)
        priority = view.findViewById(R.id.spinner_priority)
        repetitionTimes = view.findViewById(R.id.repetition_times)
        repetitionPeriod = view.findViewById(R.id.repetition_period)
        color = view.findViewById(R.id.color_value)
        description = view.findViewById(R.id.description)
    }

    private fun OnTitleEditText() {
        title?.doOnTextChanged { _, _, _, _ ->
            titleIsRequiredMessage?.visibility = View.GONE}
    }


    private fun onPrioritySpinner() {
        priority?.adapter = ArrayAdapter(
            activityContext,
            android.R.layout.simple_spinner_item,
            HabitPriority.values().map { getString(it.textId) }
        )
    }

    private fun onRepetitionTimesEditText(view: View) {
        val repeatTimes: TextView = view.findViewById(R.id.repeat_times)

        repetitionTimes?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && repetitionTimes?.text.toString().isEmpty()) {
                repetitionTimes?.setText(0.toString())
            }
        }

        repetitionTimes?.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                repeatTimes.text = resources.getQuantityString(R.plurals.times, it.toString().toInt())
            }
        }
    }

    private fun onRepetitionPeriodSpinner() {
        repetitionPeriod?.adapter = ArrayAdapter(
            activityContext,
            android.R.layout.simple_spinner_item,
            Period.values().map {getString(it.textId)}
        )
    }

    private fun onColorValue() {
        color?.setOnClickListener {
            val colorPicker: DialogFragment = ColorPickerFragment.newInstance(selectedColor)
            parentFragmentManager.setFragmentResultListener(REQUEST_KEY, this) { _, bundle ->
                val selectedColorId = bundle.getInt(BUNDLE_KEY)
                setSelectedColor(selectedColorId)
            }
            colorPicker.show(parentFragmentManager, ColorPickerFragment::class.java.name) }
    }

    private fun setSelectedColor(colorId: Int) {
        selectedColor = colorId

        val colorValue = activityContext.getColor(colorId)
        color?.setTextColor(ColorStateList.valueOf(colorValue))
        color?.text = colorValue.toHex()
    }

    private fun editHabit(newHabit: Habit) {
        if (newHabit.type == habit?.type) {
            val habits = MainActivity.habits[newHabit.type]
            if (habits != null) {
                habits[habitPosition] = newHabit
            }
        } else {
            MainActivity.habits[habit?.type]?.remove(habit)
            MainActivity.habits[newHabit.type]?.add(newHabit)
        }
    }

    private fun onSaveButton(view: View) {
        val saveButton: Button = view.findViewById(R.id.save_button)

        saveButton.setOnClickListener {
            val title = title?.text.toString()

            if (title.isEmpty()) {
                titleIsRequiredMessage?.visibility = View.VISIBLE
            } else {
                val type: HabitType = when (type?.checkedRadioButtonId) {
                    R.id.bad_habit_button -> HabitType.BAD
                    else -> HabitType.GOOD
                }
                val priority: HabitPriority = HabitPriority.values()[priority?.selectedItemPosition ?: 0]
                val repetitionTimes: Int = repetitionTimes?.text.toString().toInt()
                val repetitionPeriod: Period = Period.values()[repetitionPeriod?.selectedItemPosition ?: 0]
                val description: String = description?.text.toString()

                val newHabit = Habit(
                    title,
                    type,
                    priority,
                    repetitionTimes,
                    repetitionPeriod,
                    description,
                    selectedColor,
                )

                if (habitPosition >= 0 && habit != null) {
                    editHabit(newHabit)
                } else {
                    MainActivity.habits[newHabit.type]?.add(newHabit)
                }
                findNavController().popBackStack()
            }
        }
    }
}