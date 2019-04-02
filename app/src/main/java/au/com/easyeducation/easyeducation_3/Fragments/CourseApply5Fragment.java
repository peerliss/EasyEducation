package au.com.easyeducation.easyeducation_3.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationActivity;
import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationNewActivity;
import au.com.easyeducation.easyeducation_3.Activities.RegisterProfileDetailsActivity;
import au.com.easyeducation.easyeducation_3.R;

import static android.app.Activity.RESULT_OK;

public class CourseApply5Fragment extends Fragment {

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

    private OnFragmentInteractionListener mListener;
    private boolean buttonSelected = false;
    private Button nextButton;

    // Camera functionality - variables
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference englishTestPhotoRef;
    private Uri photoURI;
    private ScrollView mEnglishTestScrollView;
    private LinearLayout mEnglishTestPhotoLayoutButton;
    private LinearLayout mViewEnglishTestPhotoLayout;
    private int photoTakenAmount;
    private int imageLoadIndex = 1;
    private boolean photoTaken = false;
    private ProgressDialog progressDialog;
    private String currentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

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

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        // Camera functionality - initialize
        firebaseStorage = FirebaseStorage.getInstance();
        mEnglishTestPhotoLayoutButton = rootView.findViewById(R.id.courseApplyTakeEnglishTestPhoto_Layout);
        mEnglishTestScrollView = rootView.findViewById(R.id.courseApplyEnglishTest_Scrollview);
        mViewEnglishTestPhotoLayout = rootView.findViewById(R.id.courseApplyViewEnglishTestPhoto_Layout);
        progressDialog = new ProgressDialog(getContext());

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
                        mTestDate.setText(documentSnapshot.getString("englishTestDate"));
                        mYear = Integer.valueOf(documentSnapshot.getString("englishTestYear"));
                        mMonth = Integer.valueOf(documentSnapshot.getString("englishTestMonth"));
                        mDay = Integer.valueOf(documentSnapshot.getString("englishTestDay"));
                    }
                    if (documentSnapshot.getString("englishTestResults") != null) {
                        mTestResults.setText(documentSnapshot.getString("englishTestResults"));
                    }
                }
                if (documentSnapshot.getString("englishTestMainLanguageSpoken") != null) {
                    mMainLanguage.setText(documentSnapshot.getString("englishTestMainLanguageSpoken"));
                }
                // Camera functionality - initialize
                if (documentSnapshot.getString("englishTestPhotoTakenAmount") != null) {
                    photoTakenAmount = Integer.valueOf(documentSnapshot.getString("englishTestPhotoTakenAmount"));
                    loadImages(imageLoadIndex);
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

        nextButton = getActivity().findViewById(R.id.courseApplicationNextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFields()) {
                    return;
                }
                ((CourseApplicationNewActivity) getActivity()).addFragment(8);
            }
        });

        // Camera functionality - button
        mEnglishTestPhotoLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoTakenAmount <= 4) {
                    try {
                        verifyPermissions(photoTakenAmount);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "EnglishTest Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Can only take 5 englishTest photos", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    private boolean verifyDate() {
        Calendar today = Calendar.getInstance();

        if (today.get(Calendar.YEAR) == mYear) {
            if (today.get(Calendar.MONTH) + 1 == mMonth) {
                return today.get(Calendar.DAY_OF_MONTH) >= mDay;
            }
            return today.get(Calendar.MONTH) >= mMonth;
        }

        return today.get(Calendar.YEAR) >= mYear;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // Camera functionality
    private void addimageLoadIndexAmount() {
        imageLoadIndex++;
    }

    private void loadImages(int photoNumber) {
        String englishTestPhotoName = "englishTestPhoto_" + String.valueOf((photoNumber)) + ".jpg";
        englishTestPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/EnglishTest/" + englishTestPhotoName);

        englishTestPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewEnglishTestPhotoLayout, false);
                    mViewEnglishTestPhotoLayout.addView(photo_imageView);
                    ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                    Glide.with(mViewEnglishTestPhotoLayout).load(uri).into(imageView);

                    addimageLoadIndexAmount();
                    photoTaken = true;

                    if (imageLoadIndex <= photoTakenAmount) {
                        loadImages(imageLoadIndex);
                    }
                } catch (Exception e) {
//                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("English Test Photo - Load Failure", e.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            progressDialog.setMessage("Uploading Image...");
            progressDialog.show();
            try {
                View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewEnglishTestPhotoLayout, false);
                mViewEnglishTestPhotoLayout.addView(photo_imageView);
                ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                Glide.with(imageView).load(photoURI).into(imageView);

                String englishTestPhotoName = "englishTestPhoto_" + (photoTakenAmount + 1) + ".jpg";
                englishTestPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/EnglishTest/" + englishTestPhotoName);

                englishTestPhotoRef.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        photoTakenAmount++;
                        userRef.update("englishTestPhotoTakenAmount", String.valueOf(photoTakenAmount));
                        focusToTakePhotoButton();

                        photoTaken = true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Image save failure - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                Toast.makeText(getContext(), "OnActvitiyResult exception - " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void focusToTakePhotoButton() {
        mEnglishTestScrollView.post(new Runnable() {
            @Override
            public void run() {
//                mEnglishTestScrollView.scrollTo(0, mEnglishTestPhotoLayoutButton.getBottom());
                mEnglishTestScrollView.smoothScrollTo(0, mEnglishTestPhotoLayoutButton.getBottom());
            }
        });
    }

    private void verifyPermissions(int englishTestPhotoNumber) {
        Log.d("Verify Camera Permission", "verifyPermissions: asking user for permissions");
        String cameraPermission[] = {Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(getContext(), cameraPermission[0]) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent(englishTestPhotoNumber);
        } else {
            ActivityCompat.requestPermissions(getActivity(), cameraPermission, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakePictureIntent(int englishTestPhotoNumber) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(getContext(),
                            "au.com.easyeducation.easyeducation_3.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, englishTestPhotoNumber);
                }
            }
        } catch (Exception e) {
            Log.e("PACKAGEMANAGER_NULL", e.getMessage());
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        verifyPermissions(requestCode);
    }
    // end camera functionality

    private boolean validateFields() {
        if (!buttonSelected) {
            Toast.makeText(getContext(), "Please select your english test taken", Toast.LENGTH_SHORT).show();
        }

        boolean valid = true;

        if (TextUtils.isEmpty(mTestDate.getText()) || mTestDate.length() == 0) {
            mTestDate.setError("Required.");
            valid = false;
        } else {
            mTestDate.setError(null);
        }

        if (TextUtils.isEmpty(mTestResults.getText()) || mTestResults.length() == 0) {
            mTestResults.setError("Required.");
            valid = false;
        } else {
            mTestResults.setError(null);
        }

        if (TextUtils.isEmpty(mMainLanguage.getText()) || mMainLanguage.length() == 0) {
            mMainLanguage.setError("Required.");
            valid = false;
        } else {
            mMainLanguage.setError(null);
        }

        // Camera functionality
        if (!photoTaken) {
            Toast.makeText(getContext(), "Please take valid photo of English Test Results", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (!verifyDate()) {
            valid = false;
            Toast.makeText(getContext(), "Test taken date cannot be in the future", Toast.LENGTH_LONG).show();
        }

        return valid;
    }

    private void unSelectAllButtons() {
        buttonSelected = true;

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
