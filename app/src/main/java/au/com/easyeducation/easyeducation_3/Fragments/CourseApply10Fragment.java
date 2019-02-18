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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import au.com.easyeducation.easyeducation_3.R;

public class CourseApply10Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply10Fragment() {
        // Required empty public constructor
    }

    public static CourseApply10Fragment newInstance() {
        CourseApply10Fragment fragment = new CourseApply10Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    private Button beginnerButton;
    private Button elementaryButton;
    private Button preIntermediateButton;
    private Button intermediateButton;
    private Button upperIntermediateButton;
    private Button advancedButton;

    private Drawable selectedBG;
    private Drawable unSelectedBG;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_10, container, false);

        beginnerButton = rootView.findViewById(R.id.courseApplyEnglish_Beginner_Button);
        elementaryButton = rootView.findViewById(R.id.courseApplyEnglish_Elementary_Button);
        preIntermediateButton = rootView.findViewById(R.id.courseApplyEnglish_PreIntermediate_Button);
        intermediateButton = rootView.findViewById(R.id.courseApplyEnglish_Intermediate_Button);
        upperIntermediateButton = rootView.findViewById(R.id.courseApplyEnglish_UpperIntermediate_Button);
        advancedButton = rootView.findViewById(R.id.courseApplyEnglish_Advanced_Button);

        selectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_selected);
        unSelectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_unselected);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("englishLevel") != null) {
                    if (documentSnapshot.getString("englishLevel").matches("Beginner")) {
                        beginnerButton.performClick();
                    }
                    if (documentSnapshot.getString("englishLevel").matches("Elementary")) {
                        elementaryButton.performClick();
                    }
                    if (documentSnapshot.getString("englishLevel").matches("Pre-Intermediate")) {
                        preIntermediateButton.performClick();
                    }
                    if (documentSnapshot.getString("englishLevel").matches("Intermediate")) {
                        intermediateButton.performClick();
                    }
                    if (documentSnapshot.getString("englishLevel").matches("Upper-Intermediate")) {
                        upperIntermediateButton.performClick();
                    }
                    if (documentSnapshot.getString("englishLevel").matches("Advanced")) {
                        advancedButton.performClick();
                    }
                }
            }
        });

        beginnerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                beginnerButton.setBackground(selectedBG);

                userRef.update("englishLevel", "Beginner");

                return false;
            }
        });

        beginnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                beginnerButton.setBackground(selectedBG);
            }
        });

        elementaryButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                elementaryButton.setBackground(selectedBG);

                userRef.update("englishLevel", "Elementary");

                return false;
            }
        });

        elementaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                elementaryButton.setBackground(selectedBG);
            }
        });

        preIntermediateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                preIntermediateButton.setBackground(selectedBG);

                userRef.update("englishLevel", "Pre-Intermediate");

                return false;
            }
        });

        preIntermediateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                preIntermediateButton.setBackground(selectedBG);
            }
        });

        intermediateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                intermediateButton.setBackground(selectedBG);

                userRef.update("englishLevel", "Intermediate");

                return false;
            }
        });

        intermediateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                intermediateButton.setBackground(selectedBG);
            }
        });

        upperIntermediateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                upperIntermediateButton.setBackground(selectedBG);

                userRef.update("englishLevel", "Upper-Intermediate");

                return false;
            }
        });

        upperIntermediateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                upperIntermediateButton.setBackground(selectedBG);
            }
        });

        advancedButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                advancedButton.setBackground(selectedBG);

                userRef.update("englishLevel", "Advanced");

                return false;
            }
        });

        advancedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                advancedButton.setBackground(selectedBG);
            }
        });

        return rootView;
    }

    private void unSelectAllButtons() {
        beginnerButton.setBackground(unSelectedBG);
        elementaryButton.setBackground(unSelectedBG);
        preIntermediateButton.setBackground(unSelectedBG);
        intermediateButton.setBackground(unSelectedBG);
        upperIntermediateButton.setBackground(unSelectedBG);
        advancedButton.setBackground(unSelectedBG);
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
