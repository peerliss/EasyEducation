package au.com.easyeducation.easyeducation_3.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class CourseApply16Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply16Fragment() {
        // Required empty public constructor
    }

    public static CourseApply16Fragment newInstance() {
        CourseApply16Fragment fragment = new CourseApply16Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Button nextButton;

    private DocumentReference userRef;

    private LinearLayout mMedicalLayout;
    private LinearLayout mLegalLayout;
    private LinearLayout mVisaLayout;
    private LinearLayout mAATLayout;
    private LinearLayout mOtherLayout;

    private EditText mMedicalIssues;
    private EditText mLegalIssues;
    private EditText mVisaIssues;
    private EditText mAATIssues;
    private EditText mOtherIssues;

    // Camera functionality - variables
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference issueDetailsPhotoRef;
    private Uri photoURI;
    private ScrollView mIssueDetailsScrollView;

    private LinearLayout mMedicalIssuesTakePhotoLayoutButton;
    private LinearLayout mLegalIssuesTakePhotoLayoutButton;
    private LinearLayout mVisaIssuesTakePhotoLayoutButton;
    private LinearLayout mAATIssuesTakePhotoLayoutButton;
    private LinearLayout mOtherIssuesTakePhotoLayoutButton;

    private LinearLayout mViewMedicalIssueDetailsPhotoLayout;
    private LinearLayout mViewLegalIssueDetailsPhotoLayout;
    private LinearLayout mViewVisaIssueDetailsPhotoLayout;
    private LinearLayout mViewAATIssueDetailsPhotoLayout;
    private LinearLayout mViewOtherIssueDetailsPhotoLayout;

    private int photoTakenAmount_Medical;
    private int photoTakenAmount_Legal;
    private int photoTakenAmount_Visa;
    private int photoTakenAmount_AAT;
    private int photoTakenAmount_Other;

    private int imageLoadIndex_Medical = 1;
    private int imageLoadIndex_Legal = 1;
    private int imageLoadIndex_Visa = 1;
    private int imageLoadIndex_AAT = 1;
    private int imageLoadIndex_Other = 1;

    private ProgressDialog progressDialog;
    private String currentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_16, container, false);

        mMedicalLayout = rootView.findViewById(R.id.courseApplyMedicalIssues_Layout);
        mLegalLayout = rootView.findViewById(R.id.courseApplyLegalIssues_Layout);
        mVisaLayout = rootView.findViewById(R.id.courseApplyVisaIssues_Layout);
        mAATLayout = rootView.findViewById(R.id.courseApplyAATIssues_Layout);
        mOtherLayout = rootView.findViewById(R.id.courseApplyOtherIssues_Layout);

        mMedicalIssues = rootView.findViewById(R.id.courseApplyMedicalIssues_ET);
        mLegalIssues = rootView.findViewById(R.id.courseApplyLegalIssues_ET);
        mVisaIssues = rootView.findViewById(R.id.courseApplyVisaIssues_ET);
        mAATIssues = rootView.findViewById(R.id.courseApplyAATIssues_ET);
        mOtherIssues = rootView.findViewById(R.id.courseApplyOtherIssues_ET);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        // Camera functionality - initialize
        firebaseStorage = FirebaseStorage.getInstance();

        mMedicalIssuesTakePhotoLayoutButton = rootView.findViewById(R.id.courseApplyTakeMedicalIssuesPhoto_Layout);
        mLegalIssuesTakePhotoLayoutButton = rootView.findViewById(R.id.courseApplyTakeLegalIssuesPhoto_Layout);
        mVisaIssuesTakePhotoLayoutButton = rootView.findViewById(R.id.courseApplyTakeVisaIssuesPhoto_Layout);
        mAATIssuesTakePhotoLayoutButton = rootView.findViewById(R.id.courseApplyTakeAATIssuesPhoto_Layout);
        mOtherIssuesTakePhotoLayoutButton = rootView.findViewById(R.id.courseApplyTakeOtherIssuesPhoto_Layout);

        mIssueDetailsScrollView = rootView.findViewById(R.id.courseApplyIssueDetails_Scrollview);

        mViewMedicalIssueDetailsPhotoLayout = rootView.findViewById(R.id.courseApplyViewIssuesMedicalPhoto_Layout);
        mViewLegalIssueDetailsPhotoLayout = rootView.findViewById(R.id.courseApplyViewIssuesLegalPhoto_Layout);
        mViewVisaIssueDetailsPhotoLayout = rootView.findViewById(R.id.courseApplyViewIssuesVisaPhoto_Layout);
        mViewAATIssueDetailsPhotoLayout = rootView.findViewById(R.id.courseApplyViewIssuesAATPhoto_Layout);
        mViewOtherIssueDetailsPhotoLayout = rootView.findViewById(R.id.courseApplyViewIssuesOtherPhoto_Layout);

        progressDialog = new ProgressDialog(getContext());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("issuesMedical") != null
                        && documentSnapshot.getString("issuesMedical").matches("Yes")) {
                    mMedicalLayout.setVisibility(View.VISIBLE);
                }
                if (documentSnapshot.getString("issuesLegal") != null
                        && documentSnapshot.getString("issuesLegal").matches("Yes")) {
                    mLegalLayout.setVisibility(View.VISIBLE);
                }
                if (documentSnapshot.getString("issuesVisa") != null
                        && documentSnapshot.getString("issuesVisa").matches("Yes")) {
                    mVisaLayout.setVisibility(View.VISIBLE);
                }
                if (documentSnapshot.getString("issuesAAT") != null
                        && documentSnapshot.getString("issuesAAT").matches("Yes")) {
                    mAATLayout.setVisibility(View.VISIBLE);
                }
                if (documentSnapshot.getString("issuesOther") != null
                        && documentSnapshot.getString("issuesOther").matches("Yes")) {
                    mOtherLayout.setVisibility(View.VISIBLE);
                }

                if (documentSnapshot.getString("issuesMedicalDetails") != null) {
                    mMedicalIssues.setText(documentSnapshot.getString("issuesMedicalDetails"));
                }
                if (documentSnapshot.getString("issuesLegalDetails") != null) {
                    mLegalIssues.setText(documentSnapshot.getString("issuesLegalDetails"));
                }
                if (documentSnapshot.getString("issuesVisaDetails") != null) {
                    mVisaIssues.setText(documentSnapshot.getString("issuesVisaDetails"));
                }
                if (documentSnapshot.getString("issuesAATDetails") != null) {
                    mAATIssues.setText(documentSnapshot.getString("issuesAATDetails"));
                }
                if (documentSnapshot.getString("issuesOtherDetails") != null) {
                    mOtherIssues.setText(documentSnapshot.getString("issuesOtherDetails"));
                }

                // Camera functionality - initialize
                if (documentSnapshot.getString("issueMedicalDetailsPhotoTakenAmount") != null) {
                    photoTakenAmount_Medical = Integer.valueOf(documentSnapshot.getString("issueMedicalDetailsPhotoTakenAmount"));
                    loadImages(imageLoadIndex_Medical, 1);
                }
                // Camera functionality - initialize
                if (documentSnapshot.getString("issueLegalDetailsPhotoTakenAmount") != null) {
                    photoTakenAmount_Legal = Integer.valueOf(documentSnapshot.getString("issueLegalDetailsPhotoTakenAmount"));
                    loadImages(imageLoadIndex_Legal, 2);
                }
                // Camera functionality - initialize
                if (documentSnapshot.getString("issueVisaDetailsPhotoTakenAmount") != null) {
                    photoTakenAmount_Visa = Integer.valueOf(documentSnapshot.getString("issueVisaDetailsPhotoTakenAmount"));
                    loadImages(imageLoadIndex_Visa, 3);
                }
                // Camera functionality - initialize
                if (documentSnapshot.getString("issueAATDetailsPhotoTakenAmount") != null) {
                    photoTakenAmount_AAT = Integer.valueOf(documentSnapshot.getString("issueAATDetailsPhotoTakenAmount"));
                    loadImages(imageLoadIndex_AAT, 4);
                }
                // Camera functionality - initialize
                if (documentSnapshot.getString("issueOtherDetailsPhotoTakenAmount") != null) {
                    photoTakenAmount_Other = Integer.valueOf(documentSnapshot.getString("issueOtherDetailsPhotoTakenAmount"));
                    loadImages(imageLoadIndex_Other, 5);
                }
            }
        });

        mMedicalIssues.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mMedicalIssues.length() > 0) {
                    userRef.update("issuesMedicalDetails", mMedicalIssues.getText().toString().trim());
                }
            }
        });

        mLegalIssues.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mLegalIssues.length() > 0) {
                    userRef.update("issuesLegalDetails", mLegalIssues.getText().toString().trim());
                }
            }
        });

        mVisaIssues.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mVisaIssues.length() > 0) {
                    userRef.update("issuesVisaDetails", mVisaIssues.getText().toString().trim());
                }
            }
        });

        mAATIssues.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mAATIssues.length() > 0) {
                    userRef.update("issuesAATDetails", mAATIssues.getText().toString().trim());
                }
            }
        });

        mOtherIssues.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!v.hasFocus() && mOtherIssues.length() > 0) {
                    userRef.update("issuesOtherDetails", mOtherIssues.getText().toString().trim());
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
                ((CourseApplicationNewActivity) getActivity()).addFragment(14);
            }
        });

        // Camera functionality - button
        mMedicalIssuesTakePhotoLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoTakenAmount_Medical <= 4) {
                    try {
                        verifyPermissions(1);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "IssueDetails Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Can only take 5 Document photos", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Camera functionality - button
        mLegalIssuesTakePhotoLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoTakenAmount_Legal <= 4) {
                    try {
                        verifyPermissions(2);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "IssueDetails Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Can only take 5 Document photos", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Camera functionality - button
        mVisaIssuesTakePhotoLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoTakenAmount_Visa <= 4) {
                    try {
                        verifyPermissions(3);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "IssueDetails Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Can only take 5 Document photos", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Camera functionality - button
        mAATIssuesTakePhotoLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoTakenAmount_AAT <= 4) {
                    try {
                        verifyPermissions(4);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "IssueDetails Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Can only take 5 Document photos", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Camera functionality - button
        mOtherIssuesTakePhotoLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoTakenAmount_Other <= 4) {
                    try {
                        verifyPermissions(5);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "IssueDetails Button click - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Can only take 5 Document photos", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    private void loadImages(int photoNumber, int qualificationNumber) {
        switch (qualificationNumber) {
            case 1:
                String issueMedicalDetailsPhotoName = "issueMedicalDetailsPhoto_" + String.valueOf((photoNumber)) + ".jpg";
                issueDetailsPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/IssueDetails/" + "/Medical/" + issueMedicalDetailsPhotoName);

                issueDetailsPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try {
                            View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewMedicalIssueDetailsPhotoLayout, false);
                            mViewMedicalIssueDetailsPhotoLayout.addView(photo_imageView);
                            ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                            Glide.with(mViewMedicalIssueDetailsPhotoLayout).load(uri).into(imageView);

                            imageLoadIndex_Medical++;

                            if (imageLoadIndex_Medical <= photoTakenAmount_Medical) {
                                loadImages(imageLoadIndex_Medical, 1);
                            }
                        } catch (Exception e) {
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Image Load Failure - Medical", e.getMessage());
                    }
                });
                break;
            case 2:
                String issueLegalDetailsPhotoName = "issueLegalDetailsPhoto_" + String.valueOf((photoNumber)) + ".jpg";
                issueDetailsPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/IssueDetails/" + "/Legal/" + issueLegalDetailsPhotoName);

                issueDetailsPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try {
                            View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewLegalIssueDetailsPhotoLayout, false);
                            mViewLegalIssueDetailsPhotoLayout.addView(photo_imageView);
                            ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                            Glide.with(mViewLegalIssueDetailsPhotoLayout).load(uri).into(imageView);

                            imageLoadIndex_Legal++;

                            if (imageLoadIndex_Legal <= photoTakenAmount_Legal) {
                                loadImages(imageLoadIndex_Legal, 2);
                            }
                        } catch (Exception e) {
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case 3:
                String issueVisaDetailsPhotoName = "issueVisaDetailsPhoto_" + String.valueOf((photoNumber)) + ".jpg";
                issueDetailsPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/IssueDetails/" + "/Visa/" + issueVisaDetailsPhotoName);

                issueDetailsPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try {
                            View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewVisaIssueDetailsPhotoLayout, false);
                            mViewVisaIssueDetailsPhotoLayout.addView(photo_imageView);
                            ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                            Glide.with(mViewVisaIssueDetailsPhotoLayout).load(uri).into(imageView);

                            imageLoadIndex_Visa++;

                            if (imageLoadIndex_Visa <= photoTakenAmount_Visa) {
                                loadImages(imageLoadIndex_Visa, 3);
                            }
                        } catch (Exception e) {
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case 4:
                String issueAATDetailsPhotoName = "issueAATDetailsPhoto_" + String.valueOf((photoNumber)) + ".jpg";
                issueDetailsPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/IssueDetails/" + "/AAT/" + issueAATDetailsPhotoName);

                issueDetailsPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try {
                            View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewAATIssueDetailsPhotoLayout, false);
                            mViewAATIssueDetailsPhotoLayout.addView(photo_imageView);
                            ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                            Glide.with(mViewAATIssueDetailsPhotoLayout).load(uri).into(imageView);

                            imageLoadIndex_AAT++;

                            if (imageLoadIndex_AAT <= photoTakenAmount_AAT) {
                                loadImages(imageLoadIndex_AAT, 4);
                            }
                        } catch (Exception e) {
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case 5:
                String issueOtherDetailsPhotoName = "issueOtherDetailsPhoto_" + String.valueOf((photoNumber)) + ".jpg";
                issueDetailsPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/IssueDetails/" + "/Other/" + issueOtherDetailsPhotoName);

                issueDetailsPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try {
                            View photo_imageView = getLayoutInflater().inflate(R.layout.photo_imageview, mViewOtherIssueDetailsPhotoLayout, false);
                            mViewOtherIssueDetailsPhotoLayout.addView(photo_imageView);
                            ImageView imageView = photo_imageView.findViewById(R.id.photo_imageview);
                            Glide.with(mViewOtherIssueDetailsPhotoLayout).load(uri).into(imageView);

                            imageLoadIndex_Other++;

                            if (imageLoadIndex_Other <= photoTakenAmount_Other) {
                                loadImages(imageLoadIndex_Other, 5);
                            }
                        } catch (Exception e) {
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            progressDialog.setMessage("Uploading Image...");
            progressDialog.show();
            try {
                switch (requestCode) {
                    case 1:
                        View photo_imageView_medical = getLayoutInflater().inflate(R.layout.photo_imageview, mViewMedicalIssueDetailsPhotoLayout, false);
                        mViewMedicalIssueDetailsPhotoLayout.addView(photo_imageView_medical);
                        ImageView imageView_medical = photo_imageView_medical.findViewById(R.id.photo_imageview);
                        Glide.with(imageView_medical).load(photoURI).into(imageView_medical);

                        String issueMedicalDetailsPhotoName = "issueMedicalDetailsPhoto_" + (photoTakenAmount_Medical + 1) + ".jpg";
                        issueDetailsPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/IssueDetails/" + "/Medical/" + issueMedicalDetailsPhotoName);

                        issueDetailsPhotoRef.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                photoTakenAmount_Medical++;
                                userRef.update("issueMedicalDetailsPhotoTakenAmount", String.valueOf(photoTakenAmount_Medical));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Image save failure - " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 2:
                        View photo_imageView_legal = getLayoutInflater().inflate(R.layout.photo_imageview, mViewLegalIssueDetailsPhotoLayout, false);
                        mViewLegalIssueDetailsPhotoLayout.addView(photo_imageView_legal);
                        ImageView imageView_legal = photo_imageView_legal.findViewById(R.id.photo_imageview);
                        Glide.with(imageView_legal).load(photoURI).into(imageView_legal);

                        String issueLegalDetailsPhotoName = "issueLegalDetailsPhoto_" + (photoTakenAmount_Legal + 1) + ".jpg";
                        issueDetailsPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/IssueDetails/" + "/Legal/" + issueLegalDetailsPhotoName);

                        issueDetailsPhotoRef.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                photoTakenAmount_Legal++;
                                userRef.update("issueLegalDetailsPhotoTakenAmount", String.valueOf(photoTakenAmount_Legal));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Image save failure - " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 3:
                        View photo_imageView_visa = getLayoutInflater().inflate(R.layout.photo_imageview, mViewVisaIssueDetailsPhotoLayout, false);
                        mViewVisaIssueDetailsPhotoLayout.addView(photo_imageView_visa);
                        ImageView imageView_visa = photo_imageView_visa.findViewById(R.id.photo_imageview);
                        Glide.with(imageView_visa).load(photoURI).into(imageView_visa);

                        String issueVisaDetailsPhotoName = "issueVisaDetailsPhoto_" + (photoTakenAmount_Visa + 1) + ".jpg";
                        issueDetailsPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/IssueDetails/" + "/Visa/" + issueVisaDetailsPhotoName);

                        issueDetailsPhotoRef.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                photoTakenAmount_Visa++;
                                userRef.update("issueVisaDetailsPhotoTakenAmount", String.valueOf(photoTakenAmount_Visa));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Image save failure - " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 4:
                        View photo_imageView_aat = getLayoutInflater().inflate(R.layout.photo_imageview, mViewAATIssueDetailsPhotoLayout, false);
                        mViewAATIssueDetailsPhotoLayout.addView(photo_imageView_aat);
                        ImageView imageView_aat = photo_imageView_aat.findViewById(R.id.photo_imageview);
                        Glide.with(imageView_aat).load(photoURI).into(imageView_aat);

                        String issueAATDetailsPhotoName = "issueAATDetailsPhoto_" + (photoTakenAmount_AAT + 1) + ".jpg";
                        issueDetailsPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/IssueDetails/" + "/AAT/" + issueAATDetailsPhotoName);

                        issueDetailsPhotoRef.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                photoTakenAmount_AAT++;
                                userRef.update("issueAATDetailsPhotoTakenAmount", String.valueOf(photoTakenAmount_AAT));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Image save failure - " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 5:
                        View photo_imageView_other = getLayoutInflater().inflate(R.layout.photo_imageview, mViewOtherIssueDetailsPhotoLayout, false);
                        mViewOtherIssueDetailsPhotoLayout.addView(photo_imageView_other);
                        ImageView imageView_other = photo_imageView_other.findViewById(R.id.photo_imageview);
                        Glide.with(imageView_other).load(photoURI).into(imageView_other);

                        String issueOtherDetailsPhotoName = "issueOtherDetailsPhoto_" + (photoTakenAmount_Other + 1) + ".jpg";
                        issueDetailsPhotoRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/IssueDetails/" + "/Other/" + issueOtherDetailsPhotoName);

                        issueDetailsPhotoRef.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                photoTakenAmount_Other++;
                                userRef.update("issueOtherDetailsPhotoTakenAmount", String.valueOf(photoTakenAmount_Other));
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

//    private void focusToTakePhotoButton(final int qualificationFocusNumber) {
//        mIssueDetailsScrollView.post(new Runnable() {
//            @Override
//            public void run() {
//                switch (qualificationFocusNumber) {
//                    case 1:
//                        mIssueDetailsScrollView.smoothScrollTo(0, mIssueDetailsPhotoLayoutButton.getTop());
//                        break;
//                    case 2:
//                        mIssueDetailsScrollView.smoothScrollTo(0, mIssueDetails2PhotoLayoutButton.getTop());
//                        break;
//                    case 3:
//                        mIssueDetailsScrollView.smoothScrollTo(0, mIssueDetails3PhotoLayoutButton.getTop());
//                        break;
//                }
//            }
//        });
//    }

    private void verifyPermissions(int issueDetailsPhotoNumber) {
        Log.d("Verify Camera Permission", "verifyPermissions: asking user for permissions");
        String cameraPermission[] = {Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(getContext(), cameraPermission[0]) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent(issueDetailsPhotoNumber);
        } else {
            ActivityCompat.requestPermissions(getActivity(), cameraPermission, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakePictureIntent(int issueDetailsPhotoNumber) {
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
                    startActivityForResult(takePictureIntent, issueDetailsPhotoNumber);
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

        if (mMedicalLayout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(mMedicalIssues.getText()) || mMedicalIssues.length() == 0) {
                mMedicalIssues.setError("Required.");
                valid = false;
            } else {
                mMedicalIssues.setError(null);
            }
        }

        if (mLegalLayout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(mLegalIssues.getText()) || mLegalIssues.length() == 0) {
                mLegalIssues.setError("Required.");
                valid = false;
            } else {
                mLegalIssues.setError(null);
            }
        }

        if (mVisaIssues.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(mVisaIssues.getText()) || mVisaIssues.length() == 0) {
                mVisaIssues.setError("Required.");
                valid = false;
            } else {
                mVisaIssues.setError(null);
            }
        }

        if (mAATLayout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(mAATIssues.getText()) || mAATIssues.length() == 0) {
                mAATIssues.setError("Required.");
                valid = false;
            } else {
                mAATIssues.setError(null);
            }
        }

        if (mOtherLayout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(mOtherIssues.getText()) || mOtherIssues.length() == 0) {
                mOtherIssues.setError("Required.");
                valid = false;
            } else {
                mOtherIssues.setError(null);
            }
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