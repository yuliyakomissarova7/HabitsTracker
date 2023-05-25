package com.example.habitstracker.app

import android.app.Application

class HabitTrackerApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .daggerModule(DaggerModule(this))
            .build()
    }
}