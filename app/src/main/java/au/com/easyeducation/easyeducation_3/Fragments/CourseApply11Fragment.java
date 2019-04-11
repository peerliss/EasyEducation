package au.com.easyeducation.easyeducation_3.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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

import java.util.Calendar;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationNewActivity;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApply11Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button nextButton;
    private boolean buttonSelected = false;

    public CourseApply11Fragment() {
        // Required empty public constructor
    }

    public static CourseApply11Fragment newInstance() {
        CourseApply11Fragment fragment = new CourseApply11Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    private Button vocationalButton;
    private Button tafeButton;
    private Button undergraduateButton;
    private Button postgraduateButton;
    private Button doctorateButton;

    private EditText mCourseName;
    private EditText mInstitutionName;
    private EditText mCommencementDate;

    private String commencementDate;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    int mYear;
    int mMonth;
    int mDay;

    private Drawable selectedBG;
    private Drawable unSelectedBG;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_11, container, false);

        vocationalButton = rootView.findViewById(R.id.courseApplyFurtherStudies_Vocational_Button);
        tafeButton = rootView.findViewById(R.id.courseApplyFurtherStudies_TAFE_Button);
        undergraduateButton = rootView.findViewById(R.id.courseApplyFurtherStudies_Undergraduate_Button);
        postgraduateButton = rootView.findViewById(R.id.courseApplyFurtherStudies_Postgraduate_Button);
        doctorateButton = rootView.findViewById(R.id.courseApplyFurtherStudies_Doctorate_Button);

        mCourseName = rootView.findViewById(R.id.courseApplyFurtherStudies_CourseName_ET);
        mInstitutionName = rootView.findViewById(R.id.courseApplyFurtherStudies_InstitutionName_ET);
        mCommencementDate = rootView.findViewById(R.id.courseApplyFurtherStudies_CommencementDate_ET);

        selectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_selected);
        unSelectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_unselected);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("furtherStudies") != null) {
                    if (documentSnapshot.getString("furtherStudies").matches("Vocational")) {
                        vocationalButton.performClick();
                    }
                    if (documentSnapshot.getString("furtherStudies").matches("TAFE")) {
                        tafeButton.performClick();
                    }
                    if (documentSnapshot.getString("furtherStudies").matches("Undergraduate")) {
                        undergraduateButton.performClick();
                    }
                    if (documentSnapshot.getString("furtherStudies").matches("Postgraduate")) {
                        postgraduateButton.performClick();
                    }
                    if (documentSnapshot.getString("furtherStudies").matches("Doctorate")) {
                        doctorateButton.performClick();
                    }
                    if (documentSnapshot.getString("furtherStudiesCourse") != null) {
                        mCourseName.setText(documentSnapshot.getString("furtherStudiesCourse"));
                    }
                    if (documentSnapshot.getString("furtherStudiesInstitution") != null) {
                        mInstitutionName.setText(documentSnapshot.getString("furtherStudiesInstitution"));
                    }
                    if (documentSnapshot.getString("furtherStudiesCommencementDate") != null) {
                        mCommencementDate.setText(documentSnapshot.getString("furtherStudiesCommencementDate"));
                        mYear = Integer.valueOf(documentSnapshot.getString("furtherStudiesCommencementDateYear"));
                        mMonth = Integer.valueOf(documentSnapshot.getString("furtherStudiesCommencementDateMonth"));
                        mDay = Integer.valueOf(documentSnapshot.getString("furtherStudiesCommencementDateDay"));
                    }
                }
            }
        });

        vocationalButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                vocationalButton.setBackground(selectedBG);

                userRef.update("furtherStudies", "Vocational");

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

        tafeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                tafeButton.setBackground(selectedBG);

                userRef.update("furtherStudies", "TAFE");

                return false;
            }
        });

        tafeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                tafeButton.setBackground(selectedBG);
            }
        });

        undergraduateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                undergraduateButton.setBackground(selectedBG);

                userRef.update("furtherStudies", "Undergraduate");

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

                userRef.update("furtherStudies", "Postgraduate");

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

        doctorateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                doctorateButton.setBackground(selectedBG);

                userRef.update("furtherStudies", "Doctorate");

                return false;
            }
        });

        doctorateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                doctorateButton.setBackground(selectedBG);
            }
        });

        mCourseName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mCourseName.length() > 0) {
                    userRef.update("furtherStudiesCourse", mCourseName.getText().toString().trim());
                }
            }
        });

        mInstitutionName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mInstitutionName.length() > 0) {
                    userRef.update("furtherStudiesInstitution", mCourseName.getText().toString().trim());
                }
            }
        });

        mCommencementDate.setOnClickListener(new View.OnClickListener() {
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

                commencementDate = dayOfMonth + "/" + month + "/" + year;
                mCommencementDate.setText(commencementDate);
                userRef.update("furtherStudiesCommencementDate", commencementDate);

                userRef.update("furtherStudiesCommencementDateYear", Integer.toString(year));
                userRef.update("furtherStudiesCommencementDateMonth", Integer.toString(month));
                userRef.update("furtherStudiesCommencementDateDay", Integer.toString(dayOfMonth));

                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
            }
        };

        nextButton = getActivity().findViewById(R.id.courseApplicationNextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFields()) {
                    return;
                }

                ((CourseApplicationNewActivity) getActivity()).addFragment(9);
            }
        });

        return rootView;
    }

    private boolean verifyDate() {
        Calendar today = Calendar.getInstance();

        if (today.get(Calendar.YEAR) == mYear) {
            if (today.get(Calendar.MONTH) + 1 == mMonth) {
                return today.get(Calendar.DAY_OF_MONTH) < mDay;
            }
            return today.get(Calendar.MONTH) + 1 <= mMonth;
        }

        return today.get(Calendar.YEAR) < mYear;
    }

    private boolean validateFields() {
        boolean valid = true;

        if (!buttonSelected) {
            Toast.makeText(getContext(), "Please select your english level", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (TextUtils.isEmpty(mCourseName.getText()) || mCourseName.length() == 0) {
            mCourseName.setError("Required.");
            valid = false;
        } else {
            mCourseName.setError(null);
        }

        if (TextUtils.isEmpty(mInstitutionName.getText()) || mInstitutionName.length() == 0) {
            mInstitutionName.setError("Required.");
            valid = false;
        } else {
            mInstitutionName.setError(null);
        }

        if (TextUtils.isEmpty(mCommencementDate.getText()) || mCommencementDate.length() == 0) {
            mCommencementDate.setError("Required.");
            valid = false;
        } else {
            mCommencementDate.setError(null);
        }

        if (!verifyDate()) {
            valid = false;
            Toast.makeText(getContext(), "Course commencement date cannot be in the past", Toast.LENGTH_LONG).show();
        }

        return valid;
    }

    private void unSelectAllButtons() {
        buttonSelected = true;

        vocationalButton.setBackground(unSelectedBG);
        tafeButton.setBackground(unSelectedBG);
        undergraduateButton.setBackground(unSelectedBG);
        postgraduateButton.setBackground(unSelectedBG);
        doctorateButton.setBackground(unSelectedBG);
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
