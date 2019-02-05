package au.com.easyeducation.easyeducation_3.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import au.com.easyeducation.easyeducation_3.Fragments.RegisterProfileDOBFragment;
import au.com.easyeducation.easyeducation_3.R;

public class RegisterProfileDetailsActivityOld extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference userRef;

    private EditText mName;
    private EditText mSurname;
    private Button mNameNextButton;

    String name;
    String surname;
    String fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_profile_details_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registerProfileDetailsActivity, RegisterProfileDOBFragment.newInstance())
                    .commitNow();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        mName = findViewById(R.id.registerName);
        mSurname = findViewById(R.id.registerSurname);
        mNameNextButton = findViewById(R.id.registerNameNextButton);
    }

    private void updateName() {
        if (!validateName()) {
            Toast.makeText(getApplicationContext(), "Please check fields.",
                    Toast.LENGTH_LONG).show();
        } else {
            userRef.update("name", name);
            userRef.update("surname", surname);
            userRef.update("fullname", fullname);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registerProfileNameFragment, RegisterProfileDOBFragment.newInstance())
                    .commitNow();
        }
    }

    private boolean validateName() {
        boolean valid = true;

        name = mName.getText().toString().trim();
        surname = mSurname.getText().toString().trim();
        fullname = name + " " + surname;

        if (TextUtils.isEmpty(name)) {
            mName.setError("Required.");
            valid = false;
        } else {
            mName.setError(null);
        }

        if (TextUtils.isEmpty(surname)) {
            mSurname.setError("Required.");
            valid = false;
        } else {
            mSurname.setError(null);
        }

        return valid;
    }

//    public void onClick_registerNameNextButton(View view) {
//        updateName();
//    }
}