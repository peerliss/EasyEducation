package au.com.easyeducation.easyeducation_3.Fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationNewActivity;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApply20Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button nextButton;

    public CourseApply20Fragment() {
        // Required empty public constructor
    }

    public static CourseApply20Fragment newInstance() {
        CourseApply20Fragment fragment = new CourseApply20Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText mCurrentQualificationName;
    private EditText mCurrentQualificationInstitution;

    private DocumentReference userRef;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_20, container, false);

        mCurrentQualificationName = rootView.findViewById(R.id.courseApplyCurrentQualification_Name_ET);
        mCurrentQualificationInstitution = rootView.findViewById(R.id.courseApplyCurrentQualification_Institute_ET);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("currentQualificationName") != null) {
                    mCurrentQualificationName.setText(documentSnapshot.getString("currentQualificationName"));
                }
                if (documentSnapshot.getString("currentQualificationInstitution") != null) {
                    mCurrentQualificationInstitution.setText(documentSnapshot.getString("currentQualificationInstitution"));
                }
            }
        });

        mCurrentQualificationName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mCurrentQualificationName.length() > 0) {
                    userRef.update("currentQualificationName", mCurrentQualificationName.getText().toString().trim());
                }
            }
        });

        mCurrentQualificationInstitution.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mCurrentQualificationInstitution.length() > 0) {
                    userRef.update("currentQualificationInstitution", mCurrentQualificationInstitution.getText().toString().trim());
                }
            }
        });

        nextButton = getActivity().findViewById(R.id.courseApplicationNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFields()) {
                    return;
                }
                ((CourseApplicationNewActivity) getActivity()).addFragment();
            }
        });

        return rootView;
    }

    private boolean validateFields() {
        boolean valid = true;

        if (TextUtils.isEmpty(mCurrentQualificationName.getText()) || mCurrentQualificationName.length() == 0) {
            mCurrentQualificationName.setError("Required.");
            valid = false;
        } else {
            mCurrentQualificationName.setError(null);
        }

        if (TextUtils.isEmpty(mCurrentQualificationInstitution.getText()) || mCurrentQualificationInstitution.length() == 0) {
            mCurrentQualificationInstitution.setError("Required.");
            valid = false;
        } else {
            mCurrentQualificationInstitution.setError(null);
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
