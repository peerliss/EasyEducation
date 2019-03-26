package au.com.easyeducation.easyeducation_3.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationNewActivity;
import au.com.easyeducation.easyeducation_3.R;

import static android.app.Activity.RESULT_OK;

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

    // Camera functionality - variables
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference highestQualificationPhotoRef;
    private Uri photoURI;
    private ScrollView mHighestQualificationScrollView;
    private LinearLayout mHighestQualificationPhotoLayoutButton;
    private LinearLayout mHighestQualification2PhotoLayoutButton;
    private LinearLayout mHighestQualification3PhotoLayoutButton;
    private LinearLayout mViewHighestQualificationPhotoLayout;
    private LinearLayout mViewHighestQualification2PhotoLayout;
    private LinearLayout mViewHighestQualification3PhotoLayout;
    private int photoTakenAmount_Q1;
    private int photoTakenAmount_Q2;
    private int photoTakenAmount_Q3;
    private int imageLoadIndex = 1;
    private int imageLoadIndex_Q2 = 1;
    private int imageLoadIndex_Q3 = 1;
    private boolean photoTaken_Q1 = false;
    private boolean photoTaken_Q2 = false;
    private boolean photoTaken_Q3 = false;
    private ProgressDialog progressDialog;
    private String currentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_CAPTURE_Q2 = 2;
    private static final int REQUEST_IMAGE_CAPTURE_Q3 = 3;

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

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        // Camera functionality - initialize
        firebaseStorage = FirebaseStorage.getInstance();
        mHighestQualificationPhotoLayoutButton = rootView.findViewById(R.id.courseApplyTakeHighestQualificationPhoto_Layout);
        mHighestQualification2PhotoLayoutButton = rootView.findViewById(R.id.courseApplyTakeHighestQualification2Photo_Layout);
        mHighestQualification3PhotoLayoutButton = rootView.findViewById(R.id.courseApplyTakeHighestQualification3Photo_Layout);
        mHighestQualificationScrollView = rootView.findViewById(R.id.courseApplyHighestQualification_Scrollview);
        mViewHighestQualificationPhotoLayout = rootView.findViewById(R.id.courseApplyViewHighestQualificationPhoto_Layout);
        mViewHighestQualification2PhotoLayout = rootView.findViewById(R.id.courseApplyViewHighestQualification2Photo_Layout);
        mViewHighestQualification3PhotoLayout = rootView.findViewById(R.id.courseApplyViewHighestQualification3Photo_Layout);
        progressDialog = new ProgressDialog(getContext());

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
                    mAddQualificationButton.setVisibility(View.GONE);
                }
                if (documentSnapshot.getString("highestQualification3Institution") != null) {
                    mHighestQualification3Institution.setText(documentSnapshot.getString("highestQualification3Institution"));
                }
                if (documentSnapshot.get("highestQualification3Country") != null) {
                    mQualification3Country.setDefaultCountryUsingNameCode(documentSnapshot.getString("highestQualification3Country"));
                    mQualification3Country.resetToDefaultCountry();
                }
                // Camera functionality - initialize
                if (documentSnapshot.getString("highestQualificationPhotoTakenAmount") != null) {
                    photoTakenAmount_Q1 = Integer.valueOf(documentSnapshot.getString("highestQualificationPhotoTakenAmount"));
                    loadImages(imageLoadIndex, 1);
                }
                // Camera functionality - initialize
                if (documentSnapshot.getString("highestQualification2PhotoTakenAmount") != null) {
                    photoTakenAmount_Q2 = Integer.valueOf(documentSnapshot.getString("highestQualification2PhotoTakenAmount"));
                    loadImages(imageLoadIndex, 2);
                }
                // Camera functionality - initialize
                if (documentSnapshot.getString("highestQualification3PhotoTakenAmount") != null) {
                    photoTakenAmount_Q3 = Integer.valueOf(documentSnapshot.getString("highestQualification3PhotoTakenAmount"));
                    loadImages(imageLoadIndex, 3);
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
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Image Load failure", e.getMessage());
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                userRef.update("highestQualificationCountryName", mQualificationCountry.getSelectedCountryName());
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
                userRef.update("highestQualification2CountryName", mQualification2Country.getSelectedCountryName());
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
                userRef.update("highestQualification3CountryName", mQualification3Country.getSelectedCountryName());
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

        // Camera functionality - button
        mHighestQualificationPhotoLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoTakenAmount_Q1 <= 4) {
                    try {
                        verifyPermissions(1);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "HighestQualification Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Can only take 5 Highest Qualification Document photos", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Camera functionality - 2 - button
        mHighestQualification2PhotoLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoTakenAmount_Q2 <= 4) {
                    try {
                        verifyPermissions(2);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "HighestQualification2 Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Can only take 5 Highest Qualification 2 Document photos", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Camera functionality - 3 - button
        mHighestQualification3PhotoLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoTakenAmount_Q3 <= 4) {
                    try {
                        verifyPermissions(3);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "HighestQualification3 Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Can only take 5 Highest Qualification 3 Document photos", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    // Camera functionality
    private void addimageLoadIndexAmount() {
        imageLoadIndex++;
    }

    private void loadImages(int photoNumber, int qualificationNumber) {
        switch (qualificationNumber) {
            case 1:
                String highestQualificationPhotoName = "highestQualificationPhoto_" + String.valueOf((photoNumber)) + ".jpg";
                highestQualificationPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/HighestQualification/" + "/1/" + highestQualificationPhotoName);

                highestQualificationPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewHighestQualificationPhotoLayout, false);
                        mViewHighestQualificationPhotoLayout.addView(photo_imageView);
                        ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                        Glide.with(mViewHighestQualificationPhotoLayout).load(uri).into(imageView);

                        addimageLoadIndexAmount();
                        photoTaken_Q1 = true;

                        if (imageLoadIndex <= photoTakenAmount_Q1) {
                            loadImages(imageLoadIndex, 1);
                        }
                    }
                });
                break;
            case 2:
                String highestQualification2PhotoName = "highestQualification2Photo_" + String.valueOf((photoNumber)) + ".jpg";
                highestQualificationPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/HighestQualification/" + "/2/" + highestQualification2PhotoName);

                highestQualificationPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewHighestQualification2PhotoLayout, false);
                        mViewHighestQualification2PhotoLayout.addView(photo_imageView);
                        ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                        Glide.with(mViewHighestQualification2PhotoLayout).load(uri).into(imageView);

                        imageLoadIndex_Q2++;
                        photoTaken_Q2 = true;

                        if (imageLoadIndex_Q2 <= photoTakenAmount_Q2) {
                            loadImages(imageLoadIndex_Q2, 2);
                        }
                    }
                });
                break;
            case 3:
                String highestQualification3PhotoName = "highestQualification3Photo_" + String.valueOf((photoNumber)) + ".jpg";
                highestQualificationPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/HighestQualification/" + "/3/" + highestQualification3PhotoName);

                highestQualificationPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewHighestQualification3PhotoLayout, false);
                        mViewHighestQualification3PhotoLayout.addView(photo_imageView);
                        ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                        Glide.with(mViewHighestQualification3PhotoLayout).load(uri).into(imageView);

                        imageLoadIndex_Q3++;
                        photoTaken_Q3 = true;

                        if (imageLoadIndex_Q3 <= photoTakenAmount_Q3) {
                            loadImages(imageLoadIndex_Q3, 3);
                        }
                    }
                });
                break;
        }
    }

