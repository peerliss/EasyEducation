package co.edcall.app.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import co.edcall.app.Adapter.FirestoreInstitutionAdapter;
import co.edcall.app.Model.Institution;
import co.edcall.app.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DatabaseReference mDatabase;
    private CollectionReference institutionRef;

    private FirestoreInstitutionAdapter firestoreInstitutionAdapter;

    Button instituteButton;
    //    SearchView searchView;
    RecyclerView institutionRecyclerView;

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

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        institutionRef = db.collection("institutions");

        instituteButton = findViewById(R.id.institution_button);
//        searchView = findViewById(R.id.searchView);
        institutionRecyclerView = findViewById(R.id.main_recyclerView);

//        searchView.clearFocus();

        instituteButton.setPressed(true);

        institutionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupInstitutionFirestoreRecyclerView();

        BottomNavigationView bottomNavigationView = findViewById(R.id.main_bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_bottom_navigation_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_bottom_navigation_home:
                        break;
                    case R.id.menu_bottom_navigation_referral:
                        Intent intent_referral = new Intent(getApplicationContext(), ReferralActivity.class);
                        startActivity(intent_referral);
                        break;
                    case R.id.menu_bottom_navigation_course:
                        Intent intent_course = new Intent(getApplicationContext(), CourseApplicationStatusActivity.class);
                        startActivity(intent_course);
                        break;
                    case R.id.menu_bottom_navigation_support:
                        Intent intent_support = new Intent(getApplicationContext(), SupportActivity.class);
                        startActivity(intent_support);
                        break;
                }

                return false;
            }
        });

//        cloneDocument();
    }

    private void setupInstitutionFirestoreRecyclerView() {
        Query query = institutionRef;

        FirestoreRecyclerOptions<Institution> options = new FirestoreRecyclerOptions.Builder<Institution>()
                .setQuery(query, Institution.class)
                .build();

        firestoreInstitutionAdapter = new FirestoreInstitutionAdapter(options);

        institutionRecyclerView.setAdapter(firestoreInstitutionAdapter);

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

//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);

        firestoreInstitutionAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        firestoreInstitutionAdapter.stopListening();
    }

    // Clone a document in Firestore
    private void cloneDocument() {
        institutionRef.document("Institution 1").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

//    private void updateUI(FirebaseUser user) {
//        if (user != null) {
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//        }
//    }

    public void onClick_profile_button(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

    public void onClick_signOut(MenuItem item) {
        mAuth.signOut();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void onClick_enterProfileDetails(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), RegisterProfileDetailsNewActivity.class);
        startActivity(intent);
    }

    public void onClick_courseApplyForm(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), CourseApplicationChecklistActivity.class);
        startActivity(intent);
    }
}
