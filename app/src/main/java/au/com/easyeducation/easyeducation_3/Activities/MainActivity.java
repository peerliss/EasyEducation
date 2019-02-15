package au.com.easyeducation.easyeducation_3.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import au.com.easyeducation.easyeducation_3.Adapter.FirestoreAgentAdapter;
import au.com.easyeducation.easyeducation_3.Adapter.FirestoreInstitutionAdapter;
import au.com.easyeducation.easyeducation_3.Model.Agent;
import au.com.easyeducation.easyeducation_3.Model.Institution;
import au.com.easyeducation.easyeducation_3.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DatabaseReference mDatabase;
    private CollectionReference agentRef;
    private CollectionReference institutionRef;

    private FirestoreAgentAdapter firestoreAgentAdapter;
    private FirestoreInstitutionAdapter firestoreInstitutionAdapter;

    Button agentButton;
    Button instituteButton;
    //    SearchView searchView;
    RecyclerView agentRecyclerView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Drawable gradient = getResources().getDrawable(R.drawable.gradient);
        getSupportActionBar().setBackgroundDrawable(gradient);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        agentRef = db.collection("agents");
        institutionRef = db.collection("institutions");

        agentButton = findViewById(R.id.agent_button);
        instituteButton = findViewById(R.id.institution_button);
//        searchView = findViewById(R.id.searchView);
        agentRecyclerView = findViewById(R.id.main_recyclerView);

//        searchView.clearFocus();
//        agentButton.setPressed(true);
        instituteButton.setPressed(true);

        agentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupAgentFirestoreRecyclerView();
        setupInstitutionFirestoreRecyclerView();

        agentButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (instituteButton.isPressed()) {
                    instituteButton.setPressed(false);
                }

                agentRecyclerView.setAdapter(firestoreAgentAdapter);

                agentButton.setPressed(true);
                return true;
            }
        });

        instituteButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (agentButton.isPressed()) {
                    agentButton.setPressed(false);
                }

                agentRecyclerView.setAdapter(firestoreInstitutionAdapter);

                instituteButton.setPressed(true);
                return true;
            }
        });

//        cloneDocument();
    }

    private void setupAgentFirestoreRecyclerView() {
        Query query = agentRef;

        FirestoreRecyclerOptions<Agent> options = new FirestoreRecyclerOptions.Builder<Agent>()
                .setQuery(query, Agent.class)
                .build();

        firestoreAgentAdapter = new FirestoreAgentAdapter(options);

        firestoreAgentAdapter.setOnItemClickListener(new FirestoreAgentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentReference documentReference) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("businessType", "Agent");
                intent.putExtra("businessRef", documentReference.getId());
                startActivity(intent);
            }
        });
    }

    private void setupInstitutionFirestoreRecyclerView() {
        Query query = institutionRef;

        FirestoreRecyclerOptions<Institution> options = new FirestoreRecyclerOptions.Builder<Institution>()
                .setQuery(query, Institution.class)
                .build();

        firestoreInstitutionAdapter = new FirestoreInstitutionAdapter(options);

        agentRecyclerView.setAdapter(firestoreInstitutionAdapter);

        firestoreInstitutionAdapter.setOnItemClickListener(new FirestoreInstitutionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentReference documentReference) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("businessType", "Institution");
                intent.putExtra("businessRef", documentReference.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        firestoreAgentAdapter.startListening();
        firestoreInstitutionAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        firestoreAgentAdapter.stopListening();
        firestoreInstitutionAdapter.stopListening();
    }

    // Clone a document in Firestore
    private void cloneDocument() {
        agentRef.document("Agent1").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Institution institution = documentSnapshot.toObject(Institution.class);
                for (int i = 1; i < 6; i++) {
                    String institutionName = "Institution " + i;
                    institution.setName(institutionName);
                    if (institution != null) {
//                        institutionRef.document(institutionName).set(institution);
                        institutionRef.document().set(institution);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick_profile_button(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

    public void onClick_signOut(MenuItem item) {
        mAuth.signOut();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void onClick_createAgent(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), AgentRegisterActivity.class);
        startActivity(intent);
    }

    public void onClick_enterProfileDetails(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), RegisterProfileDetailsActivity.class);
        startActivity(intent);
    }

    public void onClick_courseApplyForm(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), CourseApplicationChecklistActivity.class);
        startActivity(intent);
    }
}
