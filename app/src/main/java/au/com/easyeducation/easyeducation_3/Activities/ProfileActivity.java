package au.com.easyeducation.easyeducation_3.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import au.com.easyeducation.easyeducation_3.Model.Institution;
import au.com.easyeducation.easyeducation_3.R;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private DocumentReference instituteRef;
    private TextView profileName;
    private Institution institution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String businessTypeString = intent.getExtras().getString("businessType");
        String instituteRefString = intent.getExtras().getString("instituteRef");

        db = FirebaseFirestore.getInstance();

        if (businessTypeString.matches("Institution")) {
            instituteRef = db.collection("institutions").document(instituteRefString);
        }
        else if (businessTypeString.matches("Agent")) {
            instituteRef = db.collection("agents").document(instituteRefString);
        }

        profileName = findViewById(R.id.profileName);

        instituteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                institution = documentSnapshot.toObject(Institution.class);
                profileName.setText(institution.getName());
            }
        });
    }

}
