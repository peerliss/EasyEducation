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

public class RegisterProfileDOBFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RegisterProfileDOBFragment() {
        // Required empty public constructor
    }

    public static RegisterProfileDOBFragment newInstance() {
        RegisterProfileDOBFragment fragment = new RegisterProfileDOBFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText mDOB;

    String dob;

    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.register_profile_dob_fragment, container, false);

        mDOB = rootView.findViewById(R.id.registerDOB);
        Button mDOBNextButton = rootView.findViewById(R.id.registerDOBNextButton);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        mDOBNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateDOB()) {
                    Toast.makeText(getContext(), "Please check fields.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    userRef.update("dob", dob);
                    ((RegisterProfileDetailsActivity) getActivity()).setCurrentItem(2, true);
                }
            }
        });

        return rootView;
    }

    private boolean validateDOB() {
        boolean valid = true;

        dob = mDOB.getText().toString().trim();

        if (TextUtils.isEmpty(dob)) {
            mDOB.setError("Required.");
            valid = false;
        } else {
            mDOB.setError(null);
        }

        return valid;
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