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
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationNewActivity;
import au.com.easyeducation.easyeducation_3.R;

import static android.app.Activity.RESULT_OK;

public class CourseApply23Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply23Fragment() {
        // Required empty public constructor
    }

    public static CourseApply23Fragment newInstance() {
        CourseApply23Fragment fragment = new CourseApply23Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Button nextButton;

    private EditText mPreviousName;

    private DocumentReference userRef;

    // Camera functionality - variables
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference nameChangedPhotoRef;
    private Uri photoURI;
    private ScrollView mNameChangedScrollView;
    private LinearLayout mNameChangedPhotoLayoutButton;
    private LinearLayout mViewNameChangedPhotoLayout;
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
        View rootView = inflater.inflate(R.layout.fragment_course_apply_23, container, false);

        mPreviousName = rootView.findViewById(R.id.courseApplyNameChanged_PreviousName_ET);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        // Camera functionality - initialize
        firebaseStorage = FirebaseStorage.getInstance();
        mNameChangedPhotoLayoutButton = rootView.findViewById(R.id.courseApplyTakeNameChangedPhoto_Layout);
        mNameChangedScrollView = rootView.findViewById(R.id.courseApplyNameChanged_Scrollview);
        mViewNameChangedPhotoLayout = rootView.findViewById(R.id.courseApplyViewNameChangedPhoto_Layout);
        progressDialog = new ProgressDialog(getContext());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("previousName") != null) {
                    mPreviousName.setText(documentSnapshot.getString("previousName"));
                }
                // Camera functionality - initialize
                if (documentSnapshot.getString("nameChangedPhotoTakenAmount") != null) {
                    photoTakenAmount = Integer.valueOf(documentSnapshot.getString("nameChangedPhotoTakenAmount"));
                    loadImages(imageLoadIndex);
                }
            }
        });

        mPreviousName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mPreviousName.length() > 0) {
                    userRef.update("previousName", mPreviousName.getText().toString().trim());
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
                ((CourseApplicationNewActivity) getActivity()).addFragment(21);
            }
        });

        // Camera functionality - button
        mNameChangedPhotoLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoTakenAmount <= 4) {
                    try {
                        verifyPermissions(photoTakenAmount);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "NameChanged Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Can only take 5 Name Change Document photos", Toast.LENGTH_LONG).show();
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
        String nameChangedPhotoName = "nameChangedPhoto_" + String.valueOf((photoNumber)) + ".jpg";
        nameChangedPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/NameChanged/" + nameChangedPhotoName);

        nameChangedPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewNameChangedPhotoLayout, false);
                    mViewNameChangedPhotoLayout.addView(photo_imageView);
                    ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                    Glide.with(mViewNameChangedPhotoLayout).load(uri).into(imageView);

                    addimageLoadIndexAmount();
                    photoTaken = true;

                    if (imageLoadIndex <= photoTakenAmount) {
                        loadImages(imageLoadIndex);
                    }
                } catch (Exception e) {
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewNameChangedPhotoLayout, false);
                mViewNameChangedPhotoLayout.addView(photo_imageView);
                ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                Glide.with(imageView).load(photoURI).into(imageView);

                String nameChangedPhotoName = "nameChangedPhoto_" + (photoTakenAmount + 1) + ".jpg";
                nameChangedPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/NameChanged/" + nameChangedPhotoName);

                nameChangedPhotoRef.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        photoTakenAmount++;
                        userRef.update("nameChangedPhotoTakenAmount", String.valueOf(photoTakenAmount));
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
        mNameChangedScrollView.post(new Runnable() {
            @Override
            public void run() {
//                mNameChangedScrollView.scrollTo(0, mNameChangedPhotoLayoutButton.getBottom());
                mNameChangedScrollView.smoothScrollTo(0, mNameChangedPhotoLayoutButton.getBottom());
            }
        });
    }

    private void verifyPermissions(int nameChangedPhotoNumber) {
        Log.d("Verify Camera Permission", "verifyPermissions: asking user for permissions");
        String cameraPermission[] = {Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(getContext(), cameraPermission[0]) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent(nameChangedPhotoNumber);
        } else {
            ActivityCompat.requestPermissions(getActivity(), cameraPermission, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakePictureIntent(int nameChangedPhotoNumber) {
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
                    startActivityForResult(takePictureIntent, nameChangedPhotoNumber);
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

        if (TextUtils.isEmpty(mPreviousName.getText()) || mPreviousName.length() == 0) {
            mPreviousName.setError("Required.");
            valid = false;
        } else {
            mPreviousName.setError(null);
        }

        // Camera functionality
        if (!photoTaken) {
            Toast.makeText(getContext(), "Please take valid photo of Name Change Documents", Toast.LENGTH_LONG).show();
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
