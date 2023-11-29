package com.example.myapplication.activities.SetupActivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.EditText;

import com.example.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileDescriptionSetup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDescriptionSetup extends Fragment {

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private EditText description;

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public ProfileDescriptionSetup() {
    // Required empty public constructor
  }

  public static ProfileDescriptionSetup newInstance(String param1, String param2) {
    ProfileDescriptionSetup fragment = new ProfileDescriptionSetup();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

//  public String getDescription() {
//    return ((TextView) requireActivity().findViewById(R.id.setup_profile_description)).getText().toString();
//  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    description = getActivity().findViewById(R.id.setup_profile_description);
  }

  public boolean hasEmptyField() {
    return getDescription().isEmpty();
  }

  public String getDescription() {
    return description.getText().toString();
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_profile_description_setup, container, false);
  }
}