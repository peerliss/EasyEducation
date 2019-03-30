package au.com.easyeducation.easyeducation_3.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import java.util.Calendar;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationNewActivity;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApply18Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button nextButton;

    public CourseApply18Fragment() {
        // Required empty public constructor
    }

    public static CourseApply18Fragment newInstance() {
        CourseApply18Fragment fragment = new CourseApply18Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    private EditText mHealthInsuranceProviderName;
    private EditText mHealthInsuranceMembershipNumber;
    private EditText mHealthInsuranceExpiryDate;

    private String expiryDate;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    int mYear;
    int mMonth;
    int mDay;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_18, container, false);

        mHealthInsuranceProviderName = rootView.findViewById(R.id.courseApplyHealthInsurance_Name_ET);
        mHealthInsuranceMembershipNumber = rootView.findViewById(R.id.courseApplyHealthInsurance_Number_ET);
        mHealthInsuranceExpiryDate = rootView.findViewById(R.id.courseApplyHealthInsurance_Expiry_ET);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("healthInsuranceProviderName") != null) {
                    mHealthInsuranceProviderName.setText(documentSnapshot.getString("healthInsuranceProviderName"));
                }
                if (documentSnapshot.getString("healthInsuranceMembershipNumber") != null) {
                    mHealthInsuranceMembershipNumber.setText(documentSnapshot.getString("healthInsuranceMembershipNumber"));
                }
                if (documentSnapshot.getString("healthInsuranceExpiryDate") != null) {
                    mHealthInsuranceExpiryDate.setText(documentSnapshot.getString("healthInsuranceExpiryDate"));
                    mYear = Integer.valueOf(documentSnapshot.getString("healthInsuranceExpiryDateYear"));
                    mMonth = Integer.valueOf(documentSnapshot.getString("healthInsuranceExpiryDateMonth"));
                    mDay = Integer.valueOf(documentSnapshot.getString("healthInsuranceExpiryDateDay"));
                }
            }
        });

        mHealthInsuranceProviderName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mHealthInsuranceProviderName.length() > 0) {
                    userRef.update("healthInsuranceProviderName", mHealthInsuranceProviderName.getText().toString().trim());
                }
            }
        });

        mHealthInsuranceMembershipNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mHealthInsuranceMembershipNumber.length() > 0) {
                    userRef.update("healthInsuranceMembershipNumber", mHealthInsuranceMembershipNumber.getText().toString().trim());
                }
            }
        });

        mHealthInsuranceExpiryDate.setOnClickListener(new View.OnClickListener() {
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

                expiryDate = dayOfMonth + "/" + month + "/" + year;
                mHealthInsuranceExpiryDate.setText(expiryDate);
                userRef.update("healthInsuranceExpiryDate", expiryDate);

                userRef.update("healthInsuranceExpiryDateYear", Integer.toString(year));
                userRef.update("healthInsuranceExpiryDateMonth", Integer.toString(month));
                userRef.update("healthInsuranceExpiryDateDay", Integer.toString(dayOfMonth));

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
                ((CourseApplicationNewActivity) getActivity()).addFragment(16);
            }
        });

        return rootView;
    }

    private boolean verifyExpiryDate() {
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

        if (TextUtils.isEmpty(mHealthInsuranceProviderName.getText()) || mHealthInsuranceProviderName.length() == 0) {
            mHealthInsuranceProviderName.setError("Required.");
            valid = false;
        } else {
            mHealthInsuranceProviderName.setError(null);
        }

        if (TextUtils.isEmpty(mHealthInsuranceMembershipNumber.getText()) || mHealthInsuranceMembershipNumber.length() == 0) {
            mHealthInsuranceMembershipNumber.setError("Required.");
            valid = false;
        } else {
            mHealthInsuranceMembershipNumber.setError(null);
        }

        if (TextUtils.isEmpty(mHealthInsuranceExpiryDate.getText()) || mHealthInsuranceExpiryDate.length() == 0) {
            mHealthInsuranceExpiryDate.setError("Required.");
            valid = false;
        } else {
            mHealthInsuranceExpiryDate.setError(null);
        }

        if (!verifyExpiryDate()) {
            valid = false;
            Toast.makeText(getContext(), "Health Insurance Expiry date cannot be in the past", Toast.LENGTH_LONG).show();
        }

        return valid;
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