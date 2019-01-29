package au.com.easyeducation.easyeducation_3.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import au.com.easyeducation.easyeducation_3.Model.Agent;
import au.com.easyeducation.easyeducation_3.R;

public class AgentRegisterActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mUsername;
    private EditText mDescription;
    private EditText mVisa;
    private EditText mHours;
    private EditText mDistance;
    private EditText mRating;
    private EditText mReviews;
    private Button mAgentRegisterButton;

    String name;
    String username;
    String description;
    String visa;
    String hours;
    Double distance;
    Double rating;
    Integer reviews;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mName = findViewById(R.id.agentRegisterName);
        mUsername = findViewById(R.id.agentRegisterUsername);
        mDescription = findViewById(R.id.agentRegisterDescription);
        mVisa = findViewById(R.id.agentRegisterVisa);
        mHours = findViewById(R.id.agentRegisterHours);
        mDistance = findViewById(R.id.agentRegisterDistance);
        mRating = findViewById(R.id.agentRegisterRating);
        mReviews = findViewById(R.id.agentRegisterReviews);
    }

    private void createAgent() {
//        name = mName.getText().toString().trim();
//        username = mUsername.getText().toString().trim();
//        description = mDescription.getText().toString().trim();
//        visa = mVisa.getText().toString().trim();
//        hours = mHours.getText().toString().trim();
//
//        if (!mDistance.getText().toString().isEmpty()) {
//            distance = Double.valueOf(mDistance.getText().toString().trim());
//        }
//
//        if (!mRating.getText().toString().isEmpty()) {
//            rating = Double.valueOf(mRating.getText().toString().trim());
//        }
//
//        if (!mReviews.getText().toString().isEmpty()) {
//            reviews = Integer.valueOf(mReviews.getText().toString().trim());
//        }

        if (!validateForm()) {
            Toast.makeText(getApplicationContext(), "Please check fields.",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Agent agent = new Agent(
                    name,
                    username,
                    description,
                    visa,
                    hours,
                    distance,
                    reviews,
                    rating
            );

            db.collection("agents").document().set(agent).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AgentRegisterActivity.this, "Agent successfully added", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        name = mName.getText().toString().trim();
        username = mUsername.getText().toString().trim();
        description = mDescription.getText().toString().trim();
        visa = mVisa.getText().toString().trim();
        hours = mHours.getText().toString().trim();

        if (!mDistance.getText().toString().isEmpty()) {
            distance = Double.valueOf(mDistance.getText().toString().trim());
        }

        if (!mRating.getText().toString().isEmpty()) {
            rating = Double.valueOf(mRating.getText().toString().trim());
        }

        if (!mReviews.getText().toString().isEmpty()) {
            reviews = Integer.valueOf(mReviews.getText().toString().trim());
        }


        //Validate Fields
        if (TextUtils.isEmpty(name) || mName.length() == 0) {
            mName.setError("Required.");
            valid = false;
        } else {
            mName.setError(null);
        }

        if (TextUtils.isEmpty(username) || mUsername.length() == 0) {
            mUsername.setError("Required.");
            valid = false;
        } else {
            mUsername.setError(null);
        }

        if (TextUtils.isEmpty(description) || mDescription.length() == 0) {
            mDescription.setError("Required.");
            valid = false;
        } else {
            mDescription.setError(null);
        }

        if (TextUtils.isEmpty(visa) || mVisa.length() == 0) {
            mVisa.setError("Required.");
            valid = false;
        } else {
            mVisa.setError(null);
        }

        if (TextUtils.isEmpty(hours) || mHours.length() == 0) {
            mHours.setError("Required.");
            valid = false;
        } else {
            mHours.setError(null);
        }

        if (distance == null || mDistance.length() == 0) {
            mDistance.setError("Required.");
            valid = false;
        } else {
            mDistance.setError(null);
        }

        if (rating == null || mRating.length() == 0) {
            mRating.setError("Required.");
            valid = false;
        } else {
            mRating.setError(null);
        }

        if (reviews == null || mReviews.length() == 0) {
            mReviews.setError("Required.");
            valid = false;
        } else {
            mReviews.setError(null);
        }

        return valid;
    }

    public void onClick_agentRegister(View view) {
        createAgent();
    }
}
