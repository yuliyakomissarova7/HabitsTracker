package com.example.habitstracker.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.habitstracker.R
import com.example.habitstracker.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(){

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navigationView = binding.navView
        navigationView.setupWithNavController(navController)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.editHabitFragment) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }
        }

        setupAvatar()
    }

    private fun setupAvatar() {
        val header: View = navigationView.getHeaderView(0)
        val imageView: ImageView = header.findViewById(R.id.avatar)

        Glide.with(this)
            .load("https://klike.net/uploads/posts/2019-11/1572781367_19.jpg")
            .transform(CircleCrop())
            .placeholder(R.drawable.avatar)
            .error(R.drawable.avatar)
            .into(imageView)
    }

    override fun onSupportNavigateUp() = navController.navigateUp(appBarConfiguration)

}