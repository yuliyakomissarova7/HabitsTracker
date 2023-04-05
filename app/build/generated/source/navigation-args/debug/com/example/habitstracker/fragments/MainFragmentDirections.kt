package com.example.habitstracker.fragments

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.habitstracker.Habit
import com.example.habitstracker.R
import java.io.Serializable
import kotlin.Int
import kotlin.Suppress

public class MainFragmentDirections private constructor() {
  private data class ActionMainFragmentToEditHabitFragment(
    public val habitPosition: Int = -1,
    public val habit: Habit? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_mainFragment_to_editHabitFragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
        val result = Bundle()
        result.putInt("habitPosition", this.habitPosition)
        if (Parcelable::class.java.isAssignableFrom(Habit::class.java)) {
          result.putParcelable("habit", this.habit as Parcelable?)
        } else if (Serializable::class.java.isAssignableFrom(Habit::class.java)) {
          result.putSerializable("habit", this.habit as Serializable?)
        }
        return result
      }
  }

  public companion object {
    public fun actionMainFragmentToEditHabitFragment(habitPosition: Int = -1, habit: Habit? = null):
        NavDirections = ActionMainFragmentToEditHabitFragment(habitPosition, habit)

    public fun actionMainFragmentToAboutAppFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_mainFragment_to_aboutAppFragment)
  }
}
