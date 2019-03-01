package au.com.easyeducation.easyeducation_3.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationNewActivity;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApply13Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private boolean buttonSelected = false;
    private Button nextButton;

    public CourseApply13Fragment() {
        // Required empty public constructor
    }

    public static CourseApply13Fragment newInstance() {
        CourseApply13Fragment fragment = new CourseApply13Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private LinearLayout mQualification2Layout;
    private LinearLayout mQualification3Layout;

    private int i = 0;

    private EditText mHighestQualificationName;
    private EditText mHighestQualificationInstitution;
    private CountryCodePicker mQualificationCountry;

    private EditText mHighestQualification2Name;
    private EditText mHighestQualification2Institution;
    private CountryCodePicker mQualification2Country;

    private EditText mHighestQualification3Name;
    private EditText mHighestQualification3Institution;
    private CountryCodePicker mQualification3Country;

    private Button mAddQualificationButton;

    private DocumentReference userRef;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_13, container, false);

        mQualification2Layout = rootView.findViewById(R.id.courseApplyHighestQualification2_Layout);
        mQualification3Layout = rootView.findViewById(R.id.courseApplyHighestQualification3_Layout);

        mAddQualificationButton = rootView.findViewById(R.id.courseApplyAddQualification_Button);

        mQualification2Layout.setVisibility(View.GONE);
        mQualification3Layout.setVisibility(View.GONE);

        mHighestQualificationName = rootView.findViewById(R.id.courseApplyHighestQualification_Name_ET);
        mHighestQualificationInstitution = rootView.findViewById(R.id.courseApplyHighestQualification_Institute_ET);
        mQualificationCountry = rootView.findViewById(R.id.courseApplyHighestQualification_Country);

        mHighestQualification2Name = rootView.findViewById(R.id.courseApplyHighestQualification2_Name_ET);
        mHighestQualification2Institution = rootView.findViewById(R.id.courseApplyHighestQualification2_Institute_ET);
        mQualification2Country = rootView.findViewById(R.id.courseApplyHighestQualification2_Country);

        mHighestQualification3Name = rootView.findViewById(R.id.courseApplyHighestQualification3_Name_ET);
        mHighestQualification3Institution = rootView.findViewById(R.id.courseApplyHighestQualification3_Institute_ET);
        mQualification3Country = rootView.findViewById(R.id.courseApplyHighestQualification3_Country);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("highestQualificationName") != null) {
                    mHighestQualificationName.setText(documentSnapshot.getString("highestQualificationName"));
                }
                if (documentSnapshot.getString("highestQualificationInstitution") != null) {
                    mHighestQualificationInstitution.setText(documentSnapshot.getString("highestQualificationInstitution"));
                }
                if (documentSnapshot.get("highestQualificationCountry") != null) {
                    mQualificationCountry.setDefaultCountryUsingNameCode(documentSnapshot.getString("highestQualificationCountry"));
                    mQualificationCountry.resetToDefaultCountry();
                } else if (documentSnapshot.get("highestQualificationCountry") == null) {
                    mQualificationCountry.setDefaultCountryUsingNameCode(documentSnapshot.getString("countryBirthCode"));
                    mQualificationCountry.resetToDefaultCountry();
                }

                if (documentSnapshot.getString("highestQualification2Name") != null) {
                    mHighestQualification2Name.setText(documentSnapshot.getString("highestQualification2Name"));
                    mQualification2Layout.setVisibility(View.VISIBLE);
                    i++;
                }
                if (documentSnapshot.getString("highestQualification2Institution") != null) {
                    mHighestQualification2Institution.setText(documentSnapshot.getString("highestQualification2Institution"));
                }
                if (documentSnapshot.get("highestQualification2Country") != null) {
                    mQualification2Country.setDefaultCountryUsingNameCode(documentSnapshot.getString("highestQualification2Country"));
                    mQualification2Country.resetToDefaultCountry();
                }

                if (documentSnapshot.getString("highestQualification3Name") != null) {
                    mHighestQualification3Name.setText(documentSnapshot.getString("highestQualification3Name"));
                    mQualification3Layout.setVisibility(View.VISIBLE);
                }
                if (documentSnapshot.getString("highestQualification3Institution") != null) {
                    mHighestQualification3Institution.setText(documentSnapshot.getString("highestQualification3Institution"));
                }
                if (documentSnapshot.get("highestQualification3Country") != null) {
                    mQualification3Country.setDefaultCountryUsingNameCode(documentSnapshot.getString("highestQualification3Country"));
                    mQualification3Country.resetToDefaultCountry();
                }
            }
        });

        mAddQualificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQualification2Layout.getVisibility() == View.GONE)
                    if (i == 0 && !TextUtils.isEmpty(mHighestQualificationName.getText()) || mHighestQualificationName.length() != 0
                            && !TextUtils.isEmpty(mHighestQualificationInstitution.getText()) || mHighestQualificationInstitution.length() != 0) {
                        mQualification2Layout.setVisibility(View.VISIBLE);
//                        mHighestQualificationName.setError(null);
//                        mHighestQualificationInstitution.setError(null);
                        i++;
                        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.get("highestQualification2Country") != null) {
                                    mQualification2Country.setDefaultCountryUsingNameCode(documentSnapshot.getString("highestQualification2Country"));
                                    mQualification2Country.resetToDefaultCountry();
                                } else if (documentSnapshot.get("highestQualification2Country") == null) {
                                    mQualification2Country.setDefaultCountryUsingNameCode(documentSnapshot.getString("highestQualificationCountry"));
                                    mQualification2Country.resetToDefaultCountry();
                                }
                            }
                        });
                        return;
                    }
