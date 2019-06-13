package co.edcall.app.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
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

import co.edcall.app.Activities.CourseApplicationNewActivity;
import co.edcall.app.R;

public class CourseApply1Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Drawable selectedBackground;
    private Drawable unSelectedBackground;
    private Drawable gradient;
    private Drawable disabledGradient;

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

    private Button nextButton;

    String name;
    String surname;
    String fullname;
    String dob;

    private DocumentReference userRef;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private boolean isGenderSelected = false;

    int mYear;
    int mMonth;
    int mDay;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_1, container, false);
        View activityView = inflater.inflate(R.layout.activity_course_application, container, false);

        selectedBackground = getResources().getDrawable(R.drawable.profile_buttons_border_selected);
        unSelectedBackground = getResources().getDrawable(R.drawable.profile_buttons_border_unselected);

        mName = rootView.findViewById(R.id.courseApplyNameET);
        mSurname = rootView.findViewById(R.id.courseApplySurnameET);
        mDOB = rootView.findViewById(R.id.courseApplyDOB_ET);

        gradient = getResources().getDrawable(R.drawable.gradient);
        disabledGradient = getResources().getDrawable(R.drawable.disabled_gradient);

        nextButton = getActivity().findViewById(R.id.courseApplicationNextButton);

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
                        isGenderSelected = true;
                    }
                    if (documentSnapshot.getString("gender").matches("Female")) {
                        mCourseApplyFemaleButton.performClick();
                        isGenderSelected = true;
                    }
                }
                if (documentSnapshot.get("countryBirthCode") != null) {
                    mCourseApplyCountry.setDefaultCountryUsingNameCode(documentSnapshot.getString("countryBirthCode"));
                    mCourseApplyCountry.resetToDefaultCountry();
                }
            }
        });

        mName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mName.length() > 0) {
                    userRef.update("name", mName.getText().toString().trim());
                }
//                validateFields();
            }
        });

        mSurname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mSurname.length() > 0) {
                    userRef.update("surname", mSurname.getText().toString().trim());
                }
//                validateFields();
            }
        });

        mCourseApplyMaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGenderSelected = true;

                userRef.update("gender", "Male");

                mCourseApplyMaleButton.setBackground(selectedBackground);
                mCourseApplyFemaleButton.setBackground(unSelectedBackground);

                mCourseApplyMaleButton.setTextColor(getResources().getColor(R.color.white));
                mCourseApplyFemaleButton.setTextColor(getResources().getColor(android.R.color.black));
//                validateFields();
            }
        });

        mCourseApplyFemaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGenderSelected = true;

                userRef.update("gender", "Female");

                mCourseApplyMaleButton.setBackground(unSelectedBackground);
                mCourseApplyFemaleButton.setBackground(selectedBackground);

                mCourseApplyMaleButton.setTextColor(getResources().getColor(android.R.color.black));
                mCourseApplyFemaleButton.setTextColor(getResources().getColor(R.color.white));
//                validateFields();
            }
        });

        mDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mYear == 0) {
                    Calendar calendar = Calendar.getInstance();
                    mYear = calendar.get(Calendar.YEAR) - 18;
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
//                validateFields();
            }
        };

        mCourseApplyCountry.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                userRef.update("countryBirth", mCourseApplyCountry.getSelectedCountryName());
                userRef.update("countryBirthCode", mCourseApplyCountry.getSelectedCountryNameCode());
            }
        });

//        nextButton.setEnabled(false);
//        nextButton.setBackground(disabledGradient);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFields()) {
                    return;
                }
                ((CourseApplicationNewActivity) getActivity()).addFragment(1);
            }
        });

        return rootView;
    }

    private boolean verifyAge() {
        Calendar today = Calendar.getInstance();

        if (today.get(Calendar.YEAR) - mYear == 18) {
            if (today.get(Calendar.MONTH) + 1 == mMonth) {
                return today.get(Calendar.DAY_OF_MONTH) >= mDay;
            }
            return today.get(Calendar.MONTH) >= mMonth;
        }

        return today.get(Calendar.YEAR) - mYear >= 18;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean validateFields() {
        boolean valid = true;

        if (TextUtils.isEmpty(mName.getText()) || mName.length() == 0) {
            mName.setError("Required.");
            valid = false;
        } else {
            mName.setError(null);
        }

        if (TextUtils.isEmpty(mSurname.getText()) || mSurname.length() == 0) {
            mSurname.setError("Required.");
            valid = false;
        } else {
            mSurname.setError(null);
        }

        if (TextUtils.isEmpty(mDOB.getText())) {
            mDOB.setError("Required.");
            valid = false;
        } else {
            mDOB.setError(null);
        }

        if (!isGenderSelected) {
            valid = false;
            Toast.makeText(getContext(), "Please select Male or Female", Toast.LENGTH_SHORT).show();
        }

        if (!verifyAge()) {
            valid = false;
            Toast.makeText(getContext(), "Must be at least 18 years or older", Toast.LENGTH_LONG).show();
        }

        return valid;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void sendBack() {
        if (mListener != null) {
            Toast.makeText(getContext(), "sendBack", Toast.LENGTH_SHORT).show();
            mListener.onFragmentInteraction();
        }
        Toast.makeText(getContext(), "sendBack - mListener is null", Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
