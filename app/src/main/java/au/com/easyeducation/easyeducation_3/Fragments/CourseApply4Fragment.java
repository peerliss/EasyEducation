package au.com.easyeducation.easyeducation_3.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.util.DisplayMetrics;
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
import com.google.firebase.firestore.proto.TargetOuterClass;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

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
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class CourseApply4Fragment extends Fragment {

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

    private ImageView mImageView1;
    private ImageView mPasportPhoto2_ImageView;
    private StorageReference passportPhoto2Ref;
    private static final int CAMERA_REQUEST_CODE = 1;

    String name;
    String surname;
    String fullname;
    String passportExpiryDate;

    private DocumentReference userRef;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    int mYear;
    int mMonth;
    int mDay;

    private static final int PASSPORT_PHOTO_2 = 2;
    private OnFragmentInteractionListener mListener;
    private Button nextButton;
    private ProgressDialog progressDialog;
    private Uri pictureUri;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // Camera functionality - variables
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference passportPhotoRef;
    private Uri photoURI;
    private ScrollView mPassportScrollView;
    private LinearLayout mPassportPhotoLayout;
    private LinearLayout mViewPassportPhotoLayout;
    private int photoTakenAmount;
    private int imageLoadIndex = 1;
    private boolean photoTaken = false;
    private String currentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_4, container, false);

        mPassportNumber = rootView.findViewById(R.id.courseApplyPassportNumber_ET);
        mPassportExpiry = rootView.findViewById(R.id.courseApplyPassportExpiry_ET);

        // Camera functionality - initialize
        firebaseStorage = FirebaseStorage.getInstance();
        mPassportPhotoLayout = rootView.findViewById(R.id.courseApplyTakePassportPhoto_Layout);
        mPassportScrollView = rootView.findViewById(R.id.courseApplyPassport_Scrollview);
        mViewPassportPhotoLayout = rootView.findViewById(R.id.courseApplyViewPassportPhoto_Layout);
        progressDialog = new ProgressDialog(getContext());

        final CountryCodePicker mCourseApplyCountryCitizenship = rootView.findViewById(R.id.courseApplyCountryCitizenship);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                if (documentSnapshot.get("countryCitizenshipCode") != null) {
                    mCourseApplyCountryCitizenship.setDefaultCountryUsingNameCode(documentSnapshot.getString("countryCitizenshipCode"));
                    mCourseApplyCountryCitizenship.resetToDefaultCountry();
                }
                // Camera functionality - initialize
                if (documentSnapshot.getString("passportPhotoTakenAmount") != null) {
                    photoTakenAmount = Integer.valueOf(documentSnapshot.getString("passportPhotoTakenAmount"));
                    loadImages(imageLoadIndex);
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

        // Camera functionality - button
        mPassportPhotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoTakenAmount <= 4) {
                    try {
                        verifyPermissions(photoTakenAmount);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Can only take 5 passport photos", Toast.LENGTH_LONG).show();
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
        String passportPhotoName = "passportPhoto_" + String.valueOf((photoNumber)) + ".jpg";
        passportPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/Passport/" + passportPhotoName);

        passportPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewPassportPhotoLayout, false);
                mViewPassportPhotoLayout.addView(photo_imageView);
                ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                Glide.with(mViewPassportPhotoLayout).load(uri).into(imageView);

                addimageLoadIndexAmount();
                photoTaken = true;

                if (imageLoadIndex <= photoTakenAmount) {
                    loadImages(imageLoadIndex);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                photoTakenAmount = 0;
//                userRef.update("passportPhotoTakenAmount", String.valueOf(0));
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
                View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewPassportPhotoLayout, false);
                mViewPassportPhotoLayout.addView(photo_imageView);
                ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                Glide.with(imageView).load(photoURI).into(imageView);

                String passportPhotoName = "passportPhoto_" + (photoTakenAmount + 1) + ".jpg";
                passportPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/Passport/" + passportPhotoName);

                passportPhotoRef.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        photoTakenAmount++;
                        userRef.update("passportPhotoTakenAmount", String.valueOf(photoTakenAmount));
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
        mPassportScrollView.post(new Runnable() {
            @Override
            public void run() {
//                mPassportScrollView.scrollTo(0, mPassportPhotoLayout.getBottom());
                mPassportScrollView.smoothScrollTo(0, mPassportPhotoLayout.getBottom());
            }
        });
    }

    private void verifyPermissions(int passportPhotoNumber) {
        Log.d("Verify Camera Permission", "verifyPermissions: asking user for permissions");
        String cameraPermission[] = {Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(getContext(), cameraPermission[0]) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent(passportPhotoNumber);
        } else {
            ActivityCompat.requestPermissions(getActivity(), cameraPermission, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakePictureIntent(int passportPhotoNumber) {
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
                    startActivityForResult(takePictureIntent, passportPhotoNumber);
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


    @Override
    public void onResume() {
        loadImages(imageLoadIndex);
        super.onResume();
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

        // Camera functionality
        if (!photoTaken) {
            Toast.makeText(getContext(), "Please take valid photo of passport", Toast.LENGTH_LONG).show();
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