//                    else {
//                        mHighestQualificationName.setError("Required.");
//                        mHighestQualificationInstitution.setError("Required.");
//                        return;
//                    }
                if (i == 1 && !TextUtils.isEmpty(mHighestQualification2Name.getText()) || mHighestQualification2Name.length() != 0
                        && !TextUtils.isEmpty(mHighestQualificationInstitution.getText()) || mHighestQualificationInstitution.length() != 0) {
//                    mHighestQualification2Name.setError(null);
//                    mHighestQualification2Institution.setError(null);
                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (mHighestQualification2Name != null && mHighestQualification2Institution != null) {
                                mQualification3Layout.setVisibility(View.VISIBLE);
                                mAddQualificationButton.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getContext(), "Please enter details for qualification 2 first.", Toast.LENGTH_SHORT).show();
                            }
                            if (documentSnapshot.get("highestQualification3Country") != null) {
                                mQualification3Country.setDefaultCountryUsingNameCode(documentSnapshot.getString("highestQualification3Country"));
                                mQualification3Country.resetToDefaultCountry();
                            } else if (documentSnapshot.get("highestQualification3Country") == null) {
                                mQualification3Country.setDefaultCountryUsingNameCode(documentSnapshot.getString("highestQualificationCountry"));
                                mQualification3Country.resetToDefaultCountry();
                            }
                        }
                    });
                }
//                else {
//                    mHighestQualification2Name.setError("Required.");
//                    mHighestQualification2Institution.setError("Required.");
//                }
            }
        });

        mHighestQualificationName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mHighestQualificationName.length() > 0) {
                    userRef.update("highestQualificationName", mHighestQualificationName.getText().toString().trim());
                }
            }
        });

        mHighestQualificationInstitution.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mHighestQualificationInstitution.length() > 0) {
                    userRef.update("highestQualificationInstitution", mHighestQualificationInstitution.getText().toString().trim());
                }
            }
        });

        mQualificationCountry.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
//                userRef.update("highestQualificationCountry", mQualificationCountry.getSelectedCountryName());
                userRef.update("highestQualificationCountry", mQualificationCountry.getSelectedCountryNameCode());
            }
        });


        mHighestQualification2Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mHighestQualification2Name.length() > 0) {
                    userRef.update("highestQualification2Name", mHighestQualification2Name.getText().toString().trim());
                }
            }
        });

        mHighestQualification2Institution.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mHighestQualification2Institution.length() > 0) {
                    userRef.update("highestQualification2Institution", mHighestQualification2Institution.getText().toString().trim());
                }
            }
        });

        mQualification2Country.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
//                userRef.update("highestQualification2Country", mQualification2Country.getSelectedCountryName());
                userRef.update("highestQualification2Country", mQualification2Country.getSelectedCountryNameCode());
            }
        });


        mHighestQualification3Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mHighestQualification3Name.length() > 0) {
                    userRef.update("highestQualification3Name", mHighestQualification3Name.getText().toString().trim());
                }
            }
        });

        mHighestQualification3Institution.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mHighestQualification3Institution.length() > 0) {
                    userRef.update("highestQualification3Institution", mHighestQualification3Institution.getText().toString().trim());
                }
            }
        });

        mQualification3Country.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
//                userRef.update("highestQualification3Country", mQualification3Country.getSelectedCountryName());
                userRef.update("highestQualification3Country", mQualification3Country.getSelectedCountryNameCode());
            }
        });

        nextButton = getActivity().findViewById(R.id.courseApplicationNextButton);

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

        if (TextUtils.isEmpty(mHighestQualificationName.getText()) || mHighestQualificationName.length() == 0) {
            mHighestQualificationName.setError("Required.");
            valid = false;
        } else {
            mHighestQualificationName.setError(null);
        }

        if (TextUtils.isEmpty(mHighestQualificationInstitution.getText()) || mHighestQualificationInstitution.length() == 0) {
            mHighestQualificationInstitution.setError("Required.");
            valid = false;
        } else {
            mHighestQualificationInstitution.setError(null);
        }

        return valid;
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        //INSERT CUSTOM CODE HERE
        i = 0;
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
