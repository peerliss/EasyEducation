package au.com.easyeducation.easyeducation_3.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import au.com.easyeducation.easyeducation_3.Fragments.CourseApply1Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply2Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply3Fragment;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApplicationNewActivity extends AppCompatActivity {

    private FrameLayout fragment_container;
    private Button nextButton;
    private ProgressBar applyCourseProgressBar;
    private int nextPressedTimes;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_application);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable gradient = getResources().getDrawable(R.drawable.gradient);
        getSupportActionBar().setBackgroundDrawable(gradient);

        findViewById(R.id.container).setVisibility(View.GONE);

        fragment_container = findViewById(R.id.courseApply_fragment_container);
        nextButton = findViewById(R.id.courseApplicationNextButton);
        applyCourseProgressBar = findViewById(R.id.courseApplicationProgressBar);

        nextButton.setBackground(gradient);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment();
            }
        });

        fragmentManager = getSupportFragmentManager();

//        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                Toast.makeText(CourseApplicationNewActivity.this, "Fragment count in backstack - " + fragmentManager.getBackStackEntryCount(), Toast.LENGTH_LONG).show();
//            }
//        });

        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            addFragment();
        }
//        nextButton.setVisibility(View.GONE);
    }

    public void addFragment() {
        Fragment fragment;

        switch (fragmentManager.getBackStackEntryCount()) {
            case 0:
                fragment = new CourseApply1Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress()+1);
                break;
            case 1:
//                nextButton.setVisibility(View.VISIBLE);
                fragment = new CourseApply2Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress()+1);
                break;
            case 2:
                fragment = new CourseApply3Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress()+1);
                break;
            default:
//                fragment = new CourseApply1Fragment();
                return;
//                break;
        }

        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.left_to_right, R.anim.right_to_left);
        fragmentTransaction.replace(R.id.courseApply_fragment_container, fragment, "CourseApplyFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        // This is the black back button on the bottom of the screen
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            finish();
        } else {
            applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress()-1);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_application, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Action settings", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.form_checklist) {
            Toast.makeText(this, "Back to checklist", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), CourseApplicationChecklistActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
//            Toast.makeText(this, "Home Pressed", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}