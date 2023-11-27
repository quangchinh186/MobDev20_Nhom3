package com.example.myapplication.activities.SetupActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.myapplication.R;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.schema.Profile;
import com.example.myapplication.system.BatoSystem;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.realm.RealmList;

public class SetupActivity extends AppCompatActivity {
  int currentFragment = 0;
  int numFragments = 4;
  Profile profile = new Profile();
  Uri uri;

  @SuppressLint("ResourceAsColor")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setup);
    transactionFragment(R.id.fragment_container, BasicInfoSetupFragment.class);
    setColorForProgress(findViewById(R.id.setup_frag_1), getResources().getColor(R.color.white), Typeface.BOLD, true);

    currentFragment = 1;
  }

  private void getData() {
    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    if (fragment instanceof BasicInfoSetupFragment) {
      profile.setName(((BasicInfoSetupFragment) fragment).getName());
      profile.setDob(((BasicInfoSetupFragment) fragment).getDob());
      profile.setGender(((BasicInfoSetupFragment) fragment).getGender());
      profile.setInterest(((BasicInfoSetupFragment) fragment).getSearch());
    } else if (fragment instanceof ProfileSetupFragment) {
      List<String> hobbies = ((ProfileSetupFragment) fragment).getHobbies();
      RealmList<String> hobbiesRealm = new RealmList<>();
      hobbiesRealm.addAll(hobbies);
      profile.setHobby(hobbiesRealm);
      uri = ((ProfileSetupFragment) fragment).getSelectedImageUri();
    } else if (fragment instanceof ProfileDescriptionSetup) {
      profile.setDescription(((ProfileDescriptionSetup) fragment).getDescription());
    } else if (fragment instanceof FinalSetup) {
      findViewById(R.id.setup_loading_scene).setVisibility(View.VISIBLE);
      MediaManager.get().upload(uri).callback(new UploadCallback() {
        @Override
        public void onStart(String requestId) {
          //start upload request
        }

        @Override
        public void onProgress(String requestId, long bytes, long totalBytes) {
          //request in progress
        }

        @Override
        public void onSuccess(String requestId, Map resultData) {
          //success upload the image
          //result
          Log.v("result data", resultData.toString());
          //image url
          String imageUrl = "https://res.cloudinary.com/dihtkakro/image/upload/f_auto,q_auto/" + Objects.requireNonNull(resultData.get("public_id")).toString();
          RealmList<String> images = new RealmList<>();
          images.add(imageUrl);
          profile.setPhoto(images);
          ApplicationActivity.queryHelper.createUserWithId(profile);
//          finish activity
          Toast.makeText(getApplicationContext(), "Đã hoàn thành", Toast.LENGTH_SHORT).show();
          BatoSystem.writeString("recentEmail", BatoSystem.readString("email", ""));
          BatoSystem.writeBoolean("login", true);
          findViewById(R.id.setup_loading_scene).setVisibility(View.INVISIBLE);
          finish();
        }

        @Override
        public void onError(String requestId, ErrorInfo error) {
          //handle error
        }

        @Override
        public void onReschedule(String requestId, ErrorInfo error) {

        }
      }).dispatch();
    }
  }

  private void setColorForProgress(LinearLayout progressLayout, int color, int style, boolean scrollTo) {
    TextView textView = (TextView) progressLayout.getChildAt(1);
    textView.setTextColor(color);
    textView.setTypeface(null, style);
    HorizontalScrollView scrollView = findViewById(R.id.progressSection);
    if (scrollTo) {
      scrollView.smoothScrollTo(progressLayout.getLeft(), 0);
    }
  }

  private void transactionFragment(int id, Class fragmentClass) {
    getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.frag_right_to_left_in, R.anim.frag_right_to_left_out)
            .replace(id, fragmentClass, null)
            .addToBackStack(fragmentClass.getName())
            .commit();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    if (currentFragment > 0) currentFragment -= 1;
    if (currentFragment == 1) {
      setColorForProgress(findViewById(R.id.setup_frag_1), getResources().getColor(R.color.white), Typeface.BOLD, true);
      setColorForProgress(findViewById(R.id.setup_frag_2), Color.parseColor("#cccccc"), Typeface.NORMAL, false);
    } else if (currentFragment == 2) {
      setColorForProgress(findViewById(R.id.setup_frag_2), getResources().getColor(R.color.white), Typeface.BOLD, true);
      setColorForProgress(findViewById(R.id.setup_frag_3), Color.parseColor("#cccccc"), Typeface.NORMAL, false);
    } else if (currentFragment == 3) {
      setColorForProgress(findViewById(R.id.setup_frag_3), getResources().getColor(R.color.white), Typeface.BOLD, true);
      setColorForProgress(findViewById(R.id.setup_frag_4), Color.parseColor("#cccccc"), Typeface.NORMAL, false);
    }
  }

  public void onNextFragment(View view) {
    if (currentFragment == 1 && ((BasicInfoSetupFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).hasEmptyField()) {
      Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
      return;
    } else if (currentFragment == 2 && ((ProfileSetupFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).getSelectedImageUri() == null) {
      Toast.makeText(this, "Bạn cần phải thêm hình ảnh", Toast.LENGTH_SHORT).show();
      return;
    }
    getData();  // get data from fragment
    if (currentFragment == 1) {
      transactionFragment(R.id.fragment_container, ProfileSetupFragment.class);
      setColorForProgress(findViewById(R.id.setup_frag_2), getResources().getColor(R.color.white), Typeface.BOLD, true);
      setColorForProgress(findViewById(R.id.setup_frag_1), Color.parseColor("#cccccc"), Typeface.NORMAL, false);
    } else if (currentFragment == 2) {
      transactionFragment(R.id.fragment_container, ProfileDescriptionSetup.class);
      setColorForProgress(findViewById(R.id.setup_frag_3), getResources().getColor(R.color.white), Typeface.BOLD, true);
      setColorForProgress(findViewById(R.id.setup_frag_2), Color.parseColor("#cccccc"), Typeface.NORMAL, false);
    } else if (currentFragment == 3) {
      transactionFragment(R.id.fragment_container, FinalSetup.class);
      setColorForProgress(findViewById(R.id.setup_frag_4), getResources().getColor(R.color.white), Typeface.BOLD, true);
      setColorForProgress(findViewById(R.id.setup_frag_3), Color.parseColor("#cccccc"), Typeface.NORMAL, false);
    } else {
      // send data to server
      Toast.makeText(this, "Đã hoàn thành", Toast.LENGTH_SHORT).show();
    }
    if (currentFragment < numFragments) currentFragment += 1;
  }


}