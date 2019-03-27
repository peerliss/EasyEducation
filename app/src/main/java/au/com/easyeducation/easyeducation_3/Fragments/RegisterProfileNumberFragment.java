package au.com.easyeducation.easyeducation_3.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import au.com.easyeducation.easyeducation_3.Activities.RegisterProfileDetailsActivity;
import au.com.easyeducation.easyeducation_3.Activities.RegisterProfileDetailsNewActivity;
import au.com.easyeducation.easyeducation_3.R;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class RegisterProfileNumberFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RegisterProfileNumberFragment() {
        // Required empty public constructor
    }

    public static RegisterProfileNumberFragment newInstance() {
        RegisterProfileNumberFragment fragment = new RegisterProfileNumberFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText mNumber;
    private CountryCodePicker countryCodePicker;

    String countryCode;
    String number;

    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.register_profile_number_fragment, container, false);

        mNumber = rootView.findViewById(R.id.registerNumber);
        countryCodePicker = rootView.findViewById(R.id.registerNumberCountryCode);

        countryCodePicker.clearFocus();

        final Button mNumberNextButton = rootView.findViewById(R.id.registerNumberNextButton);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("number") != null) {
                    mNumber.setText(documentSnapshot.getString("number").substring(3));
                }
            }
        });

        mNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            mNumberNextButton.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        mNumberNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateNumber()) {
                    Toast.makeText(getContext(), "Please enter a valid mobile number.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    userRef.update("number", "+" + countryCode + number);

                    ((RegisterProfileDetailsNewActivity) getActivity()).addFragment();

                }
            }
        });

//        Toast.makeText(getContext(), "Number Fragment onCreateView", Toast.LENGTH_SHORT).show();

        return rootView;
    }

    private boolean validateNumber() {
        boolean valid = true;

        countryCode = countryCodePicker.getSelectedCountryCode();
        number = mNumber.getText().toString().trim();

        if (TextUtils.isEmpty(number) && number.length() >= 9) {
            mNumber.setError("Required.");
            valid = false;
        } else {
            mNumber.setError(null);
        }

        if (number.startsWith("0")) {
            number = number.substring(1);
        }

        return valid;
    }

    // TODO: Renumber method, update argument and hook method into UI event
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
        // TODO: Update argument type and number
        void onFragmentInteraction(Uri uri);
    }
}