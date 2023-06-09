package com.example.habitstracker.entities

import com.example.habitstracker.R

enum class SortCriterion (val textId: Int, val isAscending: Boolean) {
    CREATION_DATE_DESCENDING (R.string.sort_by_creation_date_descending, false),
    CREATION_DATE_ASCENDING (R.string.sort_by_creation_date_ascending, true),
}