package au.com.easyeducation.easyeducation_3.Activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.stripe.android.SourceCallback;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Customer;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentIntentParams;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.Source;
import com.stripe.android.model.SourceParams;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.w3c.dom.Document;

import java.security.Signature;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import au.com.easyeducation.easyeducation_3.Model.Course;
import au.com.easyeducation.easyeducation_3.Model.CourseApplication;
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
    private TextView additionalFee1Tv;
    private TextView additionalFee2Tv;
    private TextView additionalFee3Tv;

    private String numberOfApplications = "1";
    private String applicationString = "Application 1";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private DocumentReference courseRef;
    private String institutionRefString;
    private String courseCodeString;
    private CardInputWidget mCardInputWidget;
    private Card cardToSave;
    private Button confirmButton;
    private FirebaseFunctions firebaseFunctions;
    private Stripe stripe;
    private DocumentReference userRef;
    private SourceCallback sourceCallback;
    private String sourceId;

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
        firebaseFunctions = FirebaseFunctions.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("numberOfApplications") != null) {
                    numberOfApplications = documentSnapshot.getString("numberOfApplications");
                    applicationString = "Application ".concat(numberOfApplications);
                    loadApplicationStatus(applicationString);
                } else {
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

        additionalFee1Tv = findViewById(R.id.courseApplicationPayment_additionalFees_1_tv);
        additionalFee2Tv = findViewById(R.id.courseApplicationPayment_additionalFees_2_tv);
        additionalFee3Tv = findViewById(R.id.courseApplicationPayment_additionalFees_3_tv);

        confirmButton = findViewById(R.id.courseApplicationPayment_confirm_button);

        mCardInputWidget = findViewById(R.id.card_input_widget);
        mCardInputWidget.clearFocus();

//        Card card = new Card("4000-0003-6000-0006", 12, 2020, "123");
//        card.validateCVC();

        stripe = new Stripe(getApplicationContext(), "pk_test_jDcOdq33BYd98URKsivS1jfh");

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardToSave = mCardInputWidget.getCard();

                // Add details such as full name and address to card
                fillCardFields();

                if (!cardToSave.validateCard()) {
                    Toast.makeText(getApplicationContext(), "Card is invalid", Toast.LENGTH_LONG).show();
                    return;
                }
                stripe.createToken(
                        cardToSave,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                // Send token to your server
                                DocumentReference tokenRef;
                                tokenRef = db.collection("stripe_customers").document(mAuth.getUid()).collection("tokens").document();

                                tokenRef.set(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(CourseApplicationPaymentActivity.this, "Token successfully added to database", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CourseApplicationPaymentActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                                SourceParams sourceParams = SourceParams.createCardParams(cardToSave);
                                stripe.createSource(sourceParams, sourceCallback);

                                final String tokenId = token.getId();

                                sourceCallback = new SourceCallback() {
                                    @Override
                                    public void onError(@NonNull Exception error) {
                                        Toast.makeText(CourseApplicationPaymentActivity.this, "Source" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onSuccess(@NonNull Source source) {
                                        DocumentReference sourceRef;
                                        sourceRef = db.collection("stripe_customers").document(mAuth.getUid()).collection("sources").document(tokenId);
                                        sourceRef.set(source);
                                        Toast.makeText(CourseApplicationPaymentActivity.this, "Source" + source.getId(), Toast.LENGTH_SHORT).show();
                                        sourceId = source.getId();
                                    }
                                };

                            }

                            public void onError(Exception error) {
                                // Show localized error message
                                Toast.makeText(getApplicationContext(),
                                        error.getLocalizedMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );

                DocumentReference chargeRef;
                chargeRef = db.collection("stripe_customers").document(mAuth.getUid()).collection("charges").document();
                Map<String, Object> amount = new HashMap<>();

                //Test amount of 5 - to be replaced with a variable
                amount.put("amount", 5);
                amount.put("currency", "aud");
                amount.put("description", "Test charge");
                amount.put("source", sourceId);
                chargeRef.set(amount);
            }
        });
    }

    private void fillCardFields() {
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CourseApplication courseApplication = documentSnapshot.toObject(CourseApplication.class);
                Toast.makeText(CourseApplicationPaymentActivity.this, courseApplication.getFullname(), Toast.LENGTH_SHORT).show();
                cardToSave.setName(courseApplication.getFullname());
                cardToSave.setAddressLine1(courseApplication.getStreet());
                cardToSave.setAddressCity(courseApplication.getSuburb());
                cardToSave.setAddressZip(courseApplication.getPostCode());
                cardToSave.setAddressState("NSW");
                cardToSave.setAddressState(courseApplication.getState());
                cardToSave.setAddressCountry("Australia");
                cardToSave.setCurrency("AUD");
            }
        });
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
                if (documentSnapshot.getString("fullname") != null) {
//                    cardToSave.setName(documentSnapshot.getString("fullname"));
                }
                if (documentSnapshot.getString("postCode") != null) {
//                    cardToSave.setAddressZip(documentSnapshot.getString("postCode"));
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

                if (documentSnapshot.getString("additionalFee1") != null) {
                    findViewById(R.id.courseApplicationPayment_additionalFees_layout).setVisibility(View.VISIBLE);
                    additionalFee1Tv.setText(documentSnapshot.getString("additionalFee1"));
                    additionalFee1Tv.setVisibility(View.VISIBLE);
                }
                if (documentSnapshot.getString("additionalFee2") != null) {
                    additionalFee2Tv.setText(documentSnapshot.getString("additionalFee2"));
                    additionalFee2Tv.setVisibility(View.VISIBLE);
                }
                if (documentSnapshot.getString("additionalFee3") != null) {
                    additionalFee3Tv.setText(documentSnapshot.getString("additionalFee3"));
                    additionalFee3Tv.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}