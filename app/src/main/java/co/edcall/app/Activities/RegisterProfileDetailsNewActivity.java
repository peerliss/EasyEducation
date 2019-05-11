package co.edcall.app.Activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import co.edcall.app.Fragments.RegisterProfileCountryBirthFragment;
import co.edcall.app.Fragments.RegisterProfileCountryCitizenshipFragment;
import co.edcall.app.Fragments.RegisterProfileDOBFragment;
import co.edcall.app.Fragments.RegisterProfileInternationalFragment;
import co.edcall.app.Fragments.RegisterProfileNameFragment;
import co.edcall.app.Fragments.RegisterProfileNumberFragment;
import co.edcall.app.Fragments.RegisterProfileNumberVerifyFragment;
import co.edcall.app.R;

public class RegisterProfileDetailsNewActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private DocumentReference userRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_profile_details_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable gradient = getResources().getDrawable(R.drawable.gradient);
        getSupportActionBar().setBackgroundDrawable(gradient);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users").document(mAuth.getUid());

        fragmentManager = getSupportFragmentManager();
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            addFragment();
        }
    }

    public void addFragment() {
        Fragment fragment;

        switch (fragmentManager.getBackStackEntryCount()) {
            case 0:
                fragment = new RegisterProfileNameFragment();
                break;
            case 1:
                fragment = new RegisterProfileDOBFragment();
                break;
            case 2:
                fragment = new RegisterProfileNumberFragment();
                break;
            case 3:
                fragment = new RegisterProfileNumberVerifyFragment();
                break;
            case 4:
                fragment = new RegisterProfileInternationalFragment();
                break;
            case 5:
                fragment = new RegisterProfileCountryBirthFragment();
                break;
            case 6:
                fragment = new RegisterProfileCountryCitizenshipFragment();
                break;
            default:
                return;
        }

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.left_to_right, R.anim.right_to_left);
        fragmentTransaction.replace(R.id.profileDetails_fragment_container, fragment, "ProfileDetailsFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        // This is the black back button on the bottom of the screen
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}