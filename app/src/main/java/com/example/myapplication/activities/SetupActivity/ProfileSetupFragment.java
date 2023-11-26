package com.example.myapplication.activities.SetupActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProfileSetupFragment extends Fragment {

  TextView add;
  LinearLayout layout;
  EditText nameEdit;
  ImageView photoButton;
  Uri selectedImageUri;
  List<String> hobbies = new ArrayList<>();

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

  public Uri getSelectedImageUri() {
    return selectedImageUri;
  }

  public void setSelectedImageUri(Uri selectedImageUri) {
    this.selectedImageUri = selectedImageUri;
  }

  public void addHobby() {
    if (hobbies.size() == 3) {
      Toast.makeText(getActivity(), "Bạn chỉ được thêm tối đa 3 sở thích", Toast.LENGTH_SHORT).show();
      return;
    }
    if (nameEdit.getText().toString().length() > 0) {
      hobbies.add(nameEdit.getText().toString());
      addHobbyLayout(nameEdit.getText().toString());
      nameEdit.setText("");
    }
  }


  public LinearLayout getLayout() {
    return layout;
  }

  public void addHobbyLayout(String name) {
    View view = getLayoutInflater().inflate(R.layout.item__hobby, null);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    params.setMargins(0, 0, 10, 0);
    view.setLayoutParams(params);
    LinearLayout list = getActivity().findViewById(R.id.setup_profile_hobby_list);
    ImageView delete = view.findViewById(R.id.hobby_item_close);
    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView hobby = view.findViewById(R.id.hobby_item_text);
    hobby.setText(name);
    view.setOnClickListener(v -> {
      list.removeView(view);
      hobbies.remove(name);
    });
    list.addView(view);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    add = getActivity().findViewById(R.id.setup_profile_add_hobby);
    layout = getActivity().findViewById(R.id.setup_profile_hobby_list);
    nameEdit = getActivity().findViewById(R.id.setup_profile_hobby);
    photoButton = getActivity().findViewById(R.id.setup_profile_img);
    hobbies.forEach(this::addHobbyLayout);
    if (selectedImageUri != null) {
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      Resources r = getResources();
      float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 99.5F, r.getDisplayMetrics());
      float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 99.5F, r.getDisplayMetrics());
      params.width = (int) width;
      params.height = (int) height;
      photoButton.setLayoutParams(params);
      photoButton.setImageURI(selectedImageUri);
    }
    photoButton.setOnClickListener(this::selectImage);
    add.setOnClickListener(v -> addHobby());
  }

  public void selectImage(View view){
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
            Uri selectedImageUri = data.getData();
            setSelectedImageUri(selectedImageUri);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Resources r = getResources();
            float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 99.5F, r.getDisplayMetrics());

            float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 99.5F, r.getDisplayMetrics());
            params.width = (int) width;
            params.height = (int) height;
            photoButton.setLayoutParams(params);
            photoButton.setImageURI(selectedImageUri);
          });
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    return inflater.inflate(R.layout.fragment_setup_profile, container, false);
  }
}