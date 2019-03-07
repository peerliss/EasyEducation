package au.com.easyeducation.easyeducation_3.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import java.io.File;
import java.util.Calendar;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationActivity;
import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationNewActivity;
import au.com.easyeducation.easyeducation_3.Activities.RegisterProfileDetailsActivity;
import au.com.easyeducation.easyeducation_3.R;

import static android.app.Activity.RESULT_OK;

public class CourseApply4Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button nextButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    public CourseApply4Fragment() {
        // Required empty public constructor
    }

    public static CourseApply4Fragment newInstance() {
        CourseApply4Fragment fragment = new CourseApply4Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText mPassportNumber;
    private EditText mPassportExpiry;

    private LinearLayout mPassportPhotoLayout;
    private ImageView mImageView1;
    private static final int CAMERA_REQUEST_CODE = 1;

    String name;
    String surname;
    String fullname;
    String passportExpiryDate;

    private DocumentReference userRef;
    private FirebaseStorage firebaseStorage;
    private StorageReference passportPhoto1Ref;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    int mYear;
    int mMonth;
    int mDay;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_4, container, false);

        mPassportNumber = rootView.findViewById(R.id.courseApplyPassportNumber_ET);
        mPassportExpiry = rootView.findViewById(R.id.courseApplyPassportExpiry_ET);

        mImageView1 = rootView.findViewById(R.id.courseApplyTakePassportPhoto_ImageView1);

        mPassportPhotoLayout = rootView.findViewById(R.id.courseApplyTakePassportPhoto_Layout);

        progressDialog = new ProgressDialog(getContext());

        final CountryCodePicker mCourseApplyCountryCitizenship = rootView.findViewById(R.id.courseApplyCountryCitizenship);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
//        passportPhoto1Ref = firebaseStorage.getReference("users/" + mAuth.getUid() + "/passport_photo_1.png");

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("passportNumber") != null) {
                    mPassportNumber.setText(documentSnapshot.getString("passportNumber"));
                }
                if (documentSnapshot.getString("passportExpiryDate") != null) {
                    mPassportExpiry.setText(documentSnapshot.getString("passportExpiryDate"));
                    mYear = Integer.valueOf(documentSnapshot.getString("passportExpiryYear"));
                    mMonth = Integer.valueOf(documentSnapshot.getString("passportExpiryMonth"));
                    mDay = Integer.valueOf(documentSnapshot.getString("passportExpiryDay"));
                }
//                if (documentSnapshot.getString("visaType") != null) {
//                    mVisaType.setText(documentSnapshot.getString("visaType"));
//                }
//                if (documentSnapshot.getString("visaSubclass") != null) {
//                    mVisaSubclass.setText(documentSnapshot.getString("visaSubclass"));
    //                }
                if (documentSnapshot.get("countryCitizenshipCode") != null) {
                    mCourseApplyCountryCitizenship.setDefaultCountryUsingNameCode(documentSnapshot.getString("countryCitizenshipCode"));
                    mCourseApplyCountryCitizenship.resetToDefaultCountry();
                }
            }
        });

        mPassportNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mPassportNumber.length() > 0) {
                    userRef.update("passportNumber", mPassportNumber.getText().toString().trim());
                }
            }
        });

        mPassportExpiry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mPassportExpiry.length() > 0) {
                    userRef.update("passportExpiry", mPassportExpiry.getText().toString().trim());
                }
            }
        });

        mPassportExpiry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mPassportExpiry.length() > 0) {
                    userRef.update("passportExpiry", mPassportExpiry.getText().toString().trim());
                }
            }
        });

        mPassportExpiry.setOnClickListener(new View.OnClickListener() {
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

                passportExpiryDate = dayOfMonth + "/" + month + "/" + year;
                mPassportExpiry.setText(passportExpiryDate);
                userRef.update("passportExpiryDate", passportExpiryDate);

                userRef.update("passportExpiryYear", Integer.toString(year));
                userRef.update("passportExpiryMonth", Integer.toString(month));
                userRef.update("passportExpiryDay", Integer.toString(dayOfMonth));

                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
            }
        };

        mCourseApplyCountryCitizenship.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                userRef.update("countryCitizenship", mCourseApplyCountryCitizenship.getSelectedCountryName());
                userRef.update("countryCitizenshipCode", mCourseApplyCountryCitizenship.getSelectedCountryNameCode());
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

        mPassportPhotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Passport photo click", Toast.LENGTH_SHORT).show();

                try {
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera_intent, CAMERA_REQUEST_CODE);
                }
                catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            progressDialog.setMessage("Uploading Image...");
            progressDialog.show();

            Uri uri = data.getData();

//            passportPhoto1Ref = firebaseStorage.getReference("users/" + mAuth.getUid() + "/Passport" + "/passport_photo_1.png");
//            Will overwrite the images
            passportPhoto1Ref = firebaseStorage.getReference("users/" + mAuth.getUid() + "/Passport" + uri.getLastPathSegment());

            passportPhoto1Ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Image successfully saved", Toast.LENGTH_LONG).show();

                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    private boolean validateFields() {
        boolean valid = true;

        if (TextUtils.isEmpty(mPassportNumber.getText()) || mPassportNumber.length() == 0) {
            mPassportNumber.setError("Required.");
            valid = false;
        } else {
            mPassportNumber.setError(null);
        }

        if (TextUtils.isEmpty(mPassportExpiry.getText()) || mPassportExpiry.length() == 0) {
            mPassportExpiry.setError("Required.");
            valid = false;
        } else {
            mPassportExpiry.setError(null);
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
