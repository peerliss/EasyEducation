package au.com.app.edcall.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import au.com.app.edcall.R;

public class CourseApplicationStatusActivity extends AppCompatActivity {

    private LinearLayout payTuitionLayout;
    private TextView institutionCricosTv;
    private TextView institutionNameTv;
    private TextView courseDurationTv;
    private TextView courseCodeTv;
    private TextView courseNameTv;
    private TextView applicationStatusTv;
    private TextView deniedTv;
    private TextView congratulationsTv;
    private LinearLayout applicationStatusDetailsLayout;
    private LinearLayout notAppliedLayout;
    private String numberOfApplications = "1";
    private String applicationString = "Application 1";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_application_status);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable gradient = getResources().getDrawable(R.drawable.gradient);
        getSupportActionBar().setBackgroundDrawable(gradient);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        DocumentReference userRef = db.collection("users").document(mAuth.getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("numberOfApplications") != null) {
                    numberOfApplications = documentSnapshot.getString("numberOfApplications");
                    applicationString = "Application ".concat(numberOfApplications);
                    loadApplicationStatus(applicationString);
                }
                else {
                    loadApplicationStatus("Application 1");
                }
            }
        });

        notAppliedLayout = findViewById(R.id.courseApplicationStatus_notApplied_layout);
        applicationStatusDetailsLayout = findViewById(R.id.courseApplicationStatus_details_layout);

        congratulationsTv = findViewById(R.id.courseApplicationStatus_congratulations_tv);
        deniedTv = findViewById(R.id.courseApplicationStatus_denied_tv);

        applicationStatusTv = findViewById(R.id.courseApplicationStatus_applicationStatus_tv);

        courseNameTv = findViewById(R.id.courseApplicationStatus_courseName_tv);
        courseCodeTv = findViewById(R.id.courseApplicationStatus_courseCode_tv);
        courseDurationTv = findViewById(R.id.courseApplicationStatus_courseDuration_tv);

        institutionNameTv = findViewById(R.id.courseApplicationStatus_institutionName_tv);
        institutionCricosTv = findViewById(R.id.courseApplicationStatus_institutionCricos_tv);

        payTuitionLayout = findViewById(R.id.courseApplicationStatus_payTuition_layout);

        Button payTuition = findViewById(R.id.courseApplicationStatus_payTuition_button);
        payTuition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CourseApplicationPaymentActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.courseApplicationStatus_bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_bottom_navigation_course);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_bottom_navigation_home:
                        Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent_main);
                        break;
                    case R.id.menu_bottom_navigation_referral:
                        Intent intent_referral = new Intent(getApplicationContext(), ReferralActivity.class);
                        startActivity(intent_referral);
                        break;
                    case R.id.menu_bottom_navigation_course:
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

    private void loadApplicationStatus(String applicationRefString) {
        DocumentReference applicationRef = db.collection("users").document(mAuth.getUid()).collection("Applications").document(applicationRefString);
        applicationRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("applicationStatus") != null) {
                    notAppliedLayout.setVisibility(View.GONE);
                    applicationStatusDetailsLayout.setVisibility(View.VISIBLE);

                    if (documentSnapshot.getString("applicationStatus").matches("Accepted")) {
                        applicationStatusTv.setText("Application Status - Accepted");
                        congratulationsTv.setVisibility(View.VISIBLE);
                        payTuitionLayout.setVisibility(View.VISIBLE);
                    }
                    if (documentSnapshot.getString("applicationStatus").matches("Denied")) {
                        applicationStatusTv.setText("Application Status - Denied");
                        deniedTv.setVisibility(View.VISIBLE);
                    }
                }
                if (documentSnapshot.getString("courseName") != null) {
                    courseNameTv.setText("Course Name - " + documentSnapshot.getString("courseName"));
                }
                if (documentSnapshot.getString("courseCode") != null) {
                    courseCodeTv.setText("National Course Code - " + documentSnapshot.getString("courseCode"));
                }

                if (documentSnapshot.getString("institutionName") != null) {
                    institutionNameTv.setText("Institution Name - " + documentSnapshot.getString("institutionName"));
                }
                if (documentSnapshot.getString("institutionCricos") != null) {
                    institutionCricosTv.setText("Institution Cricos - " + documentSnapshot.getString("institutionCricos"));
                }
                // Testing payments - remove to revert to visible when applied
//                else {
//                    notAppliedLayout.setVisibility(View.GONE);
//                    applicationStatusDetailsLayout.setVisibility(View.VISIBLE);
//                }
            }
        });
    }
}