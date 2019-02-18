package au.com.easyeducation.easyeducation_3.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewParent;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import au.com.easyeducation.easyeducation_3.Fragments.CourseApply1Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply2Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply3Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply4Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply5Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply6Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply7Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply8Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply9Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.RegisterProfileDOBFragment;
import au.com.easyeducation.easyeducation_3.Fragments.RegisterProfileNameFragment;
import au.com.easyeducation.easyeducation_3.Fragments.RegisterProfileNumberFragment;
import au.com.easyeducation.easyeducation_3.Fragments.RegisterProfileNumberVerifyFragment;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApplicationActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String businessTypeString;
    private String instituteRefString;
    private String courseRefString;

    private ProgressBar applyCourseProgressBar;
    private Button nextButton;
    private int currentViewItem;
    private boolean nextPressed;
    private int nextPressedTimes;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_application);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable gradient = getResources().getDrawable(R.drawable.gradient);
        getSupportActionBar().setBackgroundDrawable(gradient);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

//        Intent intent = getIntent();
//        setResult(RESULT_OK, intent);

//        businessTypeString = intent.getExtras().getString("businessType");
//        instituteRefString = intent.getExtras().getString("businessRef");
//        courseRefString = intent.getExtras().getString("courseRef");

        applyCourseProgressBar = findViewById(R.id.courseApplicationProgressBar);
        nextButton = findViewById(R.id.courseApplicationNextButton);

        nextButton.setBackground(gradient);

        nextPressedTimes = 1;

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPressedTimes = nextPressedTimes + 1;
                applyCourseProgressBar.setProgress(nextPressedTimes);

                setCurrentItem(mViewPager.getCurrentItem() + 1, true);
            }
        });

//        Toast.makeText(this, businessTypeString + instituteRefString + courseRefString, Toast.LENGTH_SHORT).show();
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

        if (id == R.id.homeAsUp) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_course_apply_1, container, false);

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_course_apply_1, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_course_apply_2, container, false);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_course_apply_8, container, false);
                    break;
                case 4:
                    rootView = inflater.inflate(R.layout.fragment_course_apply_3, container, false);
                    break;
                case 5:
                    rootView = inflater.inflate(R.layout.fragment_course_apply_4, container, false);
                    break;
                case 6:
                    rootView = inflater.inflate(R.layout.fragment_course_apply_9, container, false);
                    break;
                case 7:
                    rootView = inflater.inflate(R.layout.fragment_course_apply_5, container, false);
                    break;
                case 8:
                    rootView = inflater.inflate(R.layout.fragment_course_apply_6, container, false);
                    break;
                case 9:
                    rootView = inflater.inflate(R.layout.fragment_course_apply_7, container, false);
                    break;
            }

            return rootView;
        }
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        mViewPager.setCurrentItem(item, smoothScroll);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return CourseApply1Fragment.newInstance();
                case 1:
                    return CourseApply2Fragment.newInstance();
                case 2:
                    return CourseApply8Fragment.newInstance();
                case 3:
                    return CourseApply3Fragment.newInstance();
                case 4:
                    return CourseApply4Fragment.newInstance();
                case 5:
                    return CourseApply9Fragment.newInstance();
                case 6:
                    return CourseApply5Fragment.newInstance();
                case 7:
                    return CourseApply6Fragment.newInstance();
                case 8:
                    return CourseApply7Fragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 9 total pages.
            return 9;
        }
    }
}
