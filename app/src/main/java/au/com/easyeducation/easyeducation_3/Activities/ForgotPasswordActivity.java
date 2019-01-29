package au.com.easyeducation.easyeducation_3.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import au.com.easyeducation.easyeducation_3.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText mEmailField;
    private Button mResetButton;

    private String mEmail;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmailField = findViewById(R.id.forgotPasswordEmail);
        mResetButton = findViewById(R.id.forgotPasswordResetButton);

        mAuth = FirebaseAuth.getInstance();
    }

    private void resetPassword() {
        if (!validateForm()) {
            return;
        }

        mAuth.sendPasswordResetEmail(mEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Password reset email successfully sent. Please check email to reset password.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        mEmail = mEmailField.getText().toString();
        if (TextUtils.isEmpty(mEmail)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        return valid;
    }

    public void onClick_resetPasswordButton(View view) {
        resetPassword();
    }
}
