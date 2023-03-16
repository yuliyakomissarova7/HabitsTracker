package com.example.habitstracker

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged

class HabitEditingActivity : AppCompatActivity(), ColorPicker.OnSaveColorListener {

    private var titleEditText: EditText? = null
    private var typeRadioGroup: RadioGroup? = null
    private var prioritySpinner: Spinner? = null
    private var repetitionTimesEditText: EditText? = null
    private var repetitionPeriodSpinner: Spinner? = null
    private var colorValueTextView: TextView? = null
    private var descriptionEditText: EditText? = null
    private var selectedColorId: Int = R.color.default_color

    private var titleIsRequiredMessage: TextView? = null

    private var editedHabitPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_editing)

        findViews()

        setListenerOnTitleEditText()
        setListenerOnRepetitionTimesEditText()
        setAdapterForRepetitionPeriodSpinner()
        setListenerForPrioritySpinner()
        setListenerOnColorValue()

        setListenerOnCloseButton()
        setListenerOnSaveButton()
    }

    private fun setListenerOnCloseButton() {
        val closeButton: ImageButton = findViewById(R.id.close_button)
        closeButton.setOnClickListener { finish() }
    }

    private fun findViews() {
        titleEditText = findViewById(R.id.habits_name)
        typeRadioGroup = findViewById(R.id.radio_group)
        prioritySpinner = findViewById(R.id.spinner_priority)
        repetitionTimesEditText = findViewById(R.id.repetition_times)
        repetitionPeriodSpinner = findViewById(R.id.repetition_period)
        colorValueTextView = findViewById(R.id.color_value)
        descriptionEditText = findViewById(R.id.description)
        titleIsRequiredMessage = findViewById(R.id.title_is_required_message)
    }

    override fun onStart() {
        super.onStart()

        val habit: Habit? = intent.getSerializable(getString(R.string.intent_extra_habit), Habit::class.java)
        val habitPosition: Int = intent.getIntExtra(getString(R.string.intent_extra_habit_position), -1)
        if (habit != null && habitPosition >= 0) {
            titleEditText?.setText(habit.title)
            typeRadioGroup?.check(habit.type.radioButtonId)
            prioritySpinner?.setSelection(HabitPriority.values().indexOf(habit.priority))
            repetitionTimesEditText?.setText(habit.repetitionTimes.toString())
            repetitionPeriodSpinner?.setSelection(Period.values().indexOf(habit.repetitionPeriod))
            descriptionEditText?.setText(habit.description)
            setSelectedColor(habit.colorId)

            editedHabitPosition = habitPosition
        }
    }

    private fun setListenerOnTitleEditText() {
        titleEditText?.doOnTextChanged {_, _, _, _ ->
            titleIsRequiredMessage?.visibility = View.GONE}
    }


    private fun setListenerForPrioritySpinner() {
        prioritySpinner?.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            HabitPriority.strings(this)
        )
    }

    private fun setListenerOnRepetitionTimesEditText() {
        val repeatTimes: TextView = findViewById(R.id.repeat_times)

        repetitionTimesEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && repetitionTimesEditText?.text.toString().isEmpty()) {
                repetitionTimesEditText?.setText(0.toString())
            }
        }

        repetitionTimesEditText?.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                repeatTimes.text = resources.getQuantityString(R.plurals.times, it.toString().toInt())
            }
        }
    }

    private fun setAdapterForRepetitionPeriodSpinner() {
        repetitionPeriodSpinner?.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Period.strings(this)
        )
    }

    private fun setListenerOnColorValue() {
        colorValueTextView?.setOnClickListener { showColorPicker() }
    }

    private fun showColorPicker() {
        val dialog = ColorPicker(this, selectedColorId)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun setSelectedColor(colorId: Int) {
        selectedColorId = colorId

        val colorValue = getColor(colorId)
        colorValueTextView?.setTextColor(ColorStateList.valueOf(colorValue))
        colorValueTextView?.text = colorValue.toHex()
    }

    private fun setListenerOnSaveButton() {
        val saveButton: Button = findViewById(R.id.save_button)

        saveButton.setOnClickListener {
            val title = titleEditText?.text.toString()

            if (title.isEmpty()) {
                titleIsRequiredMessage?.visibility = View.VISIBLE
            } else {
                val type = HabitType from typeRadioGroup?.checkedRadioButtonId
                val priority: HabitPriority = HabitPriority at prioritySpinner?.selectedItemPosition
                val repetitionTimes: Int = repetitionTimesEditText?.text.toString().toInt()
                val repetitionPeriod: Period = Period at repetitionPeriodSpinner?.selectedItemPosition
                val description: String = descriptionEditText?.text.toString()

                val newHabit = Habit(
                    title,
                    type,
                    priority,
                    repetitionTimes,
                    repetitionPeriod,
                    description,
                    selectedColorId,
                )

                if (editedHabitPosition >= 0 && editedHabitPosition < MainActivity.habits.size) {
                    MainActivity.habits[editedHabitPosition] = newHabit
                } else {
                    MainActivity.habits.add(newHabit)
                }
                finish()
            }
        }
    }
}