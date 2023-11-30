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
    EditText profileNameEditText;
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
        getImage.launch(i);
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                try {
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
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Lỗi khi chọn ảnh", Toast.LENGTH_SHORT).show();
                    Log.e("ProfileFragment", e.getMessage());
                }
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
            if (ApplicationActivity.user.getProfile() != null) {
                Picasso.get().load(ApplicationActivity.user.getProfile().getPhoto().get(0)).into(profileImage);
            }
            if (ApplicationActivity.user.getProfile().getName() != null) {
                profileName.setText(ApplicationActivity.user.getProfile().getName());
                profileNameEditText.setText(ApplicationActivity.user.getProfile().getName());
            }
            if (ApplicationActivity.user.getProfile().getDob() != null) {
                dayInput.setText(String.valueOf(ApplicationActivity.user.getProfile().getDob().getDate()));
                monthInput.setText(String.valueOf(ApplicationActivity.user.getProfile().getDob().getMonth()));
                yearInput.setText(String.valueOf(ApplicationActivity.user.getProfile().getDob().getYear()));
            }
            if (ApplicationActivity.user.getProfile().getGender() != null) {
                genderInput.setText(ApplicationActivity.user.getProfile().getGender());
            }
            if (ApplicationActivity.user.getProfile().getInterest() != null) {
                searchGenderInput.setText(ApplicationActivity.user.getProfile().getInterest());
            }
            if (ApplicationActivity.user.getProfile().getHobby() != null) {
                hobbies = ApplicationActivity.user.getProfile().getHobby();
                hobbies.forEach(this::addHobbyLayout);
            }
            if (ApplicationActivity.user.getProfile().getDescription() != null) {
                bioInput.setText(ApplicationActivity.user.getProfile().getDescription());
            }
            ageRangeSlider.setValues((float) ApplicationActivity.user.getProfile().getMinAge(), (float) ApplicationActivity.user.getProfile().getMaxAge());
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
        Log.e("ProfileFragment", ApplicationActivity.user.getProfile().getName());
        profileImage = getActivity().findViewById(R.id.fragment_profile_avt);
        profileImage.setOnClickListener(this::onChangeProfileImage);
        // for profile name
        profileNameEditText = getActivity().findViewById(R.id.fragment_profile_name_edit_text);
        profileName = getActivity().findViewById(R.id.fragment_profile_name);
        // for profile birthday
        dayInput = getActivity().findViewById(R.id.fragment_profile_day_input);
        monthInput = getActivity().findViewById(R.id.fragment_profile_month_input);
        yearInput = getActivity().findViewById(R.id.fragment_profile_year_input);
        dayInput.setOnClickListener(this::onPickDate);
        monthInput.setOnClickListener(this::onPickDate);
        yearInput.setOnClickListener(this::onPickDate);
        // for gender
        genderInput = getActivity().findViewById(R.id.fragment_profile_autoCompleteGender);
        searchGenderInput = getActivity().findViewById(R.id.fragment_profile_autoCompleteSearch);
        createGenderSelection();
        createSearchSelection();
        // for hobby
        hobbies = new ArrayList<>();
        hobbyInput = getActivity().findViewById(R.id.fragment_profile_hobby_input);
        addHobbyButton = getActivity().findViewById(R.id.fragment_profile_add_hobby);
        addHobbyButton.setOnClickListener(v -> addHobby());
        // for bio
        bioInput = getActivity().findViewById(R.id.fragment_profile_description);
        // for age range
        ageRangeSlider = getActivity().findViewById(R.id.fragment_profile_sliderRange);
        // get info from user
        getInfoFromUser();
        // for button
        getActivity().findViewById(R.id.fragment_profile_save_button).setOnClickListener(v -> {
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
        getActivity().findViewById(R.id.fragment_profile_reset_button).setOnClickListener(v -> {
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