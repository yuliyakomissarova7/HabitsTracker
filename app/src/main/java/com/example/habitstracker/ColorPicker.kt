package com.example.habitstracker

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import com.google.android.material.button.MaterialButton
import com.example.habitstracker.extensions.toHex
import com.example.habitstracker.extensions.toRgb
import com.example.habitstracker.extensions.toHsv

class ColorPicker(private val context: Context, private val defaultColorId: Int)
    : Dialog(context) {

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

    private var colorPalette: LinearLayout? = null

    private var selectedColorImage: ImageView? = null
    private var rgbValue: TextView? = null
    private var hsvValue: TextView? = null
    private var hexValue: TextView? = null

    var selectedColorId: Int = defaultColorId

    var onSaveColorListener: OnSaveColorListener? = null

    interface OnSaveColorListener {
        fun setSelectedColor(colorId: Int)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onSaveColorListener = context as OnSaveColorListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.color_picker)

        colorPalette = findViewById(R.id.color_palette)

        selectedColorImage = findViewById(R.id.selected_color)
        rgbValue = findViewById(R.id.rgb_value)
        hsvValue = findViewById(R.id.hsv_value)
        hexValue = findViewById(R.id.hex_value)

        setGradientBackground()
        createButtons()
        setSelectedColorValues(defaultColorId)
        setListenerOnDefaultColorButton()
        setListenerOnSaveButton()
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

    private fun setListenerOnDefaultColorButton() {
        val defaultColorButton: Button = findViewById(R.id.reset_color)
        defaultColorButton.setOnClickListener {
            setSelectedColorValues(R.color.default_color)
        }
    }

    private fun setListenerOnSaveButton() {
        val saveButton: Button = findViewById(R.id.save_color)
        saveButton.setOnClickListener {
            onSaveColorListener?.setSelectedColor(selectedColorId)
            dismiss()
        }
    }
}