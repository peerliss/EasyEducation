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

    RecyclerView agentRecyclerView;
    AgentAdapter agentAdapter;
    Button agentButton;
    Button instituteButton;
    SearchView searchView;

    ArrayList<Agents> agentsList = new ArrayList<>();

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

        agentsList.add(0, new Agents("Name",
                "Username",
                "Description",
                "Visa",
                "Hours",
                24,
                11,
                4));

        agentsList.add(1, new Agents("Name",
                "Username",
                "Description",
                "Visa",
                "Hours",
                24,
                11,
                4));

        agentsList.add(1, new Agents("Name",
                "Username",
                "Description",
                "Visa",
                "Hours",
                24,
                11,
                4));

        agentsList.add(1, new Agents("Name",
                "Username",
                "Description",
                "Visa",
                "Hours",
                24,
                11,
                4));

        agentRecyclerView = findViewById(R.id.main_recyclerView);
        agentRecyclerView.setHasFixedSize(true);
        agentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        agentAdapter = new AgentAdapter(this, agentsList);
//        agentRecyclerView.setAdapter(agentAdapter);

        TextView agent_name = findViewById(R.id.agent_name);
        TextView agent_username = findViewById(R.id.agent_username);
        TextView agent_description = findViewById(R.id.agent_description);
        TextView agent_visa = findViewById(R.id.agent_visa);
        TextView agent_hours = findViewById(R.id.agent_hours);
        TextView agent_distance = findViewById(R.id.agent_distance);
        TextView agent_reviews = findViewById(R.id.agent_reviews);
        TextView agent_rating = findViewById(R.id.agent_rating);
        agentButton = findViewById(R.id.agent_button);
        instituteButton = findViewById(R.id.institution_button);
        searchView = findViewById(R.id.searchView);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.agent_institution_fragment, new AgentFragment());
        fragmentTransaction.add(R.id.agent_institution_fragment, new AgentFragment());
        fragmentTransaction.add(R.id.agent_institution_fragment, new AgentFragment());
        fragmentTransaction.add(R.id.agent_institution_fragment, new AgentFragment());
        fragmentTransaction.commit();

        agentButton.setPressed(true);

        searchView.setIconified(true);

        instituteButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (agentButton.isPressed()) {
                    agentButton.setPressed(false);
                }

                instituteButton.setPressed(true);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.agent_institution_fragment, new InstitutionFragment());
                fragmentTransaction.add(R.id.agent_institution_fragment, new InstitutionFragment());
                fragmentTransaction.add(R.id.agent_institution_fragment, new InstitutionFragment());
                fragmentTransaction.add(R.id.agent_institution_fragment, new InstitutionFragment());

                fragmentTransaction.commit();

                return true;
            }
        });

        agentButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (instituteButton.isPressed()) {
                    instituteButton.setPressed(false);
                }

                agentButton.setPressed(true);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.agent_institution_fragment, new AgentFragment());
                fragmentTransaction.add(R.id.agent_institution_fragment, new AgentFragment());
                fragmentTransaction.add(R.id.agent_institution_fragment, new AgentFragment());
                fragmentTransaction.add(R.id.agent_institution_fragment, new AgentFragment());

                fragmentTransaction.commit();

                return true;
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


    @SuppressLint("ClickableViewAccessibility")
    public void onClick_institution_button(View view) {
//        instituteButton.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (agentButton.isPressed()) {
//                    agentButton.setPressed(false);
//                }
//
//                instituteButton.setPressed(true);
//
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.agent_institution_fragment, new InstitutionFragment());
//                fragmentTransaction.commit();
//
//                return true;
//            }
//        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onClick_agent_button(View view) {
//        agentButton.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (instituteButton.isPressed()) {
//                    instituteButton.setPressed(false);
//                }
//
//                agentButton.setPressed(true);
//
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.agent_institution_fragment, new AgentFragment());
//                fragmentTransaction.commit();
//
//                return true;
//            }
//        });
    }

    public void onClick_profile_button(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }
}
