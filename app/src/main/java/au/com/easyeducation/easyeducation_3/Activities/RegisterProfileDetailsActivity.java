package au.com.easyeducation.easyeducation_3.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import au.com.easyeducation.easyeducation_3.Activities.ui.registerprofiledetails.RegisterProfileDetailsFragment;
import au.com.easyeducation.easyeducation_3.R;

public class RegisterProfileDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_profile_details_activity);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, RegisterProfileDetailsFragment.newInstance())
//                    .commitNow();
//        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}