package co.edcall.app.Fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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

import co.edcall.app.Activities.CourseApplicationNewActivity;
import co.edcall.app.R;

public class CourseApply17Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private boolean buttonSelected = false;
    private boolean noSelected = false;
    private boolean yesSelected = false;
    private Button nextButton;

    public CourseApply17Fragment() {
        // Required empty public constructor
    }

    public static CourseApply17Fragment newInstance() {
        CourseApply17Fragment fragment = new CourseApply17Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    private Button mHealthInsuranceYes;
    private Button mHealthInsuranceNo;

    private Drawable selectedBG;
    private Drawable unSelectedBG;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_17, container, false);

        mHealthInsuranceYes = rootView.findViewById(R.id.courseApplyHealthInsurance_Yes_Button);
        mHealthInsuranceNo = rootView.findViewById(R.id.courseApplyHealthInsurance_No_Button);

        selectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_selected);
        unSelectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_unselected);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("healthInsurance") != null) {
                    if (documentSnapshot.getString("healthInsurance").matches("Yes")) {
                        mHealthInsuranceYes.performClick();
                    }
                    if (documentSnapshot.getString("healthInsurance").matches("No")) {
                        mHealthInsuranceNo.performClick();
                    }
                }
            }
        });

        mHealthInsuranceYes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                mHealthInsuranceYes.setBackground(selectedBG);

                userRef.update("healthInsurance", "Yes");

                return false;
            }
        });

        mHealthInsuranceYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                mHealthInsuranceYes.setBackground(selectedBG);
                noSelected = false;
                yesSelected = true;
            }
        });

        mHealthInsuranceNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                mHealthInsuranceNo.setBackground(selectedBG);

                userRef.update("healthInsurance", "No");

                return false;
            }
        });

        mHealthInsuranceNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                mHealthInsuranceNo.setBackground(selectedBG);
                yesSelected = false;
                noSelected = true;
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
                    ((CourseApplicationNewActivity) getActivity()).addFragment(16);
                }
                if (yesSelected) {
                    ((CourseApplicationNewActivity) getActivity()).addFragment(15);
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

        mHealthInsuranceYes.setBackground(unSelectedBG);
        mHealthInsuranceNo.setBackground(unSelectedBG);
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