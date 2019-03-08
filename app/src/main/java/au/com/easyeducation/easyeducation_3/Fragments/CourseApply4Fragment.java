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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
    private OnFragmentInteractionListener mListener;
    private Button nextButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Uri pictureUri;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

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
                    verifyPermissions();
//                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(camera_intent, CAMERA_REQUEST_CODE);
                }
                catch (Exception e) {
                    Toast.makeText(getContext(), "Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
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
//            progressDialog.show();

            try {
                Uri uri = data.getData();

//            passportPhoto1Ref = firebaseStorage.getReference("users/" + mAuth.getUid() + "/Passport" + "/passport_photo_1.png");
//            Will overwrite the images
                passportPhoto1Ref = firebaseStorage.getReference("users/" + mAuth.getUid() + "/Passport" + "Passport_photo_1.jpg");

//                passportPhoto1Ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                passportPhoto1Ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Image successfully saved", Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();

                        Uri downloadUri = taskSnapshot.getUploadSessionUri();

                        Picasso.get().load(downloadUri).fit().centerCrop().into(mImageView1);

                        mImageView1.setVisibility(View.VISIBLE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Image save failure - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            catch (Exception e) {
                Toast.makeText(getContext(), "OnActvitiyResult exception - " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void verifyPermissions() {
        Log.d("Verify Camera Permission", "verifyPermissions: asking user for permissions");
        String cameraPermission[] = {Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(getContext(), cameraPermission[0]) == PackageManager.PERMISSION_GRANTED) {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, CAMERA_REQUEST_CODE);

//            final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            pictureUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,pictureUri);
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), cameraPermission, REQUEST_IMAGE_CAPTURE);
        }
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new   File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        verifyPermissions();
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
