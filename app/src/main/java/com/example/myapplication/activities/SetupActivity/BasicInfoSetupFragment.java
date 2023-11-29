package com.example.myapplication.activities.SetupActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.schema.Profile;

import java.util.Date;
import java.util.Calendar;

import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BasicInfoSetupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicInfoSetupFragment extends Fragment {
  private Profile profile = new Profile();

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;
//  data

  public String getName() {
    return ((TextView) requireActivity().findViewById(R.id.basic_info_setup_name_input)).getText().toString();
  }

  public Date getDob() {
    String day = ((TextView) getActivity().findViewById(R.id.basic_info_setup_day_input)).getText().toString();
    String month = ((TextView) getActivity().findViewById(R.id.basic_info_setup_month_input)).getText().toString();
    String year = ((TextView) getActivity().findViewById(R.id.basic_info_setup_year_input)).getText().toString();
    return new Date(Integer.parseInt(year) - 1900, Integer.parseInt(month) + 1, Integer.parseInt(day));
  }

  public String getGender() {
    return ((TextView) requireActivity().findViewById(R.id.autoCompleteGender)).getText().toString();
  }

  public String getSearch() {
    return ((TextView) requireActivity().findViewById(R.id.autoCompleteSearch)).getText().toString();
  }

  public boolean hasEmptyField() {
    return getName().isEmpty() || getDob() == null || getGender().isEmpty() || getSearch().isEmpty();
  }

  public BasicInfoSetupFragment() {
    // Required empty public constructor
  }
  public static BasicInfoSetupFragment newInstance(String param1, String param2) {
    BasicInfoSetupFragment fragment = new BasicInfoSetupFragment();
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

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    getActivity().findViewById(R.id.basic_info_setup_day_input).setOnClickListener(this::onPickDate);
    getActivity().findViewById(R.id.basic_info_setup_month_input).setOnClickListener(this::onPickDate);
    getActivity().findViewById(R.id.basic_info_setup_year_input).setOnClickListener(this::onPickDate);
  }

  public void onPickDate(View view) {
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year1, month1, dayOfMonth) -> {
      getActivity().findViewById(R.id.basic_info_setup_day_input).setTag(dayOfMonth);
      getActivity().findViewById(R.id.basic_info_setup_month_input).setTag(month1);
      getActivity().findViewById(R.id.basic_info_setup_year_input).setTag(year1);
      ((android.widget.EditText) getActivity().findViewById(R.id.basic_info_setup_day_input)).setText(String.valueOf(dayOfMonth));
      ((android.widget.EditText) getActivity().findViewById(R.id.basic_info_setup_month_input)).setText(String.valueOf(month1));
      ((android.widget.EditText) getActivity().findViewById(R.id.basic_info_setup_year_input)).setText(String.valueOf(year1));
    }, year, month, day);
    datePickerDialog.show();
  }

  private void createGenderSelection() {
    String[] genders = {"Nam", "Nữ", "Khác"};
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, genders);
    AutoCompleteTextView autoCompleteTextView = requireActivity().findViewById(R.id.autoCompleteGender);
    if (autoCompleteTextView != null) {
      autoCompleteTextView.setAdapter(arrayAdapter);
      autoCompleteTextView.setOnItemClickListener((adapterView, v, i, l) -> {});
    }
  }

  private void createSearchSelection() {
    String[] searchSelections = {"Nam", "Nữ", "Khác"};
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, searchSelections);
    AutoCompleteTextView autoCompleteTextView = requireActivity().findViewById(R.id.autoCompleteSearch);
    if (autoCompleteTextView != null) {
      autoCompleteTextView.setAdapter(arrayAdapter);
      autoCompleteTextView.setOnItemClickListener((adapterView, v, i, l) -> {});
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    createGenderSelection();
    createSearchSelection();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    createGenderSelection();
    createSearchSelection();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    return inflater.inflate(R.layout.fragment_basic_info_setup, container, false);
  }
}