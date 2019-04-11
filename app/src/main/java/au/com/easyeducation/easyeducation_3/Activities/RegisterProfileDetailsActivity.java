package au.com.easyeducation.easyeducation_3.Activities;

import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import au.com.easyeducation.easyeducation_3.Fragments.RegisterProfileCountryBirthFragment;
import au.com.easyeducation.easyeducation_3.Fragments.RegisterProfileCountryCitizenshipFragment;
import au.com.easyeducation.easyeducation_3.Fragments.RegisterProfileDOBFragment;
import au.com.easyeducation.easyeducation_3.Fragments.RegisterProfileInternationalFragment;
import au.com.easyeducation.easyeducation_3.Fragments.RegisterProfileNameFragment;
import au.com.easyeducation.easyeducation_3.Fragments.RegisterProfileNumberFragment;
import au.com.easyeducation.easyeducation_3.Fragments.RegisterProfileNumberVerifyFragment;
import au.com.easyeducation.easyeducation_3.R;

public class RegisterProfileDetailsActivity extends AppCompatActivity {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile_details);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_profile_details, menu);
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
        private static final String ARG_SECTION_NUMBER = "section_number";

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
            View rootView = null;

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    rootView = inflater.inflate(R.layout.register_profile_name_fragment, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.register_profile_dob_fragment, container, false);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.register_profile_number_fragment, container, false);
                    break;
                case 4:
                    rootView = inflater.inflate(R.layout.register_profile_number_verify_fragment, container, false);
                    break;
                case 5:
                    rootView = inflater.inflate(R.layout.register_profile_international_student_fragment, container, false);
                    break;
                case 6:
                    rootView = inflater.inflate(R.layout.register_profile_country_birth_fragment, container, false);
                    break;
                case 7:
                    rootView = inflater.inflate(R.layout.register_profile_country_citizenship_fragment, container, false);
                    break;
            }

            return rootView;
        }
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        mViewPager.setCurrentItem(item, smoothScroll);
    }

    public int getCurrentItem() {
        return mViewPager.getCurrentItem();
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
//            return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    return RegisterProfileNameFragment.newInstance();
                case 1:
                    return RegisterProfileDOBFragment.newInstance();
                case 2:
                    return RegisterProfileNumberFragment.newInstance();
                case 3:
                    return RegisterProfileNumberVerifyFragment.newInstance();
                case 4:
                    return RegisterProfileInternationalFragment.newInstance();
                case 5:
                    return RegisterProfileCountryBirthFragment.newInstance();
                case 6:
                    return RegisterProfileCountryCitizenshipFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 7;
        }
    }
}