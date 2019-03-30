package au.com.easyeducation.easyeducation_3.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
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

import au.com.easyeducation.easyeducation_3.Activities.RegisterProfileDetailsActivity;
import au.com.easyeducation.easyeducation_3.Activities.RegisterProfileDetailsNewActivity;
import au.com.easyeducation.easyeducation_3.R;

public class RegisterProfileDOBFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RegisterProfileDOBFragment() {
        // Required empty public constructor
    }

    public static RegisterProfileDOBFragment newInstance() {
        RegisterProfileDOBFragment fragment = new RegisterProfileDOBFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText mDOB;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String dob;

    private DocumentReference userRef;

    int mYear;
    int mMonth;
    int mDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.register_profile_dob_fragment, container, false);

        mDOB = rootView.findViewById(R.id.registerDOB);
        Button mDOBNextButton = rootView.findViewById(R.id.registerDOBNextButton);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("dob") != null) {
                    mDOB.setText(documentSnapshot.getString("dob"));
                }
            }
        });

        mDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                dob = dayOfMonth + "/" + month + "/" + year;

                userRef.update("dobYear", Integer.toString(year));
                userRef.update("dobMonth", Integer.toString(month));
                userRef.update("dobDay", Integer.toString(dayOfMonth));

                mDOB.setText(dob);

                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
            }
        };

        mDOBNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateDOB()) {
                    Toast.makeText(getContext(), "Please check fields.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    userRef.update("dob", dob);
                    ((RegisterProfileDetailsNewActivity) getActivity()).addFragment();
                }
            }
        });

        return rootView;
    }

    private boolean verifyAge() {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - mYear;

        if (today.get(Calendar.DAY_OF_MONTH) < mDay) {
            age--;
        }

        return age >= 18;
    }

    private boolean validateDOB() {
        boolean valid = true;

        dob = mDOB.getText().toString().trim();

        if (TextUtils.isEmpty(dob)) {
            mDOB.setError("Required.");
            valid = false;
        } else {
            mDOB.setError(null);
        }

        if (!verifyAge()) {
            valid = false;
            Toast.makeText(getContext(), "Must be at least 18 years or older", Toast.LENGTH_LONG).show();
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