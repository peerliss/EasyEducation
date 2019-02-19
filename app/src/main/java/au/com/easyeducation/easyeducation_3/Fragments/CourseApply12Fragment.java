package au.com.easyeducation.easyeducation_3.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

import au.com.easyeducation.easyeducation_3.R;

public class CourseApply12Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply12Fragment() {
        // Required empty public constructor
    }

    public static CourseApply12Fragment newInstance() {
        CourseApply12Fragment fragment = new CourseApply12Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    private Button highSchoolButton;
    private Button vocationalButton;
    private Button undergraduateButton;
    private Button postgraduateButton;

    private Drawable selectedBG;
    private Drawable unSelectedBG;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_12, container, false);

        highSchoolButton = rootView.findViewById(R.id.courseApplyHighestQualification_HighSchool_Button);
        vocationalButton = rootView.findViewById(R.id.courseApplyHighestQualification_Vocational_Button);
        undergraduateButton = rootView.findViewById(R.id.courseApplyHighestQualification_Undergraduate_Button);
        postgraduateButton = rootView.findViewById(R.id.courseApplyHighestQualification_Postgraduate_Button);

        selectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_selected);
        unSelectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_unselected);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("highestQualification") != null) {
                    if (documentSnapshot.getString("highestQualification").matches("High School")) {
                        highSchoolButton.performClick();
                    }
                    if (documentSnapshot.getString("highestQualification").matches("Vocational")) {
                        vocationalButton.performClick();
                    }
                    if (documentSnapshot.getString("highestQualification").matches("Undergraduate")) {
                        undergraduateButton.performClick();
                    }
                    if (documentSnapshot.getString("highestQualification").matches("Postgraduate")) {
                        postgraduateButton.performClick();
                    }
                }
            }
        });

        highSchoolButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                highSchoolButton.setBackground(selectedBG);

                userRef.update("highestQualification", "High School");

                return false;
            }
        });

        highSchoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                highSchoolButton.setBackground(selectedBG);
            }
        });
        
        vocationalButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                vocationalButton.setBackground(selectedBG);

                userRef.update("highestQualification", "Vocational");

                return false;
            }
        });

        vocationalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                vocationalButton.setBackground(selectedBG);
            }
        });
        
        undergraduateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                undergraduateButton.setBackground(selectedBG);

                userRef.update("highestQualification", "Undergraduate");

                return false;
            }
        });

        undergraduateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                undergraduateButton.setBackground(selectedBG);
            }
        });

        postgraduateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                postgraduateButton.setBackground(selectedBG);

                userRef.update("highestQualification", "Postgraduate");

                return false;
            }
        });

        postgraduateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                postgraduateButton.setBackground(selectedBG);
            }
        });

        return rootView;
    }

    private void unSelectAllButtons() {
        highSchoolButton.setBackground(unSelectedBG);
        vocationalButton.setBackground(unSelectedBG);
        undergraduateButton.setBackground(unSelectedBG);
        postgraduateButton.setBackground(unSelectedBG);
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
