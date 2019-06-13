package co.edcall.app.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import co.edcall.app.Model.User;
import co.edcall.app.R;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private static final int MY_REQUEST_CODE = 1;

    private EditText mEmailField;
    private EditText mPasswordField;

    private Button registerButton;

    String mEmail;
    String mPassword;

    private FirebaseAuth mAuth;

    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private AppUpdateManager appUpdateManager;
    private Activity currentActivtiy = this;
    private FirebaseUser user;
    private DocumentReference addReferredByCodeToBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailField = findViewById(R.id.registerEmail);
        mPasswordField = findViewById(R.id.registerPassword);

        progressBar = findViewById(R.id.registerProgressBar);

        registerButton = findViewById(R.id.registerConfirmButton);

        mPasswordField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            registerButton.performClick();
                            registerButton.setPressed(true);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    progressBar.setVisibility(View.VISIBLE);
                    createAccount(mEmail, mPassword);
                    hideKeyboardFrom(getApplicationContext(), v);
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                // Get deep link from result (may be null if no link is found)

                Uri deepLink = null;
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.getLink();
                }

                user = mAuth.getCurrentUser();
                if (user == null && deepLink != null && deepLink.getBooleanQueryParameter("referredby", false)) {
                    String refereeCode = deepLink.getQueryParameter("referredby");
                    createAnonymousAccountWithReferrerInfo(refereeCode);
                }
            }
        });
    }

    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void updateUI(FirebaseUser user) {
        if (user != null && !user.isAnonymous()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    private void createAccount(String email, String password) {
        final User dummyUser = new User(
                null,
                null,
                null,
                mEmail,
                null,
                null
        );

        if (user.isAnonymous()) {
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);

            FirebaseAuth.getInstance().getCurrentUser()
                    .linkWithCredential(credential)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            // Complete any post sign-up tasks here.
                            progressBar.setVisibility(View.GONE);

                            addReferredByCodeToBase.update("email", mEmail);

                            Intent intent = new Intent(getApplicationContext(), RegisterProfileDetailsNewActivity.class);
                            startActivity(intent);
                        }
                    });
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            db.collection("users").document(user.getUid()).set(dummyUser);
//                            db.collection("stripe_customers").document(user.getUid()).set(dummyUser);
                            addReferralStructure();
                            progressBar.setVisibility(View.GONE);

                            Intent intent = new Intent(getApplicationContext(), RegisterProfileDetailsNewActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void addReferralStructure() {
        Map<String, Object> referralStructure = new HashMap<>();
        referralStructure.put("numberOfReferrals", "0");
        referralStructure.put("amountEarned", "0");

        DocumentReference referralRef = db.collection("users").document(mAuth.getUid())
                .collection("referrals").document("overview");

        referralRef.set(referralStructure);
    }

    private boolean validateForm() {
        boolean valid = true;

        mEmail = mEmailField.getText().toString().trim();
        mPassword = mPasswordField.getText().toString().trim();

        if (TextUtils.isEmpty(mEmail)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPassword)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    public void onClick_registerLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void createAnonymousAccountWithReferrerInfo(final String refereeCode) {
        FirebaseAuth.getInstance().signInAnonymously().addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                DocumentReference addReferredByCode = FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(currentUser.getUid())
                        .collection("referrals")
                        .document("overview");

                addReferredByCode.update("referredBy", refereeCode);

                addReferredByCodeToBase = FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(currentUser.getUid());

                addReferredByCodeToBase.update("referredBy", refereeCode);
            }
        });
    }
}
