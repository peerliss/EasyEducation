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

public class CourseApply9Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply9Fragment() {
        // Required empty public constructor
    }

    public static CourseApply9Fragment newInstance() {
        CourseApply9Fragment fragment = new CourseApply9Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText mVisaType;
    private EditText mVisaSubclass;
    private EditText mVisaExpiry;
    private EditText mVisaNumber;

    private String visaExpiryDate;

    private DocumentReference userRef;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    int mYear;
    int mMonth;
    int mDay;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_9, container, false);

        mVisaType = rootView.findViewById(R.id.courseApplyVisaType_ET);
        mVisaSubclass = rootView.findViewById(R.id.courseApplyVisaSubclass_ET);
        mVisaNumber = rootView.findViewById(R.id.courseApplyVisaNumber_ET);
        mVisaExpiry = rootView.findViewById(R.id.courseApplyVisaExpiry_ET);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("visaNumber") != null) {
                    mVisaNumber.setText(documentSnapshot.getString("visaNumber"));
                }
                if (documentSnapshot.getString("visaExpiryDate") != null) {
                    mVisaExpiry.setText(documentSnapshot.getString("visaExpiryDate"));
                    mYear = Integer.valueOf(documentSnapshot.getString("visaExpiryYear"));
                    mMonth = Integer.valueOf(documentSnapshot.getString("visaExpiryMonth"));
                    mDay = Integer.valueOf(documentSnapshot.getString("visaExpiryDay"));
                }
                if (documentSnapshot.getString("visaType") != null) {
                    mVisaType.setText(documentSnapshot.getString("visaType"));
                }
                if (documentSnapshot.getString("visaSubclass") != null) {
                    mVisaSubclass.setText(documentSnapshot.getString("visaSubclass"));
                }
            }
        });

        mVisaNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mVisaNumber.length() > 0) {
                    userRef.update("visaNumber", mVisaNumber.getText().toString().trim());
                }
            }
        });

        mVisaExpiry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mVisaExpiry.length() > 0) {
                    userRef.update("visaExpiry", mVisaExpiry.getText().toString().trim());
                }
            }
        });

        mVisaType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mVisaType.length() > 0) {
                    userRef.update("visaType", mVisaType.getText().toString().trim());
                }
            }
        });

        mVisaSubclass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mVisaSubclass.length() > 0) {
                    userRef.update("visaSubclass", mVisaSubclass.getText().toString().trim());
                }
            }
        });

        mVisaExpiry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mVisaExpiry.length() > 0) {
                    userRef.update("visaExpiry", mVisaExpiry.getText().toString().trim());
                }
            }
        });

        mVisaExpiry.setOnClickListener(new View.OnClickListener() {
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

                visaExpiryDate = dayOfMonth + "/" + month + "/" + year;
                mVisaExpiry.setText(visaExpiryDate);
                userRef.update("visaExpiryDate", visaExpiryDate);

                userRef.update("visaExpiryYear", Integer.toString(year));
                userRef.update("visaExpiryMonth", Integer.toString(month));
                userRef.update("visaExpiryDay", Integer.toString(dayOfMonth));

                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
            }
        };

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
