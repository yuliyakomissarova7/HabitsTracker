package com.example.habitstracker.entities

import com.example.habitstracker.R

enum class HabitColor(val colorId: Int) {
    GRADIENT_COLOR1(R.color.gradient_color1),
    GRADIENT_COLOR2(R.color.gradient_color2),
    GRADIENT_COLOR3(R.color.gradient_color3),
    GRADIENT_COLOR4(R.color.gradient_color4),
    GRADIENT_COLOR5(R.color.gradient_color5),
    GRADIENT_COLOR6(R.color.gradient_color6),
    GRADIENT_COLOR7(R.color.gradient_color7),
    GRADIENT_COLOR8(R.color.gradient_color8),
    GRADIENT_COLOR9(R.color.gradient_color9),
    GRADIENT_COLOR10(R.color.gradient_color10),
    GRADIENT_COLOR11(R.color.gradient_color11),
    GRADIENT_COLOR12(R.color.gradient_color12),
    GRADIENT_COLOR13(R.color.gradient_color13),
    GRADIENT_COLOR14(R.color.gradient_color14),
    GRADIENT_COLOR15(R.color.gradient_color15),
    GRADIENT_COLOR16(R.color.gradient_color16);

    companion object {
        fun defaultColor(): HabitColor = GRADIENT_COLOR1
    }
}