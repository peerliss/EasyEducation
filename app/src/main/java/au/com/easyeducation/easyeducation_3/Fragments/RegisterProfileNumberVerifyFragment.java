package au.com.easyeducation.easyeducation_3.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import au.com.easyeducation.easyeducation_3.Activities.RegisterProfileDetailsActivity;
import au.com.easyeducation.easyeducation_3.R;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class RegisterProfileNumberVerifyFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RegisterProfileNumberVerifyFragment() {
        // Required empty public constructor
    }

    public static RegisterProfileNumberVerifyFragment newInstance() {
        RegisterProfileNumberVerifyFragment fragment = new RegisterProfileNumberVerifyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    private EditText mNumber;

//    String number;

    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.register_profile_number_verify_fragment, container, false);

//        mNumber = rootView.findViewById(R.id.registerNumber);

//        Button mNameNextButton = rootView.findViewById(R.id.registerNumberNextButton);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

//        mNameNextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!validateNumber()) {
//                    Toast.makeText(getContext(), "Please check fields.",
//                            Toast.LENGTH_LONG).show();
//                }
//                else {
//                    userRef.update("number", number);
//
//                    ((RegisterProfileDetailsActivity) getActivity()).setCurrentItem(3, true);
//                }
//            }
//        });

        return rootView;
    }

//    private boolean validateNumber() {
//        boolean valid = true;
//
//        number = mNumber.getText().toString().trim();
//
//        if (TextUtils.isEmpty(number)) {
//            mNumber.setError("Required.");
//            valid = false;
//        } else {
//            mNumber.setError(null);
//        }
//
//        return valid;
//    }

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