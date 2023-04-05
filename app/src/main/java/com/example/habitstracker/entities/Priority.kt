package com.example.habitstracker.entities

import com.example.habitstracker.R

enum class Priority(val textId: Int){
    HIGH(R.string.high_priority),
    MEDIUM(R.string.medium_priority),
    LOW(R.string.low_priority);
}