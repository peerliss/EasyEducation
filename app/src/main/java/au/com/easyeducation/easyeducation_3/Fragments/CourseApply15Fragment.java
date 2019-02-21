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

    private Boolean medicalButtonPressed = false;
    private Boolean legalButtonPressed = false;
    private Boolean visaButtonPressed = false;
    private Boolean aatButtonPressed = false;
    private Boolean otherButtonPressed = false;

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

        selectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_selected);
        unSelectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_unselected);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("issuesMedical") != null) {
                    if (documentSnapshot.getString("issuesMedical").matches("Yes")) {
                        medicalButton.setBackground(selectedBG);
                        medicalButtonPressed = true;
                    }
                }
                if (documentSnapshot.getString("issuesLegal") != null) {
                    if (documentSnapshot.getString("issuesLegal").matches("Yes")) {
                        legalButton.setBackground(selectedBG);
                        legalButtonPressed = true;
                    }
                }
                if (documentSnapshot.getString("issuesVisa") != null) {
                    if (documentSnapshot.getString("issuesVisa").matches("Yes")) {
                        visaButton.setBackground(selectedBG);
                        visaButtonPressed = true;
                    }
                }
                if (documentSnapshot.getString("issuesAAT") != null) {
                    if (documentSnapshot.getString("issuesAAT").matches("Yes")) {
                        aatButton.setBackground(selectedBG);
                        aatButtonPressed = true;
                    }
                }
                if (documentSnapshot.getString("issuesOther") != null) {
                    if (documentSnapshot.getString("issuesOther").matches("Yes")) {
                        otherButton.setBackground(selectedBG);
                        otherButtonPressed = true;
                    }
                }
            }
        });

        medicalButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!medicalButtonPressed) {
                    medicalButton.setBackground(selectedBG);
                    userRef.update("issuesMedical", "Yes");
                } else {
                    medicalButton.setBackground(unSelectedBG);
                    userRef.update("issuesMedical", "No");
                }

                return false;
            }
        });

        medicalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!medicalButtonPressed) {
                    medicalButton.setBackground(selectedBG);
                    medicalButtonPressed = true;
                } else {
                    medicalButton.setBackground(unSelectedBG);
                    medicalButtonPressed = false;
                }
            }
        });

        legalButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!medicalButtonPressed) {
                    legalButton.setBackground(selectedBG);
                    userRef.update("issuesLegal", "Yes");
                }
                else {
                    legalButton.setBackground(unSelectedBG);
                    userRef.update("issuesLegal", "No");
                }

                return false;
            }
        });

        legalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!legalButtonPressed) {
                    legalButton.setBackground(selectedBG);
                    legalButtonPressed = true;
                } else {
                    legalButton.setBackground(unSelectedBG);
                    legalButtonPressed = false;
                }
            }
        });

        visaButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!visaButtonPressed) {
                    visaButton.setBackground(selectedBG);
                    userRef.update("issuesVisa", "Yes");
                }
                else {
                    visaButton.setBackground(unSelectedBG);
                    userRef.update("issuesVisa", "No");
                }

                return false;
            }
        });

        visaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!visaButtonPressed) {
                    visaButton.setBackground(selectedBG);
                    visaButtonPressed = true;
                } else {
                    visaButton.setBackground(unSelectedBG);
                    visaButtonPressed = false;
                }
            }
        });

        aatButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!aatButtonPressed) {
                    aatButton.setBackground(selectedBG);
                    userRef.update("issuesAAT", "Yes");
                }
                else {
                    aatButton.setBackground(unSelectedBG);
                    userRef.update("issuesAAT", "No");
                }

                return false;
            }
        });

        aatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!aatButtonPressed) {
                    aatButton.setBackground(selectedBG);
                    aatButtonPressed = true;
                } else {
                    aatButton.setBackground(unSelectedBG);
                    aatButtonPressed = false;
                }
            }
        });

        otherButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!otherButtonPressed) {
                    otherButton.setBackground(selectedBG);
                    userRef.update("issuesOther", "Yes");
                }
                else {
                    otherButton.setBackground(unSelectedBG);
                    userRef.update("issuesOther", "No");
                }

                return false;
            }
        });

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!otherButtonPressed) {
                    otherButton.setBackground(selectedBG);
                    otherButtonPressed = true;
                } else {
                    otherButton.setBackground(unSelectedBG);
                    otherButtonPressed = false;
                }
            }
        });

        return rootView;
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
