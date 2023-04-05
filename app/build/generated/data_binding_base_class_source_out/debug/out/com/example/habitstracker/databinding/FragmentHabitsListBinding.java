// Generated by view binder compiler. Do not edit!
package com.example.habitstracker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.habitstracker.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentHabitsListBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RecyclerView habitsList;

  @NonNull
  public final TextView noHabitsMessage;

  private FragmentHabitsListBinding(@NonNull ConstraintLayout rootView,
      @NonNull RecyclerView habitsList, @NonNull TextView noHabitsMessage) {
    this.rootView = rootView;
    this.habitsList = habitsList;
    this.noHabitsMessage = noHabitsMessage;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentHabitsListBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentHabitsListBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_habits_list, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentHabitsListBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.habits_list;
      RecyclerView habitsList = ViewBindings.findChildViewById(rootView, id);
      if (habitsList == null) {
        break missingId;
      }

      id = R.id.no_habits_message;
      TextView noHabitsMessage = ViewBindings.findChildViewById(rootView, id);
      if (noHabitsMessage == null) {
        break missingId;
      }

      return new FragmentHabitsListBinding((ConstraintLayout) rootView, habitsList,
          noHabitsMessage);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}