package co.edcall.app.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

import co.edcall.app.Model.Course;
import co.edcall.app.R;

public class CollegeCourseInformationActivity extends AppCompatActivity {

    private String instituteRefString;
    private String businessTypeString;
    private String courseRefString;

    private Course course;

    private FirebaseFirestore db;
    private DocumentReference courseRef;

    private TextView courseName;
    private TextView courseCode;
    private TextView courseCricos;
    private TextView courseIntake;
    private TextView courseDuration;
    private TextView courseTuitionFee;
    private TextView courseEnrolmentFee;
    private TextView courseMaterialFee;
    private TextView courseCashBack;
    private TextView coursePayable;
    private TextView courseClassHours;
    private TextView courseClassAvailability;
    private TextView courseOverview;
    private TextView courseSemesterCosts;

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
        courseCricos = findViewById(R.id.courseInfoCricos_TextView);
        courseCode = findViewById(R.id.courseInfoCode_TextView);
        courseIntake = findViewById(R.id.courseInfoIntake_TextView);
        courseDuration = findViewById(R.id.courseInfoDuration_TextView);
        courseTuitionFee = findViewById(R.id.courseInfoFullFee_TextView);
        courseEnrolmentFee = findViewById(R.id.courseInfoEnrolmentFee_TextView);
        courseMaterialFee = findViewById(R.id.courseInfoMaterialFee_TextView);
        courseCashBack = findViewById(R.id.courseInfoCashBack_TextView);
        coursePayable = findViewById(R.id.courseInfoYouPay_TextView);
        courseOverview = findViewById(R.id.courseInfoOverview_TextView);
        courseClassHours = findViewById(R.id.courseInfoClassHours_TextView);
        courseClassAvailability = findViewById(R.id.courseInfoClassAvailability_TextView);
        courseSemesterCosts = findViewById(R.id.courseInfoSemesterCosts_TextView);

        courseApplyButton = findViewById(R.id.courseInfoApplyNow_Button);

        courseApplyButton.setBackground(gradient);

        courseApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), CourseApplicationActivity.class);
//                intent.putExtra("businessType", businessTypeString);
//                intent.putExtra("businessRef", instituteRefString);
//                intent.putExtra("courseRef", courseRefString);
//                startActivityForResult(intent, 1);
//
                Intent intent = new Intent(getApplicationContext(), CourseApplicationChecklistActivity.class);
                intent.putExtra("businessType", businessTypeString);
                intent.putExtra("businessRef", instituteRefString);
                intent.putExtra("courseRef", courseRefString);
                startActivityForResult(intent, 1);
            }
        });

        try {
            courseRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DecimalFormat formatter = new DecimalFormat("#,###,###");

                    course = documentSnapshot.toObject(Course.class);

                    int installments = course.getInstallments();

                    Double fullFee = Double.valueOf(course.getFullFee());
                    Double fullSemesterFee = (double) (course.getFullFee() / installments);

                    String tuitionFeeString = formatter.format(fullFee);
                    String fullSemesterFeeString = formatter.format(fullSemesterFee);

                    Double enrolmentFee = Double.valueOf(course.getEnrolmentFee());
                    Double materialFee = Double.valueOf(course.getMaterialFee());

                    String enrolmentFeeString = formatter.format(enrolmentFee);
                    String materialFeeString = formatter.format(materialFee);
                    
                    Double cashback = Double.valueOf(course.getCashback());
                    Double cashbackSemester = (double) (course.getCashback() / installments);

                    String cashbackString = formatter.format(cashback);
                    String cashbackSemesterString = formatter.format(cashbackSemester);

                    Double payable = Double.valueOf(course.getPayable());
                    Double payableSemester = (double) (course.getPayable() / installments);

                    String payableString = formatter.format(payable);
                    String payableSemesterString = formatter.format(payableSemester);

                    courseName.setText(course.getName());
                    courseCricos.setText("Cricos Code: " + course.getCourseCricos());
                    courseCode.setText("National Course Code: " + course.getCourseCode());
                    courseIntake.setText("Next Intake: " + course.getIntake());
                    courseDuration.setText("Duration: " + course.getDuration());
                    courseClassHours.setText("Class hours: " + course.getHours());
                    courseClassAvailability.setText("Class Availability: " + course.getAvailability());
                    courseTuitionFee.setText("Tuition: $" + tuitionFeeString);
                    courseEnrolmentFee.setText("Enrolment Fee: $" + enrolmentFeeString);
                    courseMaterialFee.setText("Material Fee: $" + materialFeeString);
                    courseCashBack.setText("Cash Back: $" + cashbackString);
                    coursePayable.setText("You Pay: $" + payableString);
                    courseOverview.setText(Html.fromHtml(course.getOverview(), 1)); // for 24 api and more

                    courseSemesterCosts.setText("Tuition fee $" + fullSemesterFeeString + " per semester. Cash back $"
                            + cashbackSemesterString + " per semester. You pay total $" + payableSemesterString + " per semester.");
                }
            });
        }
        catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

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