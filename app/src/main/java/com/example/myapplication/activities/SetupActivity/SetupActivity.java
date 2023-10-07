package com.example.myapplication.activities.SetupActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

public class SetupActivity extends AppCompatActivity {
  int currentFragment = 0;
  int numFragments = 4;

  @SuppressLint("ResourceAsColor")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setup);
    transactionFragment(R.id.fragment_container, BasicInfoSetupFragment.class);
    setColorForProgress(findViewById(R.id.setup_frag_1), getResources().getColor(R.color.white), Typeface.BOLD, true);
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
    currentFragment -= (currentFragment >= 0) ? 1 : 0;
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
    currentFragment += (currentFragment < numFragments) ? 1 : 0;
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
      Toast.makeText(this, "You're done!", Toast.LENGTH_SHORT).show();
    }
  }
}