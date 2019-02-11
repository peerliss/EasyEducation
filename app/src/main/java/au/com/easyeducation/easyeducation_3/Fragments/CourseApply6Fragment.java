package au.com.easyeducation.easyeducation_3.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import au.com.easyeducation.easyeducation_3.Activities.RegisterProfileDetailsActivity;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApply6Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

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

    private boolean isFullTimeButtonPressed = false;
    private boolean isPartTimeButtonPressed = false;
    private boolean isCasualButtonPressed = false;
    private boolean isUnemployedButtonPressed = false;

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
                partTimeButton.setBackgroundColor(getResources().getColor(R.color.white));
                casualButton.setBackgroundColor(getResources().getColor(R.color.white));
                unemployedButton.setBackgroundColor(getResources().getColor(R.color.white));

                fullTimeButton.setBackgroundColor(getResources().getColor(R.color.menu));
                userRef.update("employmentStatus", "Full Time");

                return false;
            }
        });

        partTimeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fullTimeButton.setBackgroundColor(getResources().getColor(R.color.white));
                casualButton.setBackgroundColor(getResources().getColor(R.color.white));
                unemployedButton.setBackgroundColor(getResources().getColor(R.color.white));

                partTimeButton.setBackgroundColor(getResources().getColor(R.color.menu));

                userRef.update("employmentStatus", "Part Time");

                return false;
            }
        });

        casualButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                partTimeButton.setBackgroundColor(getResources().getColor(R.color.white));
                fullTimeButton.setBackgroundColor(getResources().getColor(R.color.white));
                unemployedButton.setBackgroundColor(getResources().getColor(R.color.white));

                casualButton.setBackgroundColor(getResources().getColor(R.color.menu));

                userRef.update("employmentStatus", "Casual");

                return false;
            }
        });

        unemployedButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                partTimeButton.setBackgroundColor(getResources().getColor(R.color.white));
                casualButton.setBackgroundColor(getResources().getColor(R.color.white));
                fullTimeButton.setBackgroundColor(getResources().getColor(R.color.white));

                unemployedButton.setBackgroundColor(getResources().getColor(R.color.menu));

                userRef.update("employmentStatus", "Unemployed");

                return false;
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
