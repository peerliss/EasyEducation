package au.com.easyeducation.easyeducation_3.Fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import au.com.easyeducation.easyeducation_3.R;

public class CourseApply16Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply16Fragment() {
        // Required empty public constructor
    }

    public static CourseApply16Fragment newInstance() {
        CourseApply16Fragment fragment = new CourseApply16Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    private EditText mMedicalIssues;
    private EditText mLegalIssues;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_16, container, false);

        mMedicalIssues = rootView.findViewById(R.id.courseApplyMedicalIssues_ET);
        mLegalIssues = rootView.findViewById(R.id.courseApplyLegalIssues_ET);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("issuesDetails") != null) {
                    if (documentSnapshot.getString("issuesMedicalDetails") != null) {
                        mMedicalIssues.setText(documentSnapshot.getString("issuesMedicalDetails"));
                    }
                    if (documentSnapshot.getString("issuesLegalDetails") != null) {
                        mLegalIssues.setText(documentSnapshot.getString("issuesLegalDetails"));
                    }
                }
            }
        });

        mMedicalIssues.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mMedicalIssues.length() > 0) {
                    userRef.update("issuesMedicalDetails", mMedicalIssues.getText().toString().trim());
                }
            }
        });

        mLegalIssues.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mLegalIssues.length() > 0) {
                    userRef.update("issuesLegalDetails", mLegalIssues.getText().toString().trim());
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