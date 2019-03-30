package au.com.easyeducation.easyeducation_3.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationActivity;
import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationNewActivity;
import au.com.easyeducation.easyeducation_3.Activities.RegisterProfileDetailsActivity;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApply6Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button nextButton;
    private boolean buttonSelected = false;

    public CourseApply6Fragment() {
        // Required empty public constructor
    }

    public static CourseApply6Fragment newInstance() {
        CourseApply6Fragment fragment = new CourseApply6Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    private Button fullTimeButton;
    private Button partTimeButton;
    private Button casualButton;
    private Button unemployedButton;

    private Drawable selectedBG;
    private Drawable unSelectedBG;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_6, container, false);

        fullTimeButton = rootView.findViewById(R.id.courseApplyEmploymentFull_Button);
        partTimeButton = rootView.findViewById(R.id.courseApplyEmploymentPart_Button);
        casualButton = rootView.findViewById(R.id.courseApplyEmploymentCasual_Button);
        unemployedButton = rootView.findViewById(R.id.courseApplyEmploymentUnemployed_Button);

        selectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_selected);
        unSelectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_unselected);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("employmentStatus") != null) {
                    if (documentSnapshot.getString("employmentStatus").matches("Full Time")) {
                        fullTimeButton.performClick();
                    }
                    if (documentSnapshot.getString("employmentStatus").matches("Part Time")) {
                        partTimeButton.performClick();
                    }
                    if (documentSnapshot.getString("employmentStatus").matches("Casual")) {
                        casualButton.performClick();
                    }
                    if (documentSnapshot.getString("employmentStatus").matches("Unemployed")) {
                        unemployedButton.performClick();
                    }
                }
            }
        });

        fullTimeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                fullTimeButton.setBackground(selectedBG);

                userRef.update("employmentStatus", "Full Time");

                return false;
            }
        });

        fullTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                fullTimeButton.setBackground(selectedBG);
            }
        });

        partTimeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                partTimeButton.setBackground(selectedBG);

                userRef.update("employmentStatus", "Part Time");

                return false;
            }
        });

        partTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                partTimeButton.setBackground(selectedBG);
            }
        });

        casualButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                casualButton.setBackground(selectedBG);

                userRef.update("employmentStatus", "Casual");

                return false;
            }
        });

        casualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                casualButton.setBackground(selectedBG);
            }
        });

        unemployedButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                unemployedButton.setBackground(selectedBG);

                userRef.update("employmentStatus", "Unemployed");

                return false;
            }
        });

        unemployedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                unemployedButton.setBackground(selectedBG);
            }
        });

        nextButton = getActivity().findViewById(R.id.courseApplicationNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFields()) {
                    return;
                }
                ((CourseApplicationNewActivity) getActivity()).addFragment(22);
            }
        });

        return rootView;
    }

    private boolean validateFields() {
        boolean valid =  true;

        if (!buttonSelected) {
            Toast.makeText(getContext(), "Please select an option.", Toast.LENGTH_SHORT).show();
            valid =  false;
        }

        return valid;
    }

    private void unSelectAllButtons() {
        buttonSelected = true;

        fullTimeButton.setBackground(unSelectedBG);
        partTimeButton.setBackground(unSelectedBG);
        casualButton.setBackground(unSelectedBG);
        unemployedButton.setBackground(unSelectedBG);
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
