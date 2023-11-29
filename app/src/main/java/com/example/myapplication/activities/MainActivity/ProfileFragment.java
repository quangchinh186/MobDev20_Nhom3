package com.example.myapplication.activities.MainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.schema.Profile;
import com.google.android.material.slider.RangeSlider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {
    ImageView profileImage;
    Uri profileImageUri;
    TextView profileName;
    EditText dayInput, monthInput, yearInput;
    AutoCompleteTextView genderInput, searchGenderInput;
    EditText hobbyInput;
    TextView addHobbyButton;
    List<String> hobbies;
    EditText bioInput;
    RangeSlider ageRangeSlider;
    Profile profile;

    public void onChangeProfileImage(View view) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != Activity.RESULT_OK) {
                    return;
                }
                Intent data = result.getData();
                // do your operation from here....
                if (data == null || data.getData() == null) {
                    return;
                }
                profileImageUri = data.getData();
                profileImage.setImageURI(profileImageUri);
            });

    public void onPickDate(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year1, month1, dayOfMonth) -> {
            getActivity().findViewById(R.id.fragment_profile_day_input).setTag(dayOfMonth);
            getActivity().findViewById(R.id.fragment_profile_month_input).setTag(month1);
            getActivity().findViewById(R.id.fragment_profile_year_input).setTag(year1);
            ((android.widget.EditText) getActivity().findViewById(R.id.fragment_profile_day_input)).setText(String.valueOf(dayOfMonth));
            ((android.widget.EditText) getActivity().findViewById(R.id.fragment_profile_month_input)).setText(String.valueOf(month1));
            ((android.widget.EditText) getActivity().findViewById(R.id.fragment_profile_year_input)).setText(String.valueOf(year1));
        }, year, month, day);
        datePickerDialog.show();
    }

    private void createGenderSelection() {
        String[] genders = {"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, genders);
        AutoCompleteTextView autoCompleteTextView = getActivity().findViewById(R.id.fragment_profile_autoCompleteGender);
        if (autoCompleteTextView != null) {
            autoCompleteTextView.setAdapter(arrayAdapter);
            autoCompleteTextView.setOnItemClickListener((adapterView, v, i, l) -> {});
        }
    }

    private void createSearchSelection() {
        String[] searchSelections = {"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, searchSelections);
        AutoCompleteTextView autoCompleteTextView = getActivity().findViewById(R.id.fragment_profile_autoCompleteSearch);
        if (autoCompleteTextView != null) {
            autoCompleteTextView.setAdapter(arrayAdapter);
            autoCompleteTextView.setOnItemClickListener((adapterView, v, i, l) -> {});
        }
    }

    public void addHobby() {
        if (hobbies.size() == 3) {
            Toast.makeText(getActivity(), "Bạn chỉ được thêm tối đa 3 sở thích", Toast.LENGTH_SHORT).show();
            return;
        }
        if (hobbyInput.getText().toString().length() > 0) {
            hobbies.add(hobbyInput.getText().toString());
            addHobbyLayout(hobbyInput.getText().toString());
            hobbyInput.setText("");
        }
    }

    public void addHobbyLayout(String name) {
        View view = getLayoutInflater().inflate(R.layout.item__hobby, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 10, 0);
        view.setLayoutParams(params);
        LinearLayout list = getActivity().findViewById(R.id.fragment_profile_hobby_list);
        ImageView delete = view.findViewById(R.id.hobby_item_close);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView hobby = view.findViewById(R.id.hobby_item_text);
        hobby.setText(name);
        view.setOnClickListener(v -> {
            list.removeView(view);
            hobbies.remove(name);
        });
        list.addView(view);
    }

    public Date getDob() {
        String day = ((TextView) getActivity().findViewById(R.id.fragment_profile_day_input)).getText().toString();
        String month = ((TextView) getActivity().findViewById(R.id.fragment_profile_month_input)).getText().toString();
        String year = ((TextView) getActivity().findViewById(R.id.fragment_profile_year_input)).getText().toString();
        return new Date(Integer.parseInt(year) - 1900, Integer.parseInt(month) + 1, Integer.parseInt(day));
    }

    public void getInfoFromUser() {
        try {
            Log.e("ProfileFragment", ApplicationActivity.user.getId().toString());
            profileName.setText(profile.getName());
            dayInput.setText(profile.getDob().getDay());
            monthInput.setText(profile.getDob().getMonth());
            yearInput.setText(profile.getDob().getYear());
            genderInput.setText(profile.getGender());
            searchGenderInput.setText(profile.getInterest());
            bioInput.setText(profile.getDescription());
            ageRangeSlider.setValues((float) profile.getMinAge(), (float) profile.getMaxAge());
            hobbies = profile.getHobby();
            for (String hobby : hobbies) {
                addHobbyLayout(hobby);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Lỗi khi lấy thông tin từ server", Toast.LENGTH_SHORT).show();
            Log.e("ProfileFragment", e.getMessage());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  for profile image
        profile = ApplicationActivity.user.getProfile();
        profileImage = getView().findViewById(R.id.fragment_profile_avt);
        Picasso.get().load(ApplicationActivity.user.getProfile().toString()).into(profileImage);
        profileImage.setOnClickListener(this::onChangeProfileImage);
        // for profile name
        profileName = getView().findViewById(R.id.fragment_profile_name);
        // for profile birthday
        dayInput = getView().findViewById(R.id.fragment_profile_day_input);
        monthInput = getView().findViewById(R.id.fragment_profile_month_input);
        yearInput = getView().findViewById(R.id.fragment_profile_year_input);
        dayInput.setOnClickListener(this::onPickDate);
        monthInput.setOnClickListener(this::onPickDate);
        yearInput.setOnClickListener(this::onPickDate);
        // for gender
        createGenderSelection();
        createSearchSelection();
        // for hobby
        hobbies = new ArrayList<>();
        hobbyInput = getView().findViewById(R.id.fragment_profile_hobby_input);
        addHobbyButton = getView().findViewById(R.id.fragment_profile_add_hobby);
        addHobbyButton.setOnClickListener(v -> addHobby());
        // for bio
        bioInput = getView().findViewById(R.id.fragment_profile_description);
        // for age range
        ageRangeSlider = getView().findViewById(R.id.fragment_profile_sliderRange);
        // get info from user
        getInfoFromUser();
        // for button
        getView().findViewById(R.id.fragment_profile_save_button).setOnClickListener(v -> {
            Profile profile = ApplicationActivity.user.getProfile();
            profile.setName(profileName.getText().toString());
            profile.setDob(getDob());
            profile.setGender(genderInput.getText().toString());
            profile.setInterest(searchGenderInput.getText().toString());
            profile.setHobby((RealmList<String>) hobbies);
            profile.setDescription(bioInput.getText().toString());
            profile.setMinAge(ageRangeSlider.getValues().get(0).longValue());
            profile.setMaxAge((int) ageRangeSlider.getValues().get(1).longValue());
            ApplicationActivity.user.setProfile(profile);
            Toast.makeText(getActivity(), "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
        });
        getView().findViewById(R.id.fragment_profile_reset_button).setOnClickListener(v -> {
            getInfoFromUser();
            Toast.makeText(getActivity(), "Đã reset thông tin", Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}