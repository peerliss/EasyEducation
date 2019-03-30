package au.com.easyeducation.easyeducation_3.Fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationNewActivity;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApply19Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private boolean buttonSelected = false;
    private Button nextButton;
    private boolean noSelected = false;
    private boolean yesSelected = false;

    public CourseApply19Fragment() {
        // Required empty public constructor
    }

    public static CourseApply19Fragment newInstance() {
        CourseApply19Fragment fragment = new CourseApply19Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    private Button mCurrentlyStudyingNo;
    private Button mCurrentlyStudyingYes;

    private Drawable selectedBG;
    private Drawable unSelectedBG;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_19, container, false);

        mCurrentlyStudyingNo = rootView.findViewById(R.id.courseApplyCurrentlyStudying_No_Button);
        mCurrentlyStudyingYes = rootView.findViewById(R.id.courseApplyCurrentlyStudying_Yes_Button);

        selectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_selected);
        unSelectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_unselected);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("currentlyStudying") != null) {
                    if (documentSnapshot.getString("currentlyStudying").matches("No")) {
                        mCurrentlyStudyingNo.performClick();
                    }
                    if (documentSnapshot.getString("currentlyStudying").matches("Yes")) {
                        mCurrentlyStudyingYes.performClick();
                    }
                }
            }
        });

        mCurrentlyStudyingNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                mCurrentlyStudyingNo.setBackground(selectedBG);

                userRef.update("currentlyStudying", "No");

                return false;
            }
        });

        mCurrentlyStudyingNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                mCurrentlyStudyingNo.setBackground(selectedBG);
                yesSelected = false;
                noSelected = true;
            }
        });

        mCurrentlyStudyingYes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                mCurrentlyStudyingYes.setBackground(selectedBG);

                userRef.update("currentlyStudying", "Yes");

                return false;
            }
        });

        mCurrentlyStudyingYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                mCurrentlyStudyingYes.setBackground(selectedBG);
                noSelected = false;
                yesSelected = true;
            }
        });


        nextButton = getActivity().findViewById(R.id.courseApplicationNextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFields()) {
                    return;
                }
                if (noSelected) {
                    ((CourseApplicationNewActivity) getActivity()).addFragment(18);
                }
                if (yesSelected) {
                    ((CourseApplicationNewActivity) getActivity()).addFragment(17);
                }
            }
        });

        return rootView;
    }

    private boolean validateFields() {
        boolean valid =  true;

        if (!buttonSelected) {
            Toast.makeText(getContext(), "Please select Yes or No.", Toast.LENGTH_SHORT).show();
            valid =  false;
        }

        return valid;
    }

    private void unSelectAllButtons() {
        buttonSelected = true;

        mCurrentlyStudyingNo.setBackground(unSelectedBG);
        mCurrentlyStudyingYes.setBackground(unSelectedBG);
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