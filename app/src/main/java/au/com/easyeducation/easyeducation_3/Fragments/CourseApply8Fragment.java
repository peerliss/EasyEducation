package au.com.easyeducation.easyeducation_3.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationNewActivity;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApply8Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply8Fragment() {
        // Required empty public constructor
    }

    public static CourseApply8Fragment newInstance() {
        CourseApply8Fragment fragment = new CourseApply8Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText mStreet;
    private EditText mSuburb;
    private EditText mState;
    private EditText mPostCode;
    private Button nextButton;

    private CountryCodePicker homeCountry;

    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_8, container, false);

        mStreet = rootView.findViewById(R.id.courseApplyHomeCountryStreet_ET);
        mSuburb = rootView.findViewById(R.id.courseApplyHomeCountrySuburb_ET);
        mState = rootView.findViewById(R.id.courseApplyHomeCountryState_ET);
        mPostCode = rootView.findViewById(R.id.courseApplyHomeCountryPostCode_ET);

        nextButton = getActivity().findViewById(R.id.courseApplicationNextButton);

        homeCountry = rootView.findViewById(R.id.courseApplyHomeCountry);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("countryBirthCode") == documentSnapshot.get("countryCitizenshipCode") && documentSnapshot.get("homeCountryPostCode") == null) {
                    if (documentSnapshot.getString("street") != null) {
                        mStreet.setText(documentSnapshot.getString("street"));
                    }
                    if (documentSnapshot.getString("suburb") != null) {
                        mSuburb.setText(documentSnapshot.getString("suburb"));
                    }
                    if (documentSnapshot.getString("state") != null) {
                        mState.setText(documentSnapshot.getString("state"));
                    }
                    if (documentSnapshot.getString("postCode") != null) {
                        mPostCode.setText(documentSnapshot.getString("postCode"));
                    }
                    if (documentSnapshot.get("countryBirthCode") != null) {
                        homeCountry.setDefaultCountryUsingNameCode(documentSnapshot.getString("countryBirthCode"));
                        homeCountry.resetToDefaultCountry();
                    }
                }
                if (documentSnapshot.getString("homeCountryStreet") != null) {
                    mStreet.setText(documentSnapshot.getString("homeCountryStreet"));
                }
                if (documentSnapshot.getString("homeCountrySuburb") != null) {
                    mSuburb.setText(documentSnapshot.getString("homeCountrySuburb"));
                }
                if (documentSnapshot.getString("homeCountryState") != null) {
                    mState.setText(documentSnapshot.getString("homeCountryState"));
                }
                if (documentSnapshot.getString("homeCountryPostCode") != null) {
                    mPostCode.setText(documentSnapshot.getString("homeCountryPostCode"));
                }
                if (documentSnapshot.get("homeCountry") != null) {
                    homeCountry.setDefaultCountryUsingNameCode(documentSnapshot.getString("homeCountry"));
                    homeCountry.resetToDefaultCountry();
                }
            }
        });

        mStreet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mStreet.length() > 0) {
                    userRef.update("homeCountryStreet", mStreet.getText().toString().trim());
                }
            }
        });

        mSuburb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mSuburb.length() > 0) {
                    userRef.update("homeCountrySuburb", mSuburb.getText().toString().trim());
                }
            }
        });

        mState.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mState.length() > 0) {
                    userRef.update("homeCountryState", mState.getText().toString().trim());
                }
            }
        });

        mPostCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mPostCode.length() > 0) {
                    userRef.update("homeCountryPostCode", mPostCode.getText().toString().trim());
                }
            }
        });

        homeCountry.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                userRef.update("homeCountry", homeCountry.getSelectedCountryName());
                userRef.update("homeCountryCode", homeCountry.getSelectedCountryNameCode());
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFields()) {
                    return;
                }
                ((CourseApplicationNewActivity) getActivity()).addFragment();
            }
        });

        return rootView;
    }

    private boolean validateFields() {
        boolean valid = true;

        if (TextUtils.isEmpty(mStreet.getText()) || mStreet.length() == 0) {
            mStreet.setError("Required.");
            valid = false;
        } else {
            mStreet.setError(null);
        }

        if (TextUtils.isEmpty(mSuburb.getText()) || mSuburb.length() == 0) {
            mSuburb.setError("Required.");
            valid = false;
        } else {
            mSuburb.setError(null);
        }

        if (TextUtils.isEmpty(mState.getText()) || mState.length() == 0) {
            mState.setError("Required.");
            valid = false;
        } else {
            mState.setError(null);
        }

        if (TextUtils.isEmpty(mPostCode.getText()) || mPostCode.length() == 0) {
            mPostCode.setError("Required.");
            valid = false;
        } else {
            mPostCode.setError(null);
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
