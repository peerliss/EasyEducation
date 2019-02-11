package au.com.easyeducation.easyeducation_3.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import au.com.easyeducation.easyeducation_3.Model.Course;
import au.com.easyeducation.easyeducation_3.Model.Institution;
import au.com.easyeducation.easyeducation_3.R;

public class CollegeCourseInformationActivity extends AppCompatActivity {

    private String instituteRefString;
    private String businessTypeString;
    private String courseRefString;

    private Course course;

    private FirebaseFirestore db;
    private DocumentReference courseRef;

    private TextView courseName;
    private TextView courseCode;
    private TextView courseIntake;
    private TextView courseDuration;
    private TextView courseFullFee;
    private TextView courseCashBack;
    private TextView coursePayable;
    private TextView courseOverview;

    private Button courseApplyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_course_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable gradient = getResources().getDrawable(R.drawable.gradient);
        getSupportActionBar().setBackgroundDrawable(gradient);

        Intent intent = getIntent();

        businessTypeString = intent.getExtras().getString("businessType");
        instituteRefString = intent.getExtras().getString("businessRef");
        courseRefString = intent.getExtras().getString("courseRef");

        db = FirebaseFirestore.getInstance();

        courseRef = db.collection(businessTypeString).document(instituteRefString)
                .collection("courses").document(courseRefString);

        courseName = findViewById(R.id.courseInfoName_TextView);
        courseCode = findViewById(R.id.courseInfoCode_TextView);
        courseIntake = findViewById(R.id.courseInfoIntake_TextView);
        courseDuration = findViewById(R.id.courseInfoDuration_TextView);
        courseFullFee = findViewById(R.id.courseInfoFullFee_TextView);
        courseCashBack = findViewById(R.id.courseInfoCashBack_TextView);
        coursePayable = findViewById(R.id.courseInfoYouPay_TextView);
        courseOverview = findViewById(R.id.courseInfoOverview_TextView);

        courseApplyButton = findViewById(R.id.courseInfoApplyNow_Button);

        courseApplyButton.setBackground(gradient);

        courseApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CourseApplicationActivity.class);
                intent.putExtra("businessType", businessTypeString);
                intent.putExtra("businessRef", instituteRefString);
                intent.putExtra("courseRef", courseRefString);
                startActivityForResult(intent, 1);
//                startActivity(intent);
            }
        });

        courseRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DecimalFormat formatter = new DecimalFormat("#,###,###");

                course = documentSnapshot.toObject(Course.class);

                Double fullFee = Double.valueOf(course.getFullFee());
                String fullFeeString = formatter.format(fullFee);

                Double cashback = Double.valueOf(course.getCashback());
                String cashbackString = formatter.format(cashback);

                Double payable = Double.valueOf(course.getPayable());
                String payableString = formatter.format(payable);

                courseName.setText(course.getName());
                courseCode.setText("National Course Code: " + course.getCourseCode());
                courseIntake.setText("Next Intake: " + course.getIntake());
                courseDuration.setText(course.getDuration());
                courseFullFee.setText("Full Fee: $" + fullFeeString);
                courseCashBack.setText("Cash Back: $" + cashbackString);
                coursePayable.setText("You Pay: $" + payableString);
                courseOverview.setText(Html.fromHtml(course.getOverview(), 1)); // for 24 api and more
            }
        });

        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data.getStringExtra("businessType") != null) {
                    businessTypeString = data.getStringExtra("businessType");
                    instituteRefString = data.getStringExtra("businessRef");
                    courseRefString = data.getStringExtra("courseRef");
                }
                else {
                    Toast.makeText(this, "businessType is null", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // click on 'up' button in the action bar, handle it here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}