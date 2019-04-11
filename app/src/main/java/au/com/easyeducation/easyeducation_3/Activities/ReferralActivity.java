package au.com.easyeducation.easyeducation_3.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import au.com.easyeducation.easyeducation_3.R;

public class ReferralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable gradient = getResources().getDrawable(R.drawable.gradient);
        getSupportActionBar().setBackgroundDrawable(gradient);

        Button callButton = findViewById(R.id.support_call_button);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+611300411875", null));
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.referral_bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_bottom_navigation_referral);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_bottom_navigation_home:
                        Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent_main);
                        break;
                    case R.id.menu_bottom_navigation_referral:
                        break;
                    case R.id.menu_bottom_navigation_course:
                        Intent intent_course = new Intent(getApplicationContext(), CourseApplicationStatusActivity.class);
                        startActivity(intent_course);
                        break;
                    case R.id.menu_bottom_navigation_support:
                        Intent intent_support = new Intent(getApplicationContext(), SupportActivity.class);
                        startActivity(intent_support);
                        break;
                }

                return false;
            }
        });
    }

}