//    private void loadImages(int photoNumber) {
//        String highestQualificationPhotoName = "highestQualificationPhoto_" + String.valueOf((photoNumber)) + ".jpg";
//        highestQualificationPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/HighestQualification/" + highestQualificationPhotoName);
//
//        highestQualificationPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewHighestQualificationPhotoLayout, false);
//                mViewHighestQualificationPhotoLayout.addView(photo_imageView);
//                ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
//                Glide.with(mViewHighestQualificationPhotoLayout).load(uri).into(imageView);
//
//                addimageLoadIndexAmount();
//                photoTaken_Q1 = true;
//
//                if (imageLoadIndex <= photoTakenAmount_Q1) {
//                    loadImages(imageLoadIndex);
//                }
//            }
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            progressDialog.setMessage("Uploading Image...");
            progressDialog.show();
            try {
                switch (requestCode) {
                    case 1:
                        View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewHighestQualificationPhotoLayout, false);
                        mViewHighestQualificationPhotoLayout.addView(photo_imageView);
                        ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                        Glide.with(imageView).load(photoURI).into(imageView);

                        String highestQualificationPhotoName = "highestQualificationPhoto_" + (photoTakenAmount_Q1 + 1) + ".jpg";
                        highestQualificationPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/HighestQualification/" + "/1/" + highestQualificationPhotoName);

                        highestQualificationPhotoRef.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                photoTakenAmount_Q1++;
                                userRef.update("highestQualificationPhotoTakenAmount", String.valueOf(photoTakenAmount_Q1));
                                focusToTakePhotoButton(1);

                                photoTaken_Q1 = true;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Image save failure - " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 2:
                        View photo_imageView_2 = getLayoutInflater().inflate(R.layout.photo_imageview, mViewHighestQualification2PhotoLayout, false);
                        mViewHighestQualification2PhotoLayout.addView(photo_imageView_2);
                        ImageView imageView_2 = photo_imageView_2.findViewById(R.id.photo_imageview);
                        Glide.with(imageView_2).load(photoURI).into(imageView_2);

                        String highestQualification2PhotoName = "highestQualification2Photo_" + (photoTakenAmount_Q2 + 1) + ".jpg";
                        highestQualificationPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/HighestQualification/" + "/2/" + highestQualification2PhotoName);

                        highestQualificationPhotoRef.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                photoTakenAmount_Q2++;
                                userRef.update("highestQualification2PhotoTakenAmount", String.valueOf(photoTakenAmount_Q2));
                                focusToTakePhotoButton(2);

                                photoTaken_Q2 = true;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Image save failure - " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 3:
                        View photo_imageView_3 = getLayoutInflater().inflate(R.layout.photo_imageview, mViewHighestQualification3PhotoLayout, false);
                        mViewHighestQualification3PhotoLayout.addView(photo_imageView_3);
                        ImageView imageView_3 = photo_imageView_3.findViewById(R.id.photo_imageview);
                        Glide.with(imageView_3).load(photoURI).into(imageView_3);

                        String highestQualification3PhotoName = "highestQualification3Photo_" + (photoTakenAmount_Q3 + 1) + ".jpg";
                        highestQualificationPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/HighestQualification/" + "/3/" + highestQualification3PhotoName);

                        highestQualificationPhotoRef.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                photoTakenAmount_Q3++;
                                userRef.update("highestQualification3PhotoTakenAmount", String.valueOf(photoTakenAmount_Q3));
                                focusToTakePhotoButton(3);

                                photoTaken_Q3 = true;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Image save failure - " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "OnActvitiyResult exception - " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//            progressDialog.setMessage("Uploading Image...");
//            progressDialog.show();
//            try {
//                View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewHighestQualificationPhotoLayout, false);
//                mViewHighestQualificationPhotoLayout.addView(photo_imageView);
//                ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
//                Glide.with(imageView).load(photoURI).into(imageView);
//
//                String highestQualificationPhotoName = "highestQualificationPhoto_" + (photoTakenAmount_Q1 + 1) + ".jpg";
//                highestQualificationPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/HighestQualification/" + "/1/" + highestQualificationPhotoName);
//
//                highestQualificationPhotoRef.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        progressDialog.dismiss();
//                        photoTakenAmount_Q1++;
//                        userRef.update("highestQualificationPhotoTakenAmount", String.valueOf(photoTakenAmount_Q1));
//                        focusToTakePhotoButton();
//
//                        photoTaken_Q1 = true;
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(), "Image save failure - " + e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//            } catch (Exception e) {
//                Toast.makeText(getContext(), "OnActvitiyResult exception - " + e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    private void focusToTakePhotoButton(final int qualificationFocusNumber) {
//        mHighestQualificationScrollView.post(new Runnable() {
//            @Override
//            public void run() {
//                switch (qualificationFocusNumber) {
//                    case 1:
//                        mHighestQualificationScrollView.smoothScrollTo(0, mHighestQualificationPhotoLayoutButton.getTop());
//                        break;
//                    case 2:
//                        mHighestQualificationScrollView.smoothScrollTo(0, mHighestQualification2PhotoLayoutButton.getTop());
//                        break;
//                    case 3:
//                        mHighestQualificationScrollView.smoothScrollTo(0, mHighestQualification3PhotoLayoutButton.getTop());
//                        break;
//                }
//            }
//        });
    }

    private void verifyPermissions(int highestQualificationPhotoNumber) {
        Log.d("Verify Camera Permission", "verifyPermissions: asking user for permissions");
        String cameraPermission[] = {Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(getContext(), cameraPermission[0]) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent(highestQualificationPhotoNumber);
        } else {
            ActivityCompat.requestPermissions(getActivity(), cameraPermission, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakePictureIntent(int highestQualificationPhotoNumber) {
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
                    startActivityForResult(takePictureIntent, highestQualificationPhotoNumber);
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

        // Camera functionality
        if (!photoTaken_Q1) {
            Toast.makeText(getContext(), "Please take valid photo of your Highest Qualification", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (mQualification2Layout.getVisibility() == View.VISIBLE) {
            // Camera functionality
            if (!photoTaken_Q2) {
                Toast.makeText(getContext(), "Please take valid photo of your Second Highest Qualification", Toast.LENGTH_LONG).show();
                valid = false;
            }
        }

        if (mQualification3Layout.getVisibility() == View.VISIBLE) {
            // Camera functionality
            if (!photoTaken_Q3) {
                Toast.makeText(getContext(), "Please take valid photo of your Third Highest Qualification", Toast.LENGTH_LONG).show();
                valid = false;
            }
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
