package au.com.easyeducation.easyeducation_3.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import au.com.easyeducation.easyeducation_3.R;

public class CourseApply2Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply2Fragment() {
        // Required empty public constructor
    }

    public static CourseApply2Fragment newInstance() {
        CourseApply2Fragment fragment = new CourseApply2Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText mStreet;
    private EditText mSuburb;
    private EditText mState;
    private EditText mPostCode;
    private EditText mEmail;
    private EditText mNumber;

    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_2, container, false);

        mStreet = rootView.findViewById(R.id.courseApplyStreet_ET);
        mSuburb = rootView.findViewById(R.id.courseApplySuburb_ET);
        mState = rootView.findViewById(R.id.courseApplyState_ET);
        mPostCode = rootView.findViewById(R.id.courseApplyPostCode_ET);

        mEmail = rootView.findViewById(R.id.courseApplyEmail_ET);
        mNumber = rootView.findViewById(R.id.courseApplyNumber_ET);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("street") != null) {
                    mStreet.setText(documentSnapshot.getString("street"));
                }
                if (documentSnapshot.getString("suburb") != null) {
                    mSuburb.setText(documentSnapshot.getString("suburb"));
                }
                if (documentSnapshot.getString("state") != null) {
                    mState.setText(documentSnapshot.getString("state"));
                }
                if (documentSnapshot.getString("postCode") != null) {
                    mPostCode.setText(documentSnapshot.getString("postCode"));
                }
                if (documentSnapshot.getString("email") != null) {
                    mEmail.setText(documentSnapshot.getString("email"));
                }
                if (documentSnapshot.getString("number") != null) {
                    mNumber.setText(documentSnapshot.getString("number"));
                }
            }
        });

        mStreet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mStreet.length() > 0) {
                    userRef.update("street", mStreet.getText().toString().trim());
                }
            }
        });

        mSuburb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mSuburb.length() > 0) {
                    userRef.update("suburb", mSuburb.getText().toString().trim());
                }
            }
        });

        mState.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mState.length() > 0) {
                    userRef.update("state", mState.getText().toString().trim());
                }
            }
        });

        mPostCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mPostCode.length() > 0) {
                    userRef.update("postCode", mPostCode.getText().toString().trim());
                }
            }
        });

        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mEmail.length() > 0) {
                    userRef.update("email", mEmail.getText().toString().trim());
                }
            }
        });

        mNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mNumber.length() > 0) {
                    userRef.update("number", mNumber.getText().toString().trim());
                }
            }
        });

        return rootView;
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
