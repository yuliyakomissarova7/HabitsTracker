package com.example.habitstracker.fragments

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.setMargins
import androidx.fragment.app.DialogFragment
import com.example.habitstracker.R
import com.example.habitstracker.databinding.ActivityMainBinding
import com.example.habitstracker.databinding.FragmentColorPickerBinding
import com.google.android.material.button.MaterialButton
import com.example.habitstracker.extensions.toHex
import com.example.habitstracker.extensions.toRgb
import com.example.habitstracker.extensions.toHsv

private const val ARG_DEFAULT_COLOR_ID = "default_color_id"

class ColorPickerFragment : DialogFragment() {

    private val gradientColorsIds: IntArray = intArrayOf(
        R.color.gradient_color1,
        R.color.gradient_color2,
        R.color.gradient_color3,
        R.color.gradient_color4,
        R.color.gradient_color5,
        R.color.gradient_color6,
        R.color.gradient_color7,
        R.color.gradient_color8,
        R.color.gradient_color9,
        R.color.gradient_color10,
        R.color.gradient_color11,
        R.color.gradient_color12,
        R.color.gradient_color13,
        R.color.gradient_color14,
        R.color.gradient_color15,
        R.color.gradient_color16,
    )

    private lateinit var binding: FragmentColorPickerBinding

    private var colorPalette: LinearLayout? = null

    private var selectedColorImage: ImageView? = null
    private var rgbValue: TextView? = null
    private var hsvValue: TextView? = null
    private var hexValue: TextView? = null

    private var selectedColorId: Int = R.color.default_color

    private lateinit var context: Context

    companion object {
        fun newInstance(defaultColorId: Int) =
            ColorPickerFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_DEFAULT_COLOR_ID, defaultColorId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentColorPickerBinding.inflate(layoutInflater)
        arguments?.let {
            selectedColorId = it.getInt(ARG_DEFAULT_COLOR_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_color_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context = activity as Context

        colorPalette = view.findViewById(R.id.color_palette)

        selectedColorImage = view.findViewById(R.id.selected_color)
        rgbValue = view.findViewById(R.id.rgb_value)
        hsvValue = view.findViewById(R.id.hsv_value)
        hexValue = view.findViewById(R.id.hex_value)

        setGradientBackground()
        createButtons()
        setSelectedColorValues(selectedColorId)
        setListenerOnDefaultColorButton(view)
        setListenerOnSaveButton(view)
    }

    private fun setGradientBackground() {
        val gradientBackground = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            gradientColorsIds
                .map { context.getColor(it) }
                .toIntArray()
        )
        colorPalette?.background = gradientBackground
    }

    private fun createButtons() {
        for (colorId in gradientColorsIds) {
            colorPalette?.addView(createButton(colorId))
        }
    }

    private fun createButton(colorId: Int): MaterialButton {
        val button = MaterialButton(ContextThemeWrapper(context, R.style.ColorPickerButton))
        val buttonSize = context.resources.getDimensionPixelSize(R.dimen.color_picker_button_size)
        val buttonLayoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize)
        buttonLayoutParams.setMargins(
            context.resources.getDimensionPixelSize(R.dimen.color_picker_button_margin))
        button.layoutParams = buttonLayoutParams
        button.insetTop = 0
        button.insetBottom = 0
        button.cornerRadius = context.resources.getDimensionPixelSize(R.dimen.corner_radius)

        button.backgroundTintList = ContextCompat.getColorStateList(context, colorId)

        button.setOnClickListener {
            setSelectedColorValues(colorId)
        }

        return button
    }

    private fun setSelectedColorValues(colorId: Int) {
        selectedColorId = colorId

        val colorValue = context.getColor(colorId)
        selectedColorImage?.backgroundTintList = ColorStateList.valueOf(colorValue)
        rgbValue?.text = colorValue.toRgb()
        hsvValue?.text = colorValue.toHsv()
        hexValue?.text = colorValue.toHex()
    }

    private fun setListenerOnDefaultColorButton(view: View) {
        val defaultColorButton: Button = view.findViewById(R.id.reset_color)
        defaultColorButton.setOnClickListener {
            setSelectedColorValues(R.color.default_color)
        }
    }

    private fun setListenerOnSaveButton(view: View) {
        val saveButton: Button = view.findViewById(R.id.save_color)
        saveButton.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                REQUEST_KEY,
                bundleOf(BUNDLE_KEY to selectedColorId))
            dismiss()
        }
    }
}