package co.edcall.app.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

import co.edcall.app.Activities.MainActivity;
import co.edcall.app.Activities.RegisterProfileDetailsNewActivity;
import co.edcall.app.R;

public class RegisterProfileNameFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RegisterProfileNameFragment() {
        // Required empty public constructor
    }

    public static RegisterProfileNameFragment newInstance() {
        RegisterProfileNameFragment fragment = new RegisterProfileNameFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText mName;
    private EditText mSurname;
    private boolean hasReferralCode = false;
    private boolean isValid = true;

    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";

    String name;
    String surname;
    String fullname;

    private DocumentReference userRef;
    private DocumentReference referralRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.register_profile_name_fragment, container, false);

        mName = rootView.findViewById(R.id.registerName);
        mSurname = rootView.findViewById(R.id.registerSurname);

        final Button mNameNextButton = rootView.findViewById(R.id.registerNameNextButton);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());
        referralRef = db.collection("users").document(mAuth.getUid())
                .collection("referrals").document("overview");

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("name") != null) {
                    mName.setText(documentSnapshot.getString("name"));
                }
                if (documentSnapshot.getString("surname") != null) {
                    mSurname.setText(documentSnapshot.getString("surname"));
                }
            }
        });

        referralRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("referralCode") != null) {
                    hasReferralCode = true;
                }
            }
        });

        mNameNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateName()) {
                    Toast.makeText(getContext(), "Please check fields.",
                            Toast.LENGTH_LONG).show();
                } else {
                    userRef.update("name", name);
                    userRef.update("surname", surname);
                    userRef.update("fullname", fullname);

                    if (!hasReferralCode) {
                        String referralCode = createReferralCode();
                        userRef.update("referralCode", referralCode);
                        referralRef.update("referralCode", referralCode);
                    }

                    ((RegisterProfileDetailsNewActivity) getActivity()).addFragment();
                }
            }
        });

        mSurname.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            mNameNextButton.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        return rootView;
    }

    private String createReferralCode() {
//        String referralNameLowercase = name.toLowerCase().concat(surname.substring(0, 1).toLowerCase());
//        int randomNumber = new Random().nextInt((9999 - 1000) + 1) + 1000;
//        String randomNumberString = String.valueOf(randomNumber);
//        String referralCode = referralNameLowercase.concat(randomNumberString);

        String referralCode = randomAlphaNumeric(4);

        if (checkIfReferralCodeIsUnique(referralCode)) {
            return referralCode;
        } else {
            createReferralCode();
        }
        return null;
    }


    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    private boolean checkIfReferralCodeIsUnique(final String referralCode) {
        isValid = true;
        try {
            CollectionReference userCollectionRef;
            userCollectionRef = db.collection("users");
            userCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        int numberOfDocs = queryDocumentSnapshots.getDocuments().size();
                        for (int i = 0; i < numberOfDocs; i++) {
                            if (queryDocumentSnapshots.getDocuments().get(i).getString("referralCode") != null) {
                                if (queryDocumentSnapshots.getDocuments().get(i).getString("referralCode").matches(referralCode)) {
                                    isValid = false;
                                }
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        return isValid;
    }

    private boolean validateName() {
        boolean valid = true;

        name = mName.getText().toString().trim();
        surname = mSurname.getText().toString().trim();
        fullname = name + " " + surname;

        if (TextUtils.isEmpty(name)) {
            mName.setError("Required.");
            valid = false;
        } else {
            mName.setError(null);
        }

        if (TextUtils.isEmpty(surname)) {
            mSurname.setError("Required.");
            valid = false;
        } else {
            mSurname.setError(null);
        }

        return valid;
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

    private void onBackPressed() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}