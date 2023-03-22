package com.example.habitstracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), HabitAdapter.OnHabitCardListener {

    private var noHabitsMessage: TextView? = null

    private lateinit var binding: ActivityMainBinding

    companion object {
        var habits: MutableList<Habit> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noHabitsMessage = binding.noHabitsMessage

        setListenerOnAddHabitButton()
    }

    private fun setListenerOnAddHabitButton() {
        val addHabitButton: FloatingActionButton = binding.addHabitButton
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
        val recyclerView: RecyclerView = binding.habitsList
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = HabitAdapter(habits, applicationContext, this)
    }

    override fun onHabitCardClick(position: Int) {
        val intent = Intent(this, HabitEditingActivity::class.java)
            .apply {
                putExtra("position", position)
                putExtra("habit", habits[position]) }
        startActivity(intent)
    }
}