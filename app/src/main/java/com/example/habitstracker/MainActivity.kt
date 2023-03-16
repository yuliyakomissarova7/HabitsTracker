package com.example.habitstracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), HabitAdapter.OnHabitCardListener {

    private var noHabitsMessage: TextView? = null

    companion object {
        var habits: MutableList<Habit> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noHabitsMessage = findViewById(R.id.no_habits_message)

        setListenerOnAddHabitButton()
    }

    private fun setListenerOnAddHabitButton() {
        val addHabitButton: FloatingActionButton = findViewById(R.id.add_habit_button)
        addHabitButton.setOnClickListener {
            val intent = Intent(this, HabitEditingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        noHabitsMessage?.visibility = if (habits.size == 0) View.VISIBLE else View.GONE

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.habits_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = HabitAdapter(habits, applicationContext, this)
    }

    override fun onHabitCardClick(position: Int) {
        val intent = Intent(this, HabitEditingActivity::class.java)
            .apply {
                putExtra(getString(R.string.intent_extra_habit_position), position)
                putExtra(getString(R.string.intent_extra_habit), habits[position]) }
        startActivity(intent)
    }
}