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

public class CourseApply1Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply1Fragment() {
        // Required empty public constructor
    }

    public static CourseApply1Fragment newInstance() {
        CourseApply1Fragment fragment = new CourseApply1Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText mName;
    private EditText mSurname;
    private EditText mDOB;

    String name;
    String surname;
    String fullname;
    String dob;

    private DocumentReference userRef;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private boolean isFemaleButtonPressed = false;
    private boolean isMaleButtonPressed = false;

    int mYear;
    int mMonth;
    int mDay;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_1, container, false);

        mName = rootView.findViewById(R.id.courseApplyNameET);
        mSurname = rootView.findViewById(R.id.courseApplySurnameET);
        mDOB = rootView.findViewById(R.id.courseApplyDOB_ET);

        final Button mCourseApplyMaleButton = rootView.findViewById(R.id.courseApplyMaleButton);
        final Button mCourseApplyFemaleButton = rootView.findViewById(R.id.courseApplyFemaleButton);

        final CountryCodePicker mCourseApplyCountry = rootView.findViewById(R.id.courseApplyCountry);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("name") != null) {
                    mName.setText(documentSnapshot.getString("name"));
                }
                if (documentSnapshot.getString("surname") != null) {
                    mSurname.setText(documentSnapshot.getString("surname"));
                }
                if (documentSnapshot.getString("dob") != null) {
                    mDOB.setText(documentSnapshot.getString("dob"));
                    mYear = Integer.valueOf(documentSnapshot.getString("dobYear"));
                    mMonth = Integer.valueOf(documentSnapshot.getString("dobMonth"));
                    mDay = Integer.valueOf(documentSnapshot.getString("dobDay"));
                }
                if (documentSnapshot.getString("gender") != null) {
                    if (documentSnapshot.getString("gender").matches("Male")) {
                        mCourseApplyMaleButton.performClick();
                    }
                    if (documentSnapshot.getString("gender").matches("Female")) {
                        mCourseApplyFemaleButton.performClick();
                    }
                }
            }
        });

        mName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mName.length() > 0) {
                    userRef.update("name", mName.getText().toString().trim());
                }
            }
        });

        mSurname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mName.length() > 0) {
                    userRef.update("surname", mSurname.getText().toString().trim());
                }
            }
        });

        mCourseApplyFemaleButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isFemaleButtonPressed = true;
                if (isMaleButtonPressed) {
                    mCourseApplyMaleButton.setBackgroundColor(getResources().getColor(R.color.white));
                    isMaleButtonPressed = false;
                }
                else {
                    mCourseApplyFemaleButton.setBackgroundColor(getResources().getColor(R.color.menu));
                    userRef.update("gender", "Female");
                }
                return false;
            }
        });

        mCourseApplyMaleButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isMaleButtonPressed = true;
                if (isFemaleButtonPressed) {
                    isFemaleButtonPressed = false;
                    mCourseApplyFemaleButton.setBackgroundColor(getResources().getColor(R.color.white));
                }
                else {
                    mCourseApplyMaleButton.setBackgroundColor(getResources().getColor(R.color.menu));
                    userRef.update("gender", "Male");
                }
                return false;
            }
        });

        mDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mYear == 0) {
                    Calendar calendar = Calendar.getInstance();
                    mYear = calendar.get(Calendar.YEAR);
                    mMonth = calendar.get(Calendar.MONTH);
                    mDay = calendar.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener,
                        mYear, mMonth - 1, mDay);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                dob = dayOfMonth + "/" + month + "/" + year;
                mDOB.setText(dob);
                userRef.update("dob", dob);

                userRef.update("dobYear", Integer.toString(year));
                userRef.update("dobMonth", Integer.toString(month));
                userRef.update("dobDay", Integer.toString(dayOfMonth));

                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
            }
        };

//        userRef.update("countryBirth", mCourseApplyCountry.getSelectedCountryName());

        mCourseApplyCountry.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                userRef.update("countryBirth", mCourseApplyCountry.getSelectedCountryName());
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
