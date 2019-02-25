package au.com.easyeducation.easyeducation_3.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import au.com.easyeducation.easyeducation_3.Adapter.FirestoreAgentAdapter;
import au.com.easyeducation.easyeducation_3.Adapter.FirestoreCourseAdapter;
import au.com.easyeducation.easyeducation_3.Adapter.FirestoreInstitutionAdapter;
import au.com.easyeducation.easyeducation_3.Model.Agent;
import au.com.easyeducation.easyeducation_3.Model.Course;
import au.com.easyeducation.easyeducation_3.Model.Institution;
import au.com.easyeducation.easyeducation_3.R;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private DocumentReference instituteRef;
    private CollectionReference coursesListRef;
    private CollectionReference coursesRef;

    private TextView profileName;
    private TextView profileUsername;
    private TextView profileDescription;
    private TextView profileCricos;
    private Button profileDescriptionButton;
    private Button profileCoursesButton;

    private View descriptionLayout;
    private RecyclerView coursesRecyclerView;

    private Institution institution;

    private String instituteRefString;
    private String businessTypeString;

    private FirestoreCourseAdapter firestoreCourseAdapter;
    private String courseRefString;

    private Drawable selectedBG;
    private Drawable unselectedBG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable gradient = getResources().getDrawable(R.drawable.gradient);
        getSupportActionBar().setBackgroundDrawable(gradient);

        Intent intent = getIntent();

        db = FirebaseFirestore.getInstance();

        if (intent.getExtras() != null) {
            businessTypeString = intent.getExtras().getString("businessType");
            instituteRefString = intent.getExtras().getString("businessRef");
        } else {
            businessTypeString = "Institution";
            instituteRefString = "Institution 1";
            Toast.makeText(this, "Invalid Institution Selection", Toast.LENGTH_SHORT).show();
        }

        if (businessTypeString.matches("Institution")) {
            instituteRef = db.collection("institutions").document(instituteRefString);
            coursesListRef = instituteRef.collection("courses");
        } else if (businessTypeString.matches("Agent")) {
            instituteRef = db.collection("agents").document(instituteRefString);
        }

        profileName = findViewById(R.id.profileName);
        profileUsername = findViewById(R.id.profileUsername);
        profileDescription = findViewById(R.id.profileDescriptionTextView);
        profileCricos = findViewById(R.id.profileCricos);

        profileDescriptionButton = findViewById(R.id.profileDescriptionButton);
        profileCoursesButton = findViewById(R.id.profileCoursesButton);

        descriptionLayout = findViewById(R.id.profileDescription_layout);
        coursesRecyclerView = findViewById(R.id.profileCourses_recyclerView);

        instituteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                institution = documentSnapshot.toObject(Institution.class);
                profileName.setText(institution.getName());
                profileUsername.setText(institution.getUsername());
                profileDescription.setText(institution.getDescription());
                profileCricos.setText("CRICOS NO: " + institution.getCricos());
            }
        });

        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupCoursesRecyclerView();

        selectedBG = getDrawable(R.drawable.profile_buttons_square_border_selected);
        unselectedBG = getDrawable(R.drawable.profile_buttons_square_border_unselected);

//        cloneDocument();
    }

    private void setupCoursesRecyclerView() {
        Query query = coursesListRef;

        FirestoreRecyclerOptions<Course> options = new FirestoreRecyclerOptions.Builder<Course>()
                .setQuery(query, Course.class)
                .build();

        firestoreCourseAdapter = new FirestoreCourseAdapter(options);

        coursesRecyclerView.setAdapter(firestoreCourseAdapter);

        firestoreCourseAdapter.setOnItemClickListener(new FirestoreCourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentReference documentReference) {
                courseRefString = documentReference.getId();

                Intent intent = new Intent(getApplicationContext(), CollegeCourseInformationActivity.class);
                intent.putExtra("businessType", "institutions");
                intent.putExtra("businessRef", instituteRefString);
                intent.putExtra("courseRef", courseRefString);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        firestoreCourseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        firestoreCourseAdapter.stopListening();
    }

    public void onClick_profileDescriptionButton(View view) {
        descriptionLayout.setVisibility(View.VISIBLE);
        coursesRecyclerView.setVisibility(View.GONE);

        profileDescriptionButton.setBackground(selectedBG);
        profileDescriptionButton.setTextColor(getResources().getColor(R.color.white));

        profileCoursesButton.setBackground(unselectedBG);
        profileCoursesButton.setTextColor(getResources().getColor(android.R.color.black));
    }

    public void onClick_profileCoursesButton(View view) {
        descriptionLayout.setVisibility(View.GONE);
        coursesRecyclerView.setVisibility(View.VISIBLE);

        profileCoursesButton.setBackground(selectedBG);
        profileCoursesButton.setTextColor(getResources().getColor(R.color.white));

        profileDescriptionButton.setBackground(unselectedBG);
        profileDescriptionButton.setTextColor(getResources().getColor(android.R.color.black));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                businessTypeString = data.getStringExtra("businessType");
                instituteRefString = data.getStringExtra("businessRef");

                profileCoursesButton.performClick();
            }
        }
    }

    // Clone a document in Firestore
    private void cloneDocument() {
        coursesListRef.document("HLT54115").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Course course = documentSnapshot.toObject(Course.class);
                for (int i = 1; i < 6; i++) {
                    String courseName = "Course " + i;
                    course.setName(courseName);
                    if (course != null) {
//                        coursesListRef.document(courseName).set(course);
//                        coursesListRef.document().set(course);
                        coursesRef = db.collection("institutions");

                        coursesRef.document("S7QXgGTEG96vHexjhJ03")
                                .collection("courses")
                                .document("TEST").set(course);
//                        instituteRef.collection("courses").document().set(course);
                    }
                }
            }
        });
    }
}
