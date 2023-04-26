package com.example.habitstracker.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.setMargins
import androidx.fragment.app.DialogFragment
import com.example.habitstracker.R
import com.example.habitstracker.databinding.FragmentColorPickerBinding
import com.example.habitstracker.entities.HabitColor
import com.example.habitstracker.extensions.customGetSerializable
import com.google.android.material.button.MaterialButton
import com.example.habitstracker.extensions.toHex
import com.example.habitstracker.extensions.toRgb
import com.example.habitstracker.extensions.toHsv

private const val ARG_DEFAULT_COLOR = "default_color"

class ColorPickerFragment : DialogFragment() {
    private var selectedColor: HabitColor = HabitColor.defaultColor()
    private lateinit var context: Context
    private lateinit var binding: FragmentColorPickerBinding

    companion object {
        fun newInstance(defaultColorId: HabitColor) =
            ColorPickerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_DEFAULT_COLOR, defaultColorId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedColor = it.customGetSerializable(ARG_DEFAULT_COLOR, HabitColor::class.java)
                ?: HabitColor.defaultColor()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = FragmentColorPickerBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context = activity as Context

        setGradientBackground()
        createButtons()
        setSelectedColorValues(selectedColor)
        setListenerOnDefaultColorButton(view)
        setListenerOnSaveButton(view)
    }

    private fun setGradientBackground() {
        val gradientBackground = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            HabitColor.values()
                .map { context.getColor(it.colorId) }
                .toIntArray()
        )
        binding.colorPalette.background = gradientBackground
    }

    private fun createButtons() {
        for (color in HabitColor.values()) {
            binding.colorPalette.addView(createButton(color))
        }
    }

    private fun createButton(color: HabitColor): MaterialButton {
        val button = MaterialButton(ContextThemeWrapper(context, R.style.ColorPickerButton))
        val buttonSize = context.resources.getDimensionPixelSize(R.dimen.color_picker_button_size)
        val buttonLayoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize)
        buttonLayoutParams.setMargins(
            context.resources.getDimensionPixelSize(R.dimen.color_picker_button_margin))
        button.layoutParams = buttonLayoutParams
        button.insetTop = 0
        button.insetBottom = 0
        button.cornerRadius = context.resources.getDimensionPixelSize(R.dimen.corner_radius)

        button.backgroundTintList = ContextCompat.getColorStateList(context, color.colorId)

        button.setOnClickListener {
            setSelectedColorValues(color)
        }

        return button
    }

    private fun setSelectedColorValues(color: HabitColor) {
        selectedColor = color

        val colorValue = context.getColor(color.colorId)
        binding.selectedColor.backgroundTintList = ColorStateList.valueOf(colorValue)
        binding.rgbValue.text = colorValue.toRgb()
        binding.hsvValue.text = colorValue.toHsv()
        binding.hexValue.text = colorValue.toHex()
    }

    private fun setListenerOnDefaultColorButton(view: View) {
        val defaultColorButton: Button = view.findViewById(R.id.reset_color)
        defaultColorButton.setOnClickListener {
            setSelectedColorValues(HabitColor.defaultColor())
        }
    }

    private fun setListenerOnSaveButton(view: View) {
        val saveButton: Button = view.findViewById(R.id.save_color)
        saveButton.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                REQUEST_KEY,
                bundleOf(BUNDLE_KEY to selectedColor))
            dismiss()
        }
    }
}