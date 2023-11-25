package com.example.myapplication.activities.SetupActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;

public class ProfileSetupFragment extends Fragment {

  Button add;
  LinearLayout layout;
  EditText nameEdit;

  public ProfileSetupFragment() {
  }
  public static ProfileSetupFragment newInstance(String param1, String param2) {
    ProfileSetupFragment fragment = new ProfileSetupFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    add = getView().findViewById(R.id.setup_profile_add_hobby);
    layout = getView().findViewById(R.id.hobby_container);
    nameEdit = getView().findViewById(R.id.setup_profile_hobby);
  }

  public void addName(View view) {
    String name = nameEdit.getText().toString();
    addCard(name);
  }


  private void addCard(String name) {
    final View view = getLayoutInflater().inflate(R.layout.card, null);

    TextView nameView = getView().findViewById(R.id.name);
    Button delete = getView().findViewById(R.id.delete);

    nameView.setText(name);

    delete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        layout.removeView(view);
      }
    });

    layout.addView(view);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_setup_profile, container, false);
  }
}