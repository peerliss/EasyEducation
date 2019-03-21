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

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PASSPORT_PHOTO_2 = 2;
    private OnFragmentInteractionListener mListener;
    private Button nextButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Uri pictureUri;
    private String currentPhotoPath;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri photoURI;

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
    private Button mPassportPhoto2Button;
    private ImageView mImageView1;
    private ImageView mPasportPhoto2_ImageView;
    private StorageReference passportPhoto2Ref;
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
        mPasportPhoto2_ImageView = rootView.findViewById(R.id.courseApplyTakePassportPhoto_ImageView2);

        mPassportPhoto2Button = rootView.findViewById(R.id.courseApplyTakeAdditionalPassportPhoto_Button);

        mPassportPhotoLayout = rootView.findViewById(R.id.courseApplyTakePassportPhoto_Layout);

        progressDialog = new ProgressDialog(getContext());

        final CountryCodePicker mCourseApplyCountryCitizenship = rootView.findViewById(R.id.courseApplyCountryCitizenship);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
//        passportPhoto1Ref = firebaseStorage.getReference("users/" + mAuth.getUid() + "/passport_photo_1.png");

        userRef = db.collection("users").document(mAuth.getUid());

        passportPhoto1Ref = firebaseStorage.getReference("users/" + mAuth.getUid() + "/Passport/" + "passportPhoto_1.jpg");
        passportPhoto1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(mImageView1);
                mImageView1.setVisibility(View.VISIBLE);
                mPassportPhoto2Button.setVisibility(View.VISIBLE);
                mPassportPhoto2Button.requestFocus();
            }
        });

        passportPhoto2Ref = firebaseStorage.getReference("users/" + mAuth.getUid() + "/Passport/" + "passportPhoto_2.jpg");
        passportPhoto2Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(mPasportPhoto2_ImageView);
                mPasportPhoto2_ImageView.setVisibility(View.VISIBLE);
                mPassportPhoto2Button.setVisibility(View.VISIBLE);
                mPassportPhoto2Button.requestFocus();
            }
        });

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
//                Toast.makeText(getContext(), "Passport photo click", Toast.LENGTH_SHORT).show();

                try {
                    verifyPermissions(1);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        mPassportPhoto2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Passport photo click", Toast.LENGTH_SHORT).show();

                try {
                    verifyPermissions(2);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            progressDialog.setMessage("Uploading Image...");
            progressDialog.show();
            try {
//                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoURI);
//                mImageView1.setImageBitmap(imageBitmap);

                Picasso.get().load(photoURI).fit().centerCrop().into(mImageView1);
                mImageView1.setVisibility(View.VISIBLE);

//            Will overwrite the images
                passportPhoto1Ref = firebaseStorage.getReference("users/" + mAuth.getUid() + "/Passport/" + "passportPhoto_1.jpg");

                passportPhoto1Ref.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        mPassportPhoto2Button.setVisibility(View.VISIBLE);
                        mPassportPhoto2Button.requestFocus();
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

        if (requestCode == PASSPORT_PHOTO_2 && resultCode == RESULT_OK) {
            progressDialog.setMessage("Uploading Image...");
            progressDialog.show();
            try {
//                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoURI);
//                mPasportPhoto2_ImageView.setImageBitmap(imageBitmap);

                Picasso.get().load(photoURI).fit().centerCrop().into(mPasportPhoto2_ImageView);
                mPasportPhoto2_ImageView.setVisibility(View.VISIBLE);

//            Will overwrite the images
                passportPhoto1Ref = firebaseStorage.getReference("users/" + mAuth.getUid() + "/Passport/" + "passportPhoto_2.jpg");

                passportPhoto1Ref.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
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
        if (passportPhotoNumber == 1) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
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
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            } catch (Exception e) {
                Log.e("PACKAGEMANAGER_NULL", e.getMessage());
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if (passportPhotoNumber == 2) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
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
                        startActivityForResult(takePictureIntent, PASSPORT_PHOTO_2);
                    }
                }
            } catch (Exception e) {
                Log.e("PACKAGEMANAGER_NULL", e.getMessage());
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
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
