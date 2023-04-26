package com.example.habitstracker.entities

import com.example.habitstracker.R

enum class Priority(val textId: Int, val value: Int){
    HIGH(R.string.high_priority, 2),
    MEDIUM(R.string.medium_priority, 1),
    LOW(R.string.low_priority, 0);
}