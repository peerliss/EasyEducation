package au.com.easyeducation.easyeducation_3.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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

import java.text.DecimalFormat;

import au.com.easyeducation.easyeducation_3.Model.Course;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApplicationPaymentActivity extends AppCompatActivity {

    private TextView institutionCricosTv;
    private TextView institutionNameTv;
    private TextView courseCodeTv;
    private TextView courseNameTv;

    private TextView courseFullFee;
    private TextView courseCashBack;
    private TextView coursePayable;
    private TextView courseFullFeeSemester;
    private TextView courseCashBackSemester;
    private TextView coursePayableSemester;

    private String numberOfApplications = "1";
    private String applicationString = "Application 1";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private DocumentReference courseRef;
    private String institutionRefString;
    private String courseCodeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_application_payment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        courseNameTv = findViewById(R.id.courseApplicationPayment_courseName_tv);
        courseCodeTv = findViewById(R.id.courseApplicationPayment_courseCode_tv);

        institutionNameTv = findViewById(R.id.courseApplicationPayment_institutionName_tv);
        institutionCricosTv = findViewById(R.id.courseApplicationPayment_institutionCricos_tv);

        courseFullFee = findViewById(R.id.courseApplicationPayment_payment_fullFee_tv);
        courseCashBack = findViewById(R.id.courseApplicationPayment_payment_cashback_tv);
        coursePayable = findViewById(R.id.courseApplicationPayment_payment_youPay_tv);

        courseFullFeeSemester = findViewById(R.id.courseApplicationPayment_payment_fullFeeSemester_tv);
        courseCashBackSemester = findViewById(R.id.courseApplicationPayment_payment_cashbackSemester_tv);
        coursePayableSemester = findViewById(R.id.courseApplicationPayment_payment_youPaySemester_tv);
    }

    private void loadApplicationStatus(String applicationRefString) {
        DocumentReference applicationRef = db.collection("users").document(mAuth.getUid()).collection("Applications").document(applicationRefString);
        applicationRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
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
                if (documentSnapshot.getString("institutionRef") != null) {
                    institutionRefString = documentSnapshot.getString("institutionRef");
                }
                if (documentSnapshot.getString("courseCode") != null) {
                    courseCodeString = documentSnapshot.getString("courseCode");
                    loadPaymentInformation();
                }
            }
        });
    }

    private void loadPaymentInformation() {
        courseRef = db.collection("institutions").document(institutionRefString).collection("courses").document(courseCodeString);

        courseRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DecimalFormat formatter = new DecimalFormat("#,###,###");

                Course course = documentSnapshot.toObject(Course.class);

                Double fullFee = Double.valueOf(course.getFullFee());
                Double fullSemesterFee = (double) (course.getFullFee() / (Double.valueOf(course.getDuration().substring(0, 1)) * 2));
//                Double fullSemesterFee = 20000.0;

                String fullFeeString = formatter.format(fullFee);
                String fullSemesterFeeString = formatter.format(fullSemesterFee);

                Double cashback = Double.valueOf(course.getCashback());
                Double cashbackSemester = (double) (course.getCashback() / (Double.valueOf(course.getDuration().substring(0, 1)) * 2));

                String cashbackString = formatter.format(cashback);
                String cashbackSemesterString = formatter.format(cashbackSemester);

                Double payable = Double.valueOf(course.getPayable());
                Double payableSemester = (double) (course.getPayable() / (Double.valueOf(course.getDuration().substring(0, 1)) * 2));

                String payableString = formatter.format(payable);
                String payableSemesterString = formatter.format(payableSemester);

                courseFullFee.setText("Full Fee: $" + fullFeeString);
                courseCashBack.setText("Cash Back: $" + cashbackString);
                coursePayable.setText("You Pay: $" + payableString);

                courseFullFeeSemester.setText("Full Fee: $" + fullSemesterFeeString + " per semester");
                courseCashBackSemester.setText("Cash Back: $" + cashbackSemesterString + " per semester");
                coursePayableSemester.setText("You Pay: $" + payableSemesterString + " per semester");
            }
        });
    }
}