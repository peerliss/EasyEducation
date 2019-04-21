package au.com.easyeducation.easyeducation_3.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationNewActivity;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApply7Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private boolean buttonSelected = false;
    private Button nextButton;

    public CourseApply7Fragment() {
        // Required empty public constructor
    }

    public static CourseApply7Fragment newInstance() {
        CourseApply7Fragment fragment = new CourseApply7Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;
    private StorageReference signatureRef;

    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;

    private ProgressBar mProgressBar;

    private int amountOfTimesSigned = 0;
    private int amountOfTimesSaved = 0;

    private Drawable selectedBG;
    private Drawable unSelectedBG;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_7, container, false);

        selectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_selected);
        unSelectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_unselected);

        mSignaturePad = rootView.findViewById(R.id.courseApplySignature_Pad);
        mClearButton = rootView.findViewById(R.id.courseApply_Signature_Clear_Button);
        mSaveButton = rootView.findViewById(R.id.courseApply_Signature_Save_Button);

        mProgressBar = rootView.findViewById(R.id.courseApply_Signature_ProgressBar);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRef = db.collection("users").document(mAuth.getUid());

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        signatureRef = firebaseStorage.getReference("users/" + mAuth.getUid() + "/signature.png");

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                mClearButton.setEnabled(true);
                mSaveButton.setEnabled(true);

                amountOfTimesSigned++;
            }

            @Override
            public void onClear() {
                mClearButton.setEnabled(false);
                mSaveButton.setEnabled(false);

                buttonSelected = false;
            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.setEnabled(false);
                mProgressBar.setVisibility(View.VISIBLE);

                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = signatureRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressBar.setVisibility(View.GONE);
                        mSignaturePad.setEnabled(true);
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        userRef.update("signedApplicationForm", "Failed - " + e.getMessage());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mProgressBar.setVisibility(View.GONE);
                        mSignaturePad.setEnabled(true);
                        Toast.makeText(getContext(), "Signature successfully saved", Toast.LENGTH_SHORT).show();

                        userRef.update("signedApplicationForm", "Yes");

                        buttonSelected = true;
                        amountOfTimesSaved++; // add if (amountOfTimesSigned != amountOfTimesSaved) then save when applying
                    }
                });
            }
        });

        nextButton = getActivity().findViewById(R.id.courseApplicationNextButton);
        nextButton.setText("Apply");
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFields()) {
                    return;
                }
                ((CourseApplicationNewActivity) getActivity()).completeApplication();

//                Toast.makeText(getContext(), "Course Application Submitted", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    private boolean validateFields() {
        boolean valid = true;

        if (!buttonSelected) {
            Toast.makeText(getContext(), "Please sign and press save.", Toast.LENGTH_SHORT).show();
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