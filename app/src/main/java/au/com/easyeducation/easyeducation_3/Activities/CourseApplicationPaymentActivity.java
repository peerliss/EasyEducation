package au.com.easyeducation.easyeducation_3.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.stripe.android.CustomerSession;
import com.stripe.android.EphemeralKeyProvider;
import com.stripe.android.EphemeralKeyUpdateListener;
import com.stripe.android.SourceCallback;
import com.stripe.android.Stripe;
import com.stripe.android.StripeError;
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
import com.stripe.android.view.PaymentMethodsActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.security.Signature;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import au.com.easyeducation.easyeducation_3.Model.Course;
import au.com.easyeducation.easyeducation_3.Model.CourseApplication;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApplicationPaymentActivity extends AppCompatActivity {

    private EditText mReferredBy;
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
    private CollectionReference sourceRef;
    private SourceCallback sourceCallback;
    private String sourceId;
    private Source source;
    private int payableSemesterAmount;
    private DocumentReference tokenRef;
    private ProgressBar progressBar;
    private Customer customerRetrieved;
    private TextView successfullyPaid;
    private String referredBy;
    private String uid;
    private int numberOfReferrals;
    private String referralQuery;

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
                if (documentSnapshot.getString("referredBy") != null) {
                    mReferredBy.setText(documentSnapshot.getString("referredBy"));
                }
                if (documentSnapshot.getString("numberOfApplications") != null) {
                    numberOfApplications = documentSnapshot.getString("numberOfApplications");
                    applicationString = "Application ".concat(numberOfApplications);
                    loadApplicationStatus(applicationString);
                } else {
                    loadApplicationStatus("Application 1");
                }
            }
        });

        tokenRef = db.collection("stripe_customers").document(mAuth.getUid()).collection("tokens").document();

        mReferredBy = findViewById(R.id.courseApplicationPayment_referredBy_Et);

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
        successfullyPaid = findViewById(R.id.courseApplicationPayment_payment_SuccessfulPayments);

        additionalFee1Tv = findViewById(R.id.courseApplicationPayment_additionalFees_1_tv);
        additionalFee2Tv = findViewById(R.id.courseApplicationPayment_additionalFees_2_tv);
        additionalFee3Tv = findViewById(R.id.courseApplicationPayment_additionalFees_3_tv);

        confirmButton = findViewById(R.id.courseApplicationPayment_confirm_button);

        mReferredBy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mReferredBy.length() > 0) {
                    userRef.update("referredBy", mReferredBy.getText().toString().trim());
                }
            }
        });

        mCardInputWidget = findViewById(R.id.card_input_widget);
        mCardInputWidget.clearFocus();

        stripe = new Stripe(getApplicationContext(), "pk_test_jDcOdq33BYd98URKsivS1jfh");

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardToSave = mCardInputWidget.getCard();
//              Add details such as full name and address to card
//                fillCardFields();

                addReferralDetails();

                if (cardToSave == null || !cardToSave.validateCard()) {
                    Toast.makeText(getApplicationContext(), "Card is invalid", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    progressBar = findViewById(R.id.progressbar);
                    progressBar.setVisibility(View.VISIBLE);// To Show ProgressBar

                    createToken();
                }
            }
        });
    }

    private void addReferralDetails() {
        enterReferee();
    }

    private void enterReferee() {
        if (mReferredBy.length() > 0) {
            referredBy = mReferredBy.getText().toString().trim();

            userRef.update("referredBy", referredBy);

            DocumentReference referralRef;

            referralRef = db.collection("users").document(mAuth.getUid())
                    .collection("referrals").document("overview");
            referralRef.update("referredBy", referredBy);

            getRefereeUid();
        }
    }

    private void getRefereeUid() {
        try {
            CollectionReference userCollectionRef;
            userCollectionRef = db.collection("users");
            userCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        int numberOfDocs = queryDocumentSnapshots.getDocuments().size();
                        for (int i = 0; i < numberOfDocs; i++) {
                            if (queryDocumentSnapshots.getDocuments().get(i).getString("referralCode") != null) {
                                referralQuery = queryDocumentSnapshots.getDocuments().get(1).getString("referralCode");

                                if (referralQuery.matches(referredBy)) {
                                    uid = queryDocumentSnapshots.getDocuments().get(1).getString("uid");
                                    enterReferredByDoc();
                                } else {
                                    Toast.makeText(CourseApplicationPaymentActivity.this, "Invalid referee code", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void enterReferredByDoc() {
        final DocumentReference refereeReferralRef = db.collection("users").document(mAuth.getUid())
                .collection("referrals").document("referredBy");

        Map<String, Object> referredBy = new HashMap<>();

        referredBy.put("amountEarned", "250");
        referredBy.put("refereeUid", uid);
        referredBy.put("paid", false);

        refereeReferralRef.set(referredBy);

        addReferalInfoToReferee();
    }

    private void addReferalInfoToReferee() {
        final DocumentReference refereeOverviewRef = db.collection("users").document(uid)
                .collection("referrals").document("overview");

        refereeOverviewRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("numberOfReferrals") != null) {
                    numberOfReferrals = Integer.valueOf(documentSnapshot.getString("numberOfReferrals"));
                    numberOfReferrals = numberOfReferrals + 1;
                    refereeOverviewRef.update("numberOfReferrals", String.valueOf(numberOfReferrals));
                } else {
                    int numberOfReferrals = 1;
                    refereeOverviewRef.update("numberOfReferrals", String.valueOf(numberOfReferrals));
                }
                if (documentSnapshot.getString("amountEarned") != null) {
                    int amountEarned = Integer.valueOf(documentSnapshot.getString("amountEarned"));
                    amountEarned = amountEarned + 250;
                    refereeOverviewRef.update("amountEarned", String.valueOf(amountEarned));
                } else {
                    int amountEarned = 250;
                    refereeOverviewRef.update("amountEarned", String.valueOf(amountEarned));
                }
            }
        });

        String referralNumber = "Referral ".concat(String.valueOf(numberOfReferrals));
        final DocumentReference refereeReferralRef = db.collection("users").document(uid)
                .collection("referrals").document(referralNumber);

        Map<String, Object> referredBy = new HashMap<>();

        referredBy.put("amountEarned", "250");
        referredBy.put("referredUid", mAuth.getUid());
        referredBy.put("paid", false);

        refereeReferralRef.set(referredBy);
    }

    private void createToken() {
        stripe.createToken(cardToSave, new TokenCallback() {
            @Override
            public void onSuccess(Token token) {
                // Send token to your server
//                Toast.makeText(CourseApplicationPaymentActivity.this, "createToken - onSuccess", Toast.LENGTH_LONG).show();
                tokenRef.set(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(CourseApplicationPaymentActivity.this, "Token successfully added to database", Toast.LENGTH_LONG).show();
                        createCharge();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CourseApplicationPaymentActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
            }
        });
    }

    private void createCharge() {
        DocumentReference chargeRef;
        chargeRef = db.collection("stripe_customers").document(mAuth.getUid()).collection("charges").document();
        Map<String, Object> charge = new HashMap<>();

        charge.put("amount", payableSemesterAmount * 100);
        charge.put("currency", "aud");
        chargeRef.set(charge).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CourseApplicationPaymentActivity.this, "Payment successful", Toast.LENGTH_LONG).show();
                loadSuccessfulPayment();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CourseApplicationPaymentActivity.this, "Payment failed - " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.INVISIBLE); //To Hide ProgressBar
    }

    private void loadSuccessfulPayment() {
        try {
            CollectionReference chargeCollectionRef;
            chargeCollectionRef = db.collection("stripe_customers").document(mAuth.getUid()).collection("charges");
            chargeCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        int numberOfDocs = queryDocumentSnapshots.getDocuments().size();
                        double amountPaid = 0;
                        for (int i = 0; i < numberOfDocs; i++) {
                            if (queryDocumentSnapshots.getDocuments().get(i).getString("status") != null) {
                                if (queryDocumentSnapshots.getDocuments().get(i).getString("status").matches("succeeded")) {
                                    amountPaid = amountPaid + queryDocumentSnapshots.getDocuments().get(i).getDouble("amount") / 100;
                                    successfullyPaid.setText("Successfully paid: $" + String.valueOf(amountPaid));
                                    successfullyPaid.setVisibility(View.VISIBLE);
//                                confirmButton.setEnabled(false);
                                }
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void fillCardFields() {
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CourseApplication courseApplication = documentSnapshot.toObject(CourseApplication.class);
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

                confirmButton.setText("Pay $" + fullSemesterFeeString);

                Double cashback = Double.valueOf(course.getCashback());
                Double cashbackSemester = (double) (course.getCashback() / (Double.valueOf(course.getDuration().substring(0, 1)) * 2));

                String cashbackString = formatter.format(cashback);
                String cashbackSemesterString = formatter.format(cashbackSemester);

                Double payable = Double.valueOf(course.getPayable());
                Double payableSemester = (double) (course.getPayable() / (Double.valueOf(course.getDuration().substring(0, 1)) * 2));
                payableSemesterAmount = course.getFullFee() / (Integer.valueOf(course.getDuration().substring(0, 1)) * 2);
                loadSuccessfulPayment();

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
                    payableSemesterAmount = payableSemesterAmount + Integer.valueOf(documentSnapshot.getString("additionalFee1"));
                    loadSuccessfulPayment();
                }
                if (documentSnapshot.getString("additionalFee2") != null) {
                    additionalFee2Tv.setText(documentSnapshot.getString("additionalFee2"));
                    additionalFee2Tv.setVisibility(View.VISIBLE);
                    payableSemesterAmount = payableSemesterAmount + Integer.valueOf(documentSnapshot.getString("additionalFee2"));
                    loadSuccessfulPayment();
                }
                if (documentSnapshot.getString("additionalFee3") != null) {
                    additionalFee3Tv.setText(documentSnapshot.getString("additionalFee3"));
                    additionalFee3Tv.setVisibility(View.VISIBLE);
                    payableSemesterAmount = payableSemesterAmount + Integer.valueOf(documentSnapshot.getString("additionalFee3"));
                    loadSuccessfulPayment();
                }
            }
        });
    }
}