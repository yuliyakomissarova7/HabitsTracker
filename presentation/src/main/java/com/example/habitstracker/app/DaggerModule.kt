package com.example.habitstracker.app

import android.content.Context
import com.example.data.db.HabitDatabase
import com.example.data.db.HabitService
import com.example.data.repository.HabitRepositoryImpl
import com.example.data.serializers.HabitDoneBodySerializer
import com.example.data.serializers.HabitJsonDeserializer
import com.example.data.serializers.HabitJsonSerializer
import com.example.domain.HabitRepository
import com.example.domain.entities.Habit
import com.example.domain.entities.DoneHabitBody
import com.example.domain.usecases.HabitEditingUseCase
import com.example.domain.usecases.SearchAndFilterHabitUseCase
import com.example.habitstracker.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DaggerModule(private val context: Context) {
    @Provides
    fun provideContext(): Context = context

    @Singleton
    @Provides
    fun provideHabitEditingUseCase(habitRepository: HabitRepository): HabitEditingUseCase =
        HabitEditingUseCase(habitRepository)

    @Singleton
    @Provides
    fun provideSearchAndFilterHabitsUseCase(habitRepository: HabitRepository): SearchAndFilterHabitUseCase =
        SearchAndFilterHabitUseCase(habitRepository)

    @Singleton
    @Provides
    fun provideDatabase(): HabitDatabase =
        HabitDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideHabitRepository(habitDatabase: HabitDatabase, habitService: HabitService): HabitRepository =
        HabitRepositoryImpl(habitDatabase.habitDao(), habitService)

    @Singleton
    @Provides
    fun provideHabitService(retrofit: Retrofit): HabitService =
        retrofit.create(HabitService::class.java)

    @Singleton
    @Provides
    fun provideGson(): Gson =
        GsonBuilder()
            .registerTypeAdapter(Habit::class.java, HabitJsonSerializer())
            .registerTypeAdapter(Habit::class.java, HabitJsonDeserializer())
            .registerTypeAdapter(DoneHabitBody::class.java, HabitDoneBodySerializer())
            .create()

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor { chain ->
                val request: Request =
                    chain.request().newBuilder().addHeader(
                        "Authorization",
                        BuildConfig.TOKEN
                    ).build()
                val response = chain.proceed(request)
                when (response.code()) {
                    200 -> response
                    else -> {
                        Thread.sleep(1000)
                        response.close()
                        chain.call().clone().execute()
                    }
                }
            }
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://droid-test-server.doubletapp.ru/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
}