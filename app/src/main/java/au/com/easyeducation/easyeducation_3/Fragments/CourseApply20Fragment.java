package au.com.easyeducation.easyeducation_3.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.Date;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationNewActivity;
import au.com.easyeducation.easyeducation_3.R;

import static android.app.Activity.RESULT_OK;

public class CourseApply20Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button nextButton;

    public CourseApply20Fragment() {
        // Required empty public constructor
    }

    public static CourseApply20Fragment newInstance() {
        CourseApply20Fragment fragment = new CourseApply20Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText mCurrentQualificationName;
    private EditText mCurrentQualificationInstitution;

    private DocumentReference userRef;

    // Camera functionality - variables
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference currentQualificationPhotoRef;
    private Uri photoURI;
    private ScrollView mCurrentQualificationScrollView;
    private LinearLayout mCurrentQualificationPhotoLayoutButton;
    private LinearLayout mViewCurrentQualificationPhotoLayout;
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
        View rootView = inflater.inflate(R.layout.fragment_course_apply_20, container, false);

        mCurrentQualificationName = rootView.findViewById(R.id.courseApplyCurrentQualification_Name_ET);
        mCurrentQualificationInstitution = rootView.findViewById(R.id.courseApplyCurrentQualification_Institute_ET);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        // Camera functionality - initialize
        firebaseStorage = FirebaseStorage.getInstance();
        mCurrentQualificationPhotoLayoutButton = rootView.findViewById(R.id.courseApplyTakeCurrentQualificationPhoto_Layout);
        mCurrentQualificationScrollView = rootView.findViewById(R.id.courseApplyCurrentQualification_Scrollview);
        mViewCurrentQualificationPhotoLayout = rootView.findViewById(R.id.courseApplyViewCurrentQualificationPhoto_Layout);
        progressDialog = new ProgressDialog(getContext());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("currentQualificationName") != null) {
                    mCurrentQualificationName.setText(documentSnapshot.getString("currentQualificationName"));
                }
                if (documentSnapshot.getString("currentQualificationInstitution") != null) {
                    mCurrentQualificationInstitution.setText(documentSnapshot.getString("currentQualificationInstitution"));
                }
                // Camera functionality - initialize
                if (documentSnapshot.getString("currentQualificationPhotoTakenAmount") != null) {
                    photoTakenAmount = Integer.valueOf(documentSnapshot.getString("currentQualificationPhotoTakenAmount"));
                    loadImages(imageLoadIndex);
                }
            }
        });

        mCurrentQualificationName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mCurrentQualificationName.length() > 0) {
                    userRef.update("currentQualificationName", mCurrentQualificationName.getText().toString().trim());
                }
            }
        });

        mCurrentQualificationInstitution.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mCurrentQualificationInstitution.length() > 0) {
                    userRef.update("currentQualificationInstitution", mCurrentQualificationInstitution.getText().toString().trim());
                }
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
        mCurrentQualificationPhotoLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoTakenAmount <= 4) {
                    try {
                        verifyPermissions(photoTakenAmount);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "CurrentQualification Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Can only take 5 Confirmation of Enrolment photos", Toast.LENGTH_LONG).show();
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
        String currentQualificationPhotoName = "currentQualificationPhoto_" + String.valueOf((photoNumber)) + ".jpg";
        currentQualificationPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/CurrentQualification/" + currentQualificationPhotoName);

        currentQualificationPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewCurrentQualificationPhotoLayout, false);
                mViewCurrentQualificationPhotoLayout.addView(photo_imageView);
                ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                Glide.with(mViewCurrentQualificationPhotoLayout).load(uri).into(imageView);

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
                View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewCurrentQualificationPhotoLayout, false);
                mViewCurrentQualificationPhotoLayout.addView(photo_imageView);
                ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                Glide.with(imageView).load(photoURI).into(imageView);

                String currentQualificationPhotoName = "currentQualificationPhoto_" + (photoTakenAmount + 1) + ".jpg";
                currentQualificationPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/CurrentQualification/" + currentQualificationPhotoName);

                currentQualificationPhotoRef.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        photoTakenAmount++;
                        userRef.update("currentQualificationPhotoTakenAmount", String.valueOf(photoTakenAmount));
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
        mCurrentQualificationScrollView.post(new Runnable() {
            @Override
            public void run() {
//                mCurrentQualificationScrollView.scrollTo(0, mCurrentQualificationPhotoLayoutButton.getBottom());
                mCurrentQualificationScrollView.smoothScrollTo(0, mCurrentQualificationPhotoLayoutButton.getBottom());
            }
        });
    }

    private void verifyPermissions(int currentQualificationPhotoNumber) {
        Log.d("Verify Camera Permission", "verifyPermissions: asking user for permissions");
        String cameraPermission[] = {Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(getContext(), cameraPermission[0]) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent(currentQualificationPhotoNumber);
        } else {
            ActivityCompat.requestPermissions(getActivity(), cameraPermission, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakePictureIntent(int currentQualificationPhotoNumber) {
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
                    startActivityForResult(takePictureIntent, currentQualificationPhotoNumber);
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

        if (TextUtils.isEmpty(mCurrentQualificationName.getText()) || mCurrentQualificationName.length() == 0) {
            mCurrentQualificationName.setError("Required.");
            valid = false;
        } else {
            mCurrentQualificationName.setError(null);
        }

        if (TextUtils.isEmpty(mCurrentQualificationInstitution.getText()) || mCurrentQualificationInstitution.length() == 0) {
            mCurrentQualificationInstitution.setError("Required.");
            valid = false;
        } else {
            mCurrentQualificationInstitution.setError(null);
        }

        // Camera functionality
        if (!photoTaken) {
            Toast.makeText(getContext(), "Please take valid photo of Current Qualification", Toast.LENGTH_LONG).show();
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
