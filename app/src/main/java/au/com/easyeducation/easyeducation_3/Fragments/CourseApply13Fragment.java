package au.com.easyeducation.easyeducation_3.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;

import au.com.easyeducation.easyeducation_3.R;

public class CourseApply13Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply13Fragment() {
        // Required empty public constructor
    }

    public static CourseApply13Fragment newInstance() {
        CourseApply13Fragment fragment = new CourseApply13Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText mHighestQualificationName;
    private EditText mHighestQualificationInstitution;

    private CountryCodePicker mQualificationCountry;

    private DocumentReference userRef;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_13, container, false);

        mHighestQualificationName = rootView.findViewById(R.id.courseApplyHighestQualification_Name_ET);
        mHighestQualificationInstitution = rootView.findViewById(R.id.courseApplyHighestQualification_Institute_ET);

        mQualificationCountry = rootView.findViewById(R.id.courseApplyHighestQualification_Country);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("highestQualificationName") != null) {
                    mHighestQualificationName.setText(documentSnapshot.getString("highestQualificationName"));
                }
                if (documentSnapshot.getString("highestQualificationInstitution") != null) {
                    mHighestQualificationInstitution.setText(documentSnapshot.getString("highestQualificationInstitution"));
                }
                if (documentSnapshot.get("highestQualificationCountry") != null) {
                    mQualificationCountry.setDefaultCountryUsingNameCode(documentSnapshot.getString("highestQualificationCountry"));
                    mQualificationCountry.resetToDefaultCountry();
                }
                else if (documentSnapshot.get("highestQualificationCountry") == null){
                    mQualificationCountry.setDefaultCountryUsingNameCode(documentSnapshot.getString("countryBirth"));
                    mQualificationCountry.resetToDefaultCountry();
                }
            }
        });

        mHighestQualificationName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mHighestQualificationName.length() > 0) {
                    userRef.update("highestQualificationName", mHighestQualificationName.getText().toString().trim());
                }
            }
        });

        mHighestQualificationInstitution.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mHighestQualificationInstitution.length() > 0) {
                    userRef.update("highestQualificationInstitution", mHighestQualificationInstitution.getText().toString().trim());
                }
            }
        });

        mQualificationCountry.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                userRef.update("highestQualificationCountry", mQualificationCountry.getSelectedCountryName());
                userRef.update("highestQualificationCountry", mQualificationCountry.getSelectedCountryNameCode());
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
