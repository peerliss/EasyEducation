package au.com.easyeducation.easyeducation_3.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import au.com.easyeducation.easyeducation_3.Fragments.CourseApply10Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply11Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply12Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply13Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply14Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply15Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply16Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply17Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply18Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply19Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply1Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply20Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply21Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply22Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply23Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply2Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply3Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply4Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply5Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply6Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply7Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply8Fragment;
import au.com.easyeducation.easyeducation_3.Fragments.CourseApply9Fragment;
import au.com.easyeducation.easyeducation_3.Model.CourseApplication;
import au.com.easyeducation.easyeducation_3.Model.Institution;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApplicationNewActivity extends AppCompatActivity {

    private FrameLayout fragment_container;
    private Button nextButton;
    private ProgressBar applyCourseProgressBar;
    private int nextPressedTimes;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private String businessTypeString;
    private String instituteRefString;
    private String courseRefString;

    private DocumentReference userRef;
    private StorageReference signatureRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_application);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable gradient = getResources().getDrawable(R.drawable.gradient);
        getSupportActionBar().setBackgroundDrawable(gradient);


        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users").document(mAuth.getUid());

        firebaseStorage = FirebaseStorage.getInstance();
        signatureRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/signature.png");

        Intent intent = getIntent();

        businessTypeString = intent.getExtras().getString("businessType");
        instituteRefString = intent.getExtras().getString("businessRef");
        courseRefString = intent.getExtras().getString("courseRef");

        findViewById(R.id.container).setVisibility(View.GONE);

        fragment_container = findViewById(R.id.courseApply_fragment_container);
        nextButton = findViewById(R.id.courseApplicationNextButton);
        applyCourseProgressBar = findViewById(R.id.courseApplicationProgressBar);

        nextButton.setBackground(gradient);

//        nextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addFragment();
//            }
//        });

        fragmentManager = getSupportFragmentManager();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
//                Toast.makeText(CourseApplicationNewActivity.this, "Fragment count in backstack - " + fragmentManager.getBackStackEntryCount(), Toast.LENGTH_LONG).show();
                if (fragmentManager.getBackStackEntryCount() == 23) {
                    nextButton.setText("Apply");
                } else {
                    nextButton.setText("Next");
                }
            }
        });

        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            addFragment();
        }

        setResult(RESULT_OK, intent);
    }

    public void addFragment() {
        Fragment fragment;

        switch (fragmentManager.getBackStackEntryCount()) {
            case 0:
                fragment = new CourseApply1Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 1:
//                nextButton.setVisibility(View.VISIBLE);
                fragment = new CourseApply2Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 2:
//                nextButton.setVisibility(View.VISIBLE);
                fragment = new CourseApply8Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 3:
                fragment = new CourseApply3Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 4:
                fragment = new CourseApply4Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 5:
                fragment = new CourseApply9Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 6:
                fragment = new CourseApply10Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 7:
                fragment = new CourseApply5Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 8:
                fragment = new CourseApply11Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 9:
                fragment = new CourseApply12Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 10:
                fragment = new CourseApply13Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 11:
                fragment = new CourseApply14Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 12:
                fragment = new CourseApply15Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 13:
                fragment = new CourseApply16Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 14:
                fragment = new CourseApply17Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 15:
                fragment = new CourseApply18Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 16:
                fragment = new CourseApply19Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 17:
                fragment = new CourseApply20Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 18:
                fragment = new CourseApply21Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 19:
                fragment = new CourseApply22Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 20:
                fragment = new CourseApply23Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 21:
                fragment = new CourseApply6Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            case 22:
                fragment = new CourseApply7Fragment();
                applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() + 1);
                break;
            default:
                return;

        }

        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.left_to_right, R.anim.right_to_left);
        fragmentTransaction.replace(R.id.courseApply_fragment_container, fragment, "CourseApplyFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void completeApplication() {
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DocumentReference instituteRef = db.collection("institutions").document(instituteRefString);
                DocumentReference courseRef = instituteRef.collection("courses").document(courseRefString);

                instituteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userRef.update("institutionName", documentSnapshot.getString("name"));
                        userRef.update("institutionCricos", documentSnapshot.getString("cricos"));
                    }
                });

                courseRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userRef.update("courseName", documentSnapshot.getString("name"));
                        userRef.update("courseCode", documentSnapshot.getString("courseCode"));
                    }
                });

                CourseApplication courseApplication = documentSnapshot.toObject(CourseApplication.class);
                userRef.collection("Applications").document().set(courseApplication);
                instituteRef.collection("Applications").document().set(courseApplication);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // This is the black back button on the bottom of the screen
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            finish();
        } else {
            applyCourseProgressBar.setProgress(applyCourseProgressBar.getProgress() - 1);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_application, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Action settings", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.form_checklist) {
            Toast.makeText(this, "Back to checklist", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), CourseApplicationChecklistActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
//            Toast.makeText(this, "Home Pressed", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}