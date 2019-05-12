package co.edcall.app.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import java.util.HashMap;
import java.util.Map;

import co.edcall.app.R;

public class CourseApplicationStatusActivity extends AppCompatActivity {

    private LinearLayout payTuitionLayout;
    private LinearLayout payApplicationFeeLayout;
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

    private TextView applicationFeeinstitutionCricosTv;
    private TextView applicationFeeinstitutionNameTv;
    private TextView applicationFeecourseCodeTv;
    private TextView applicationFeecourseNameTv;
    private TextView applicationFeeTv;
    private CardInputWidget payApplicationFeeCardInput;
    private Button payApplicationFeeButton;
    private String institutionRef;
    private String courseCode;
    private int applicationFee;
    private Stripe stripe;
    private DocumentReference tokenRef;
    private boolean sourcesExists = false;
    private DocumentReference chargeRef;
    private Card cardToSave;
    private ProgressBar progressBar;
    private DocumentReference applicationRef;

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

        institutionNameTv = findViewById(R.id.courseApplicationStatus_institutionName_tv);
        institutionCricosTv = findViewById(R.id.courseApplicationStatus_institutionCricos_tv);
        courseNameTv = findViewById(R.id.courseApplicationStatus_courseName_tv);
        courseCodeTv = findViewById(R.id.courseApplicationStatus_courseCode_tv);
        courseDurationTv = findViewById(R.id.courseApplicationStatus_courseDuration_tv);

        payTuitionLayout = findViewById(R.id.courseApplicationStatus_payTuition_layout);

        payApplicationFeeLayout = findViewById(R.id.courseApplicationStatus_ApplicationFee_layout);

        applicationFeeinstitutionNameTv = findViewById(R.id.courseApplicationStatus_ApplicationFee_institutionName_tv);
        applicationFeeinstitutionCricosTv = findViewById(R.id.courseApplicationStatus_ApplicationFee_institutionCricos_tv);
        applicationFeecourseNameTv = findViewById(R.id.courseApplicationStatus_ApplicationFee_courseName_tv);
        applicationFeecourseCodeTv = findViewById(R.id.courseApplicationStatus_ApplicationFee_courseCode_tv);
        applicationFeeTv = findViewById(R.id.courseApplicationStatus_ApplicationFee_fee_tv);
        payApplicationFeeCardInput = findViewById(R.id.courseApplicationStatus_ApplicationFee_cardInput);
        payApplicationFeeButton = findViewById(R.id.courseApplicationStatus_ApplicationFee_confirmPayment_button);
        progressBar = findViewById(R.id.courseApplicationStatus_ApplicationFee_ProgressBar);

        payApplicationFeeCardInput.clearFocus();

        tokenRef = db.collection("stripe_customers").document(mAuth.getUid()).collection("tokens").document();
        stripe = new Stripe(getApplicationContext(), "pk_live_0lzMcKMzGSb3lf2T0IDLzSG1");

        payApplicationFeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardToSave = payApplicationFeeCardInput.getCard();

                if (cardToSave == null || !cardToSave.validateCard()) {
                    Toast.makeText(getApplicationContext(), "Card is invalid", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);// To Show ProgressBar

                    createToken();
                }
            }
        });

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
        applicationRef = db.collection("users").document(mAuth.getUid()).collection("Applications").document(applicationRefString);
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
                    applicationFeecourseNameTv.setText("Course Name - " + documentSnapshot.getString("courseName"));
                }
                if (documentSnapshot.getString("courseCode") != null) {
                    courseCodeTv.setText("National Course Code - " + documentSnapshot.getString("courseCode"));
                    applicationFeecourseCodeTv.setText("National Course Code - " + documentSnapshot.getString("courseCode"));
                    courseCode = documentSnapshot.getString("courseCode");
                }

                if (documentSnapshot.getString("institutionName") != null) {
                    institutionNameTv.setText("Institution Name - " + documentSnapshot.getString("institutionName"));
                    applicationFeeinstitutionNameTv.setText("Institution Name - " + documentSnapshot.getString("institutionName"));
                }
                if (documentSnapshot.getString("institutionCricos") != null) {
                    institutionCricosTv.setText("Institution Cricos - " + documentSnapshot.getString("institutionCricos"));
                    applicationFeeinstitutionCricosTv.setText("Institution Cricos - " + documentSnapshot.getString("institutionCricos"));
                }
                if (documentSnapshot.getString("institutionRef") != null) {
                    institutionRef = documentSnapshot.getString("institutionRef");
                    hasApplicationFee();
                }
            }
        });
    }

    private void hasApplicationFee() {
        DocumentReference courseRef = db.collection("institutions").document(institutionRef).collection("courses").document(courseCode);
        courseRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("applicationFee") != null) {
                    notAppliedLayout.setVisibility(View.GONE);
                    payApplicationFeeLayout.setVisibility(View.VISIBLE);
                    applicationStatusDetailsLayout.setVisibility(View.GONE);

                    applicationFeeTv.setText("Application Fee - $" + documentSnapshot.getString("applicationFee"));
                    applicationFee = Integer.valueOf(documentSnapshot.getString("applicationFee"));
                    payApplicationFeeButton.setText("Pay $" + documentSnapshot.getString("applicationFee"));
                }
            }
        });
    }

    private void createToken() {
        try {
            CollectionReference sourceRef = db.collection("stripe_customers").document(mAuth.getUid()).collection("sources");
            sourceRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
//                        Toast.makeText(CourseApplicationPaymentActivity.this, "Sources exists", Toast.LENGTH_SHORT).show();
                        sourcesExists = true;
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

        if (sourcesExists) {
            createCharge();
        } else {
            stripe.createToken(cardToSave, new TokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    // Send token to your server
                    tokenRef.set(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            createCharge();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Token upload failure - " + e.getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE); //To Hide ProgressBar
                        }
                    });
                }

                @Override
                public void onError(Exception error) {
                    // Show localized error message
                    Toast.makeText(getApplicationContext(),
                            error.getLocalizedMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                    progressBar.setVisibility(View.INVISIBLE); //To Hide ProgressBar
                }
            });
        }
    }

    private void createCharge() {
        chargeRef = db.collection("stripe_customers").document(mAuth.getUid()).collection("charges").document();
        Map<String, Object> charge = new HashMap<>();

        charge.put("amount", applicationFee * 100);
        charge.put("currency", "aud");
        chargeRef.set(charge).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        checkChargeStatus();
                    }
                }, 2500);   //2.5 seconds
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Payment failed - " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE); //To Hide ProgressBar
            }
        });
    }

    private void checkChargeStatus() {
        chargeRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("status") != null) {
                    if (documentSnapshot.getString("status").matches("succeeded")) {
                        Toast.makeText(getApplicationContext(), "Payment successful", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE); //To Hide ProgressBar
                        setApplicationFeePaid();
                        changeStatusLayout();
                    }
                }
                else if (documentSnapshot.getString("error") != null) {
                    Toast.makeText(getApplicationContext(), documentSnapshot.getString("error"), Toast.LENGTH_LONG).show();
                }
            }
        });
        progressBar.setVisibility(View.INVISIBLE); //To Hide ProgressBar
    }

    private void setApplicationFeePaid() {
        applicationRef.update("applicationFeePaid", "true");
    }

    private void changeStatusLayout() {
        notAppliedLayout.setVisibility(View.GONE);
        payApplicationFeeLayout.setVisibility(View.GONE);
        applicationStatusDetailsLayout.setVisibility(View.VISIBLE);
    }
}