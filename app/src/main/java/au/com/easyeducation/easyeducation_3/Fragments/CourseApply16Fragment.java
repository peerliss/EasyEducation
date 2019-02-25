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
import android.widget.LinearLayout;

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

    private LinearLayout mMedicalLayout;
    private LinearLayout mLegalLayout;
    private LinearLayout mVisaLayout;
    private LinearLayout mAATLayout;
    private LinearLayout mOtherLayout;

    private EditText mMedicalIssues;
    private EditText mLegalIssues;
    private EditText mVisaIssues;
    private EditText mAATIssues;
    private EditText mOtherIssues;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_16, container, false);

        mMedicalLayout = rootView.findViewById(R.id.courseApplyMedicalIssues_Layout);
        mLegalLayout = rootView.findViewById(R.id.courseApplyLegalIssues_Layout);
        mVisaLayout = rootView.findViewById(R.id.courseApplyTakeVisaPhoto_Layout);
        mAATLayout = rootView.findViewById(R.id.courseApplyAATIssues_Layout);
        mOtherLayout = rootView.findViewById(R.id.courseApplyOtherIssues_Layout);

        mMedicalIssues = rootView.findViewById(R.id.courseApplyMedicalIssues_ET);
        mLegalIssues = rootView.findViewById(R.id.courseApplyLegalIssues_ET);
        mVisaIssues = rootView.findViewById(R.id.courseApplyVisaIssues_ET);
        mAATIssues = rootView.findViewById(R.id.courseApplyAATIssues_ET);
        mOtherIssues = rootView.findViewById(R.id.courseApplyOtherIssues_ET);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("issuesMedical") != null
                        && documentSnapshot.getString("issuesMedical").matches("Yes")) {
                    mMedicalLayout.setVisibility(View.VISIBLE);
                }
                if (documentSnapshot.getString("issuesLegal") != null
                        && documentSnapshot.getString("issuesLegal").matches("Yes")) {
                    mLegalLayout.setVisibility(View.VISIBLE);
                }
                if (documentSnapshot.getString("issuesVisa") != null
                        && documentSnapshot.getString("issuesVisa").matches("Yes")) {
                    mVisaLayout.setVisibility(View.VISIBLE);
                }
                if (documentSnapshot.getString("issuesAAT") != null
                        && documentSnapshot.getString("issuesAAT").matches("Yes")) {
                    mAATLayout.setVisibility(View.VISIBLE);
                }
                if (documentSnapshot.getString("issuesOther") != null
                        && documentSnapshot.getString("issuesOther").matches("Yes")) {
                    mOtherLayout.setVisibility(View.VISIBLE);
                }

                if (documentSnapshot.getString("issuesMedicalDetails") != null) {
                    mMedicalIssues.setText(documentSnapshot.getString("issuesMedicalDetails"));
                }
                if (documentSnapshot.getString("issuesLegalDetails") != null) {
                    mLegalIssues.setText(documentSnapshot.getString("issuesLegalDetails"));
                }
                if (documentSnapshot.getString("issuesVisaDetails") != null) {
                    mLegalIssues.setText(documentSnapshot.getString("issuesVisaDetails"));
                }
                if (documentSnapshot.getString("issuesAATDetails") != null) {
                    mLegalIssues.setText(documentSnapshot.getString("issuesAATDetails"));
                }
                if (documentSnapshot.getString("issuesOtherDetails") != null) {
                    mLegalIssues.setText(documentSnapshot.getString("issuesOtherDetails"));
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

        mVisaIssues.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mVisaIssues.length() > 0) {
                    userRef.update("issuesVisaDetails", mVisaIssues.getText().toString().trim());
                }
            }
        });

        mAATIssues.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mAATIssues.length() > 0) {
                    userRef.update("issuesAATDetails", mAATIssues.getText().toString().trim());
                }
            }
        });

        mOtherIssues.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mOtherIssues.length() > 0) {
                    userRef.update("issuesOtherDetails", mOtherIssues.getText().toString().trim());
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