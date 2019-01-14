package au.com.easyeducation.easyeducation_3.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import au.com.easyeducation.easyeducation_3.Adapter.AgentAdapter;
import au.com.easyeducation.easyeducation_3.Fragments.AgentFragment;
import au.com.easyeducation.easyeducation_3.Fragments.InstitutionFragment;
import au.com.easyeducation.easyeducation_3.Fragments.RecyclerFragment;
import au.com.easyeducation.easyeducation_3.Model.Agents;
import au.com.easyeducation.easyeducation_3.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button agentButton;
    Button instituteButton;
    SearchView searchView;
    RecyclerView agentRecyclerView;

    ArrayList<Agents> agentsList = new ArrayList<>();
    ArrayList<Agents> institutesList = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        agentButton = findViewById(R.id.agent_button);
        instituteButton = findViewById(R.id.institution_button);
        searchView = findViewById(R.id.searchView);
        agentRecyclerView = findViewById(R.id.main_recyclerView);

        agentButton.setPressed(true);

        inflateAgentList();
        inflateInstitutesList();
        agentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        agentButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (instituteButton.isPressed()) {
                    instituteButton.setPressed(false);
                }

                agentRecyclerView.swapAdapter(new AgentAdapter(getBaseContext(), agentsList), true);

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

                agentRecyclerView.swapAdapter(new AgentAdapter(getBaseContext(), institutesList), true);

                instituteButton.setPressed(true);
                return true;
            }
        });

    }

    private void inflateAgentList() {
        agentsList.add(new Agents("XY Migration",
                "@xymigration",
                "Migration for all international students to lodge their visa's on time and enrol  in other institutions. Migration for all international students to lodge their  visa's on time and enrol in other institutions. Migration for all international....",
                "7 Visa Services Available",
                "Open: 9am - 5pm today",
                15.7,
                11,
                4.0));

        agentsList.add(new Agents("XY Migration",
                "@xymigration",
                "Migration for all international students to lodge their visa's on time and enrol  in other institutions. Migration for all international students to lodge their  visa's on time and enrol in other institutions. Migration for all international....",
                "7 Visa Services Available",
                "Open: 9am - 5pm today",
                15.7,
                11,
                4.0));

        agentsList.add(new Agents("XY Migration",
                "@xymigration",
                "Migration for all international students to lodge their visa's on time and enrol  in other institutions. Migration for all international students to lodge their  visa's on time and enrol in other institutions. Migration for all international....",
                "7 Visa Services Available",
                "Open: 9am - 5pm today",
                15.7,
                11,
                4.0));

        agentsList.add(new Agents("XY Migration",
                "@xymigration",
                "Migration for all international students to lodge their visa's on time and enrol  in other institutions. Migration for all international students to lodge their  visa's on time and enrol in other institutions. Migration for all international....",
                "7 Visa Services Available",
                "Open: 9am - 5pm today",
                15.7,
                11,
                4.0));


        initAgentRecyclerView();
    }

    private void inflateInstitutesList() {
        institutesList.add(new Agents("Study University",
                "Username",
                "Migration for all international students to lodge their visa's on time and enrol  in other institutions. Migration for all international students to lodge their  visa's on time and enrol in other institutions. Migration for all international....",
                "7 Visa Services Available",
                "Open: 9am - 5pm today",
                15.7,
                11,
                4.0));

        institutesList.add(new Agents("Study University",
                "Username",
                "Migration for all international students to lodge their visa's on time and enrol  in other institutions. Migration for all international students to lodge their  visa's on time and enrol in other institutions. Migration for all international....",
                "7 Visa Services Available",
                "Open: 9am - 5pm today",
                15.7,
                11,
                4.0));

        institutesList.add(new Agents("Study University",
                "Username",
                "Migration for all international students to lodge their visa's on time and enrol  in other institutions. Migration for all international students to lodge their  visa's on time and enrol in other institutions. Migration for all international....",
                "7 Visa Services Available",
                "Open: 9am - 5pm today",
                15.7,
                11,
                4.0));

        institutesList.add(new Agents("Study University",
                "Username",
                "Migration for all international students to lodge their visa's on time and enrol  in other institutions. Migration for all international students to lodge their  visa's on time and enrol in other institutions. Migration for all international....",
                "7 Visa Services Available",
                "Open: 9am - 5pm today",
                15.7,
                11,
                4.0));

        institutesList.add(new Agents("Study University",
                "Username",
                "Migration for all international students to lodge their visa's on time and enrol  in other institutions. Migration for all international students to lodge their  visa's on time and enrol in other institutions. Migration for all international....",
                "7 Visa Services Available",
                "Open: 9am - 5pm today",
                15.7,
                11,
                4.0));

    }

    private void initAgentRecyclerView() {
        agentRecyclerView.setAdapter(new AgentAdapter(this, agentsList));
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
}
