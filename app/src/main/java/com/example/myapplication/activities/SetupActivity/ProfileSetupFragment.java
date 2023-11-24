package com.example.myapplication.activities.SetupActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ProfileSetupFragment extends Fragment {
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  EditText hobbyEditText = (EditText) requireActivity().findViewById(R.id.setup_profile_hobby);

  public ProfileSetupFragment() {
    hobbyEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Do nothing
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Do nothing
      }

      @Override
      public void afterTextChanged(Editable s) {
        // Do nothing
      }
    });
  }
  public static ProfileSetupFragment newInstance(String param1, String param2) {
    ProfileSetupFragment fragment = new ProfileSetupFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  public ArrayList getInterest() {
    ((TextView) (requireActivity().findViewById(R.id.setup_profile_hobby))).getText().toString();
    return new ArrayList();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_setup_profile, container, false);
  }


}