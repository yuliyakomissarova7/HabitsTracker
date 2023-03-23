package com.example.habitstracker.fragments

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import com.example.habitstracker.Habit
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import kotlin.Int
import kotlin.Suppress
import kotlin.jvm.JvmStatic

public data class HabitEditingFragmentArgs(
  public val habitPosition: Int = -1,
  public val habit: Habit? = null,
) : NavArgs {
  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putInt("habitPosition", this.habitPosition)
    if (Parcelable::class.java.isAssignableFrom(Habit::class.java)) {
      result.putParcelable("habit", this.habit as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(Habit::class.java)) {
      result.putSerializable("habit", this.habit as Serializable?)
    }
    return result
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("habitPosition", this.habitPosition)
    if (Parcelable::class.java.isAssignableFrom(Habit::class.java)) {
      result.set("habit", this.habit as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(Habit::class.java)) {
      result.set("habit", this.habit as Serializable?)
    }
    return result
  }

  public companion object {
    @JvmStatic
    @Suppress("DEPRECATION")
    public fun fromBundle(bundle: Bundle): HabitEditingFragmentArgs {
      bundle.setClassLoader(HabitEditingFragmentArgs::class.java.classLoader)
      val __habitPosition : Int
      if (bundle.containsKey("habitPosition")) {
        __habitPosition = bundle.getInt("habitPosition")
      } else {
        __habitPosition = -1
      }
      val __habit : Habit?
      if (bundle.containsKey("habit")) {
        if (Parcelable::class.java.isAssignableFrom(Habit::class.java) ||
            Serializable::class.java.isAssignableFrom(Habit::class.java)) {
          __habit = bundle.get("habit") as Habit?
        } else {
          throw UnsupportedOperationException(Habit::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
      } else {
        __habit = null
      }
      return HabitEditingFragmentArgs(__habitPosition, __habit)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): HabitEditingFragmentArgs {
      val __habitPosition : Int?
      if (savedStateHandle.contains("habitPosition")) {
        __habitPosition = savedStateHandle["habitPosition"]
        if (__habitPosition == null) {
          throw IllegalArgumentException("Argument \"habitPosition\" of type integer does not support null values")
        }
      } else {
        __habitPosition = -1
      }
      val __habit : Habit?
      if (savedStateHandle.contains("habit")) {
        if (Parcelable::class.java.isAssignableFrom(Habit::class.java) ||
            Serializable::class.java.isAssignableFrom(Habit::class.java)) {
          __habit = savedStateHandle.get<Habit?>("habit")
        } else {
          throw UnsupportedOperationException(Habit::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
      } else {
        __habit = null
      }
      return HabitEditingFragmentArgs(__habitPosition, __habit)
    }
  }
}
