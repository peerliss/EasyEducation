package au.com.easyeducation.easyeducation_3.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
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

public class CourseApply5Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply5Fragment() {
        // Required empty public constructor
    }

    public static CourseApply5Fragment newInstance() {
        CourseApply5Fragment fragment = new CourseApply5Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    private Button ieltsButton;
    private Button pteButton;
    private Button toeflButton;

    private EditText mTestDate;
    private EditText mTestResults;
    private EditText mMainLanguage;

    private String testDate;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    int mYear;
    int mMonth;
    int mDay;

    private Drawable selectedBG;
    private Drawable unselectedBG;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_5, container, false);

        ieltsButton = rootView.findViewById(R.id.courseApplyIELTS_Button);
        pteButton = rootView.findViewById(R.id.courseApplyPTE_Button);
        toeflButton = rootView.findViewById(R.id.courseApplyTOEFL_Button);

        mTestDate = rootView.findViewById(R.id.courseApplyEnglishTestDate_ET);
        mTestResults = rootView.findViewById(R.id.courseApplyEnglishTestResults_ET);
        mMainLanguage = rootView.findViewById(R.id.courseApplyEnglishTestOtherLanguage_ET);

        selectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_selected);
        unselectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_unselected);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("englishTest") != null) {
                    if (documentSnapshot.getString("englishTest").matches("IELTS")) {
                        ieltsButton.performClick();
                    }
                    if (documentSnapshot.getString("englishTest").matches("PTE")) {
                        pteButton.performClick();
                    }
                    if (documentSnapshot.getString("englishTest").matches("TOEFL")) {
                        toeflButton.performClick();
                    }
                    if (documentSnapshot.getString("englishTestDate") != null) {
                        mTestResults.setText(documentSnapshot.getString("englishTestDate"));
                        mYear = Integer.valueOf(documentSnapshot.getString("englishTestYear"));
                        mMonth = Integer.valueOf(documentSnapshot.getString("englishTestMonth"));
                        mDay = Integer.valueOf(documentSnapshot.getString("englishTestDay"));
                    }
                    if (documentSnapshot.getString("englishTestResults") != null) {
                        mTestResults.setText(documentSnapshot.getString("englishTestResults"));
                    }
                }
                else {
                    ieltsButton.performClick();
                }
                if (documentSnapshot.getString("englishTestMainLanguageSpoken") != null) {
                    mMainLanguage.setText(documentSnapshot.getString("englishTestMainLanguageSpoken"));
                }
            }
        });

        ieltsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                ieltsButton.setBackground(selectedBG);

                userRef.update("englishTest", "IELTS");

                return false;
            }
        });

        ieltsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                ieltsButton.setBackground(selectedBG);
            }
        });

        pteButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                pteButton.setBackground(selectedBG);

                userRef.update("englishTest", "PTE");

                return false;
            }
        });

        pteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                pteButton.setBackground(selectedBG);
            }
        });

        toeflButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                toeflButton.setBackground(selectedBG);

                userRef.update("englishTest", "TOEFL");

                return false;
            }
        });

        toeflButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                toeflButton.setBackground(selectedBG);
            }
        });

        mTestResults.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mTestResults.length() > 0) {
                    userRef.update("englishTestResults", mTestResults.getText().toString().trim());
                }
            }
        });

        mMainLanguage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mMainLanguage.length() > 0) {
                    userRef.update("englishTestMainLanguageSpoken", mMainLanguage.getText().toString().trim());
                }
            }
        });

        mTestDate.setOnClickListener(new View.OnClickListener() {
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

                testDate = dayOfMonth + "/" + month + "/" + year;
                mTestDate.setText(testDate);
                userRef.update("englishTestDate", testDate);

                userRef.update("englishTestYear", Integer.toString(year));
                userRef.update("englishTestMonth", Integer.toString(month));
                userRef.update("englishTestDay", Integer.toString(dayOfMonth));

                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
            }
        };

        return rootView;
    }

    private void unSelectAllButtons() {
        ieltsButton.setBackground(unselectedBG);
        pteButton.setBackground(unselectedBG);
        toeflButton.setBackground(unselectedBG);
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
