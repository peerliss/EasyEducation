package au.com.easyeducation.easyeducation_3.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import au.com.easyeducation.easyeducation_3.Model.User;
import au.com.easyeducation.easyeducation_3.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    private EditText mName;
    private EditText mSurname;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mPhone;
    private EditText mDOB;
    private Button registerButton;

    String name;
    String surname;
    String fullname;
    String mEmail;
    String mPassword;
    String phone;
    String dob;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private DatabaseReference mDatabase;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable gradient = getResources().getDrawable(R.drawable.gradient);
        getSupportActionBar().setBackgroundDrawable(gradient);

//        mName = findViewById(R.id.registerName);
//        mSurname = findViewById(R.id.registerSurname);
        mEmailField = findViewById(R.id.registerEmail);
        mPasswordField = findViewById(R.id.registerPassword);
//        mPhone = findViewById(R.id.registerPhone);
//        mDOB = findViewById(R.id.registerDOB);
        registerButton = findViewById(R.id.registerConfirmButton);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            Toast.makeText(getApplicationContext(), "Please check fields.",
                    Toast.LENGTH_LONG).show();
        } else {
            final User dummyUser = new User(
                    null,
                    null,
                    null,
                    mEmail,
                    null,
                    null
            );

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                db.collection("users").document(user.getUid()).set(dummyUser);

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean validateForm() {
        boolean valid = true;

//        name = mName.getText().toString().trim();
//        surname = mSurname.getText().toString().trim();
//        fullname = name + " " + surname;
        mEmail = mEmailField.getText().toString().trim();
        mPassword = mPasswordField.getText().toString().trim();
//        phone = mPhone.getText().toString().trim();
//        dob = mDOB.getText().toString().trim();

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

//        if (TextUtils.isEmpty(name)) {
//            mName.setError("Required.");
//            valid = false;
//        } else {
//            mName.setError(null);
//        }
//
//        if (TextUtils.isEmpty(surname)) {
//            mSurname.setError("Required.");
//            valid = false;
//        } else {
//            mSurname.setError(null);
//        }
//
//        if (TextUtils.isEmpty(phone)) {
//            mPhone.setError("Required.");
//            valid = false;
//        } else {
//            mPhone.setError(null);
//        }
//
//        if (dob == null || dob.length() != 8) {
//            mDOB.setError("Required.");
//            valid = false;
//        } else {
//            mDOB.setError(null);
//        }

        return valid;
    }

    public void onClick_registerConfirm(View view) {
        createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
    }

    public void onClick_registerLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
