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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import au.com.easyeducation.easyeducation_3.R;

public class CourseApply15Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply15Fragment() {
        // Required empty public constructor
    }

    public static CourseApply15Fragment newInstance() {
        CourseApply15Fragment fragment = new CourseApply15Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    private Button medicalButton;
    private Button legalButton;
    private Button visaButton;
    private Button aatButton;
    private Button otherButton;
    private Button noneButton;

    private Drawable selectedBG;
    private Drawable unSelectedBG;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_15, container, false);

        medicalButton = rootView.findViewById(R.id.courseApplyIssues_Medical_Button);
        legalButton = rootView.findViewById(R.id.courseApplyIssues_Legal_Button);
        visaButton = rootView.findViewById(R.id.courseApplyIssues_Visa_Button);
        aatButton = rootView.findViewById(R.id.courseApplyIssues_AAT_Button);
        otherButton = rootView.findViewById(R.id.courseApplyIssues_Other_Button);
        noneButton = rootView.findViewById(R.id.courseApplyIssues_None_Button);

        selectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_selected);
        unSelectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_unselected);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("issuesDetails") != null) {
                    if (documentSnapshot.getString("issuesDetails").matches("Medical")) {
                        medicalButton.performClick();
                    }
                    if (documentSnapshot.getString("issuesDetails").matches("Legal")) {
                        legalButton.performClick();
                    }
                    if (documentSnapshot.getString("issuesDetails").matches("Visa")) {
                        visaButton.performClick();
                    }
                    if (documentSnapshot.getString("issuesDetails").matches("AAT")) {
                        aatButton.performClick();
                    }
                    if (documentSnapshot.getString("issuesDetails").matches("Other")) {
                        otherButton.performClick();
                    }
                    if (documentSnapshot.getString("issuesDetails").matches("None")) {
                        noneButton.performClick();
                    }
                }
                else if (documentSnapshot.getString("issuesDetails") == null) {
                    noneButton.performClick();
                }
            }
        });

        medicalButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                medicalButton.setBackground(selectedBG);

                userRef.update("issuesDetails", "Medical");

                return false;
            }
        });

        medicalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                medicalButton.setBackground(selectedBG);
            }
        });

        legalButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                legalButton.setBackground(selectedBG);

                userRef.update("issuesDetails", "Legal");

                return false;
            }
        });

        legalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                legalButton.setBackground(selectedBG);
            }
        });

        visaButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                visaButton.setBackground(selectedBG);

                userRef.update("issuesDetails", "Visa");

                return false;
            }
        });

        visaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                visaButton.setBackground(selectedBG);
            }
        });

        aatButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                aatButton.setBackground(selectedBG);

                userRef.update("issuesDetails", "AAT");

                return false;
            }
        });

        aatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                aatButton.setBackground(selectedBG);
            }
        });

        otherButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                otherButton.setBackground(selectedBG);

                userRef.update("issuesDetails", "Other");

                return false;
            }
        });

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                otherButton.setBackground(selectedBG);
            }
        });

        noneButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                noneButton.setBackground(selectedBG);

                userRef.update("issuesDetails", "None");

                return false;
            }
        });

        noneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                noneButton.setBackground(selectedBG);
            }
        });

        return rootView;
    }

    private void unSelectAllButtons() {
        medicalButton.setBackground(unSelectedBG);
        legalButton.setBackground(unSelectedBG);
        visaButton.setBackground(unSelectedBG);
        aatButton.setBackground(unSelectedBG);
        otherButton.setBackground(unSelectedBG);
        noneButton.setBackground(unSelectedBG);
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
