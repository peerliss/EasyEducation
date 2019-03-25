package au.com.easyeducation.easyeducation_3.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

public class CourseApply9Fragment extends Fragment {

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

    private OnFragmentInteractionListener mListener;
    private Button nextButton;
    
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

    // Camera functionality - variables
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference visaPhotoRef;
    private Uri photoURI;
    private ScrollView mVisaScrollView;
    private LinearLayout mVisaPhotoLayoutButton;
    private LinearLayout mViewVisaPhotoLayout;
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
        View rootView = inflater.inflate(R.layout.fragment_course_apply_9, container, false);

        mVisaType = rootView.findViewById(R.id.courseApplyVisaType_ET);
        mVisaSubclass = rootView.findViewById(R.id.courseApplyVisaSubclass_ET);
        mVisaNumber = rootView.findViewById(R.id.courseApplyVisaNumber_ET);
        mVisaExpiry = rootView.findViewById(R.id.courseApplyVisaExpiry_ET);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        // Camera functionality - initialize
        firebaseStorage = FirebaseStorage.getInstance();
        mVisaPhotoLayoutButton = rootView.findViewById(R.id.courseApplyTakeVisaPhoto_Layout);
        mVisaScrollView = rootView.findViewById(R.id.courseApplyVisa_Scrollview);
        mViewVisaPhotoLayout = rootView.findViewById(R.id.courseApplyViewVisaPhoto_Layout);
        progressDialog = new ProgressDialog(getContext());

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
                // Camera functionality - initialize
                if (documentSnapshot.getString("visaPhotoTakenAmount") != null) {
                    photoTakenAmount = Integer.valueOf(documentSnapshot.getString("visaPhotoTakenAmount"));
                    loadImages(imageLoadIndex);
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

        // Camera functionality - button
        mVisaPhotoLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoTakenAmount <= 4) {
                    try {
                        verifyPermissions(photoTakenAmount);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Visa Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Can only take 5 visa photos", Toast.LENGTH_LONG).show();
                }
            }
        });
        
        return rootView;
    }

    // Camera functionality
    private void addimageLoadIndexAmount() {
        imageLoadIndex++;
    }

    private void loadImages(int photoNumber) {
        String visaPhotoName = "visaPhoto_" + String.valueOf((photoNumber)) + ".jpg";
        visaPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/Visa/" + visaPhotoName);

        visaPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewVisaPhotoLayout, false);
                mViewVisaPhotoLayout.addView(photo_imageView);
                ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                Glide.with(mViewVisaPhotoLayout).load(uri).into(imageView);

                addimageLoadIndexAmount();
                photoTaken = true;

                if (imageLoadIndex <= photoTakenAmount) {
                    loadImages(imageLoadIndex);
                }
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
                View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewVisaPhotoLayout, false);
                mViewVisaPhotoLayout.addView(photo_imageView);
                ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                Glide.with(imageView).load(photoURI).into(imageView);

                String visaPhotoName = "visaPhoto_" + (photoTakenAmount + 1) + ".jpg";
                visaPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/Visa/" + visaPhotoName);

                visaPhotoRef.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        photoTakenAmount++;
                        userRef.update("visaPhotoTakenAmount", String.valueOf(photoTakenAmount));
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
        mVisaScrollView.post(new Runnable() {
            @Override
            public void run() {
//                mVisaScrollView.scrollTo(0, mVisaPhotoLayoutButton.getBottom());
                mVisaScrollView.smoothScrollTo(0, mVisaPhotoLayoutButton.getBottom());
            }
        });
    }

    private void verifyPermissions(int visaPhotoNumber) {
        Log.d("Verify Camera Permission", "verifyPermissions: asking user for permissions");
        String cameraPermission[] = {Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(getContext(), cameraPermission[0]) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent(visaPhotoNumber);
        } else {
            ActivityCompat.requestPermissions(getActivity(), cameraPermission, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakePictureIntent(int visaPhotoNumber) {
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
                    startActivityForResult(takePictureIntent, visaPhotoNumber);
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
        boolean valid = true;

        if (TextUtils.isEmpty(mVisaType.getText()) || mVisaType.length() == 0) {
            mVisaType.setError("Required.");
            valid = false;
        } else {
            mVisaType.setError(null);
        }

        if (TextUtils.isEmpty(mVisaSubclass.getText()) || mVisaSubclass.length() == 0) {
            mVisaSubclass.setError("Required.");
            valid = false;
        } else {
            mVisaSubclass.setError(null);
        }

        if (TextUtils.isEmpty(mVisaExpiry.getText()) || mVisaExpiry.length() == 0) {
            mVisaExpiry.setError("Required.");
            valid = false;
        } else {
            mVisaExpiry.setError(null);
        }

        if (TextUtils.isEmpty(mVisaNumber.getText()) || mVisaNumber.length() == 0) {
            mVisaNumber.setError("Required.");
            valid = false;
        } else {
            mVisaNumber.setError(null);
        }

        // Camera functionality
        if (!photoTaken) {
            Toast.makeText(getContext(), "Please take valid photo of visa", Toast.LENGTH_LONG).show();
            valid = false;
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
