package co.edcall.app.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.edcall.app.R;

public class ReferralActivity extends AppCompatActivity {

    private TextView mAmountEarned;
    private TextView mNumberOfReferrals;
    private TextView mSignupBonus;
    private TextView mReferralCodeTv;
    private TextView mPayoutPerReferral;
    private TextView mReferredPeople;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference userRef;
    private DocumentReference referralOverviewRef;
    private int numberOfReferrals;
    private String referralDoc = "Referral 1";
    private String numberOfReferralsString;
    private int amountEarned;
    private String amountEarnedString;
    private int referralLoadIndex = 1;
    private LinearLayout mAmountEarnedLayout;
    private String referralCodeString;
    private Uri shareLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.referral_fab);

        Drawable gradient = getResources().getDrawable(R.drawable.gradient, getTheme());
        getSupportActionBar().setBackgroundDrawable(gradient);

        BottomNavigationView bottomNavigationView = findViewById(R.id.referral_bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_bottom_navigation_referral);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_bottom_navigation_home:
                        Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent_main);
                        break;
                    case R.id.menu_bottom_navigation_referral:
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

        mAmountEarnedLayout = findViewById(R.id.referral_amountEarned_Layout);
        mAmountEarned = findViewById(R.id.referral_amountEarned_Tv);
        mNumberOfReferrals = findViewById(R.id.referral_amountEarned_numberOfReferrals_Tv);
        mSignupBonus = findViewById(R.id.referral_signUpBonus_Tv);
        mReferralCodeTv = findViewById(R.id.referral_referralCode_Tv);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        referralOverviewRef = db.collection("users").document(mAuth.getUid()).collection("referrals").document("overview");
        referralOverviewRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("amountEarned") != null) {
                    amountEarned = Integer.valueOf(documentSnapshot.getString("amountEarned"));
                    if (amountEarned > 0) {
                        mAmountEarnedLayout.setVisibility(View.VISIBLE);
                    }
                    amountEarnedString = "$" + documentSnapshot.getString("amountEarned");
                    mAmountEarned.setText(amountEarnedString);
                }
                if (documentSnapshot.getString("numberOfReferrals") != null) {
                    numberOfReferrals = Integer.valueOf(documentSnapshot.getString("numberOfReferrals"));
                    numberOfReferralsString = "Earned from " + String.valueOf(documentSnapshot.getString("numberOfReferrals") + " referrals");
                    if (numberOfReferrals == 1) {
                        numberOfReferralsString = "Earned from " + String.valueOf(documentSnapshot.getString("numberOfReferrals") + " referral");
                    }
                    mNumberOfReferrals.setText(numberOfReferralsString);
                    loadReferralDetails();
                }
            }
        });

        userRef = db.collection("users").document(mAuth.getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("referralCode") != null) {
                    referralCodeString = (documentSnapshot.getString("referralCode"));
                    mReferralCodeTv.setText(referralCodeString);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri link = generateContentLink();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_TEXT, link.toString());
//                intent.putExtra(Intent.EXTRA_TEXT, referralCodeString);
//                intent.putExtra(Intent.EXTRA_TEXT, "https://app.edcall.co/share/refer");
                intent.putExtra(Intent.EXTRA_TEXT, "https://promo.edcall.com.au/share/cashback");

                startActivity(Intent.createChooser(intent, "Share Link"));
            }
        });
    }

    private void loadReferralDetails() {
        referralDoc = "Referral" + String.valueOf(referralLoadIndex);
        userRef = db.collection("users").document(mAuth.getUid()).collection("referrals").document(referralDoc);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("numberOfReferrals") != null) {

                    referralLoadIndex++;
                    if (referralLoadIndex < numberOfReferrals) {
                        loadReferralDetails();
                    }
                }
            }
        });
    }

    private Uri generateContentLink() {
        String link = "https://promo.edcall.com.au/share/" + referralCodeString;

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://promo.edcall.com.au/share")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("co.edcall.app")
                                .setMinimumVersion(125)
                                .build())
                .buildDynamicLink();

        return dynamicLink.getUri();
    }
}
