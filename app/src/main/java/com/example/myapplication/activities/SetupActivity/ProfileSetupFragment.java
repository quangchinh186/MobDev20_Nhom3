package com.example.myapplication.activities.SetupActivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.Objects;

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

  }

  public void addHobby() {
    if (nameEdit.getText().toString().length() > 0) {
      addCard(nameEdit.getText().toString());
      nameEdit.setText("");
    }
  }


  public LinearLayout getLayout() {
    return layout;
  }

  public void addCard(String name) {
    final View view = getLayoutInflater().inflate(R.layout.name_tag, null);

    TextView nameView = view.findViewById(R.id.name);
    Button delete =  view.findViewById(R.id.delete);

    nameView.setText(name);

    delete.setOnClickListener(v -> layout.removeView(view));

    layout.addView(view);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    add = getActivity().findViewById(R.id.setup_profile_add_hobby);
    layout = getActivity().findViewById(R.id.setup_profile_hobby_list);
    nameEdit = getActivity().findViewById(R.id.setup_profile_hobby);
    add.setOnClickListener(v -> addHobby());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    return inflater.inflate(R.layout.fragment_setup_profile, container, false);
  }
}