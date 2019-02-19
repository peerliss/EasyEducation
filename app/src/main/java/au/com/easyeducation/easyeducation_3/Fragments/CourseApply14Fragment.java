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

public class CourseApply14Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply14Fragment() {
        // Required empty public constructor
    }

    public static CourseApply14Fragment newInstance() {
        CourseApply14Fragment fragment = new CourseApply14Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    private Button issuesNoButton;
    private Button issuesYesButton;

    private Drawable selectedBG;
    private Drawable unSelectedBG;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_14, container, false);

        issuesNoButton = rootView.findViewById(R.id.courseApplyIssues_No_Button);
        issuesYesButton = rootView.findViewById(R.id.courseApplyIssues_Yes_Button);

        selectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_selected);
        unSelectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_unselected);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("issues") != null) {
                    if (documentSnapshot.getString("issues").matches("No")) {
                        issuesNoButton.performClick();
                    }
                    if (documentSnapshot.getString("issues").matches("Yes")) {
                        issuesYesButton.performClick();
                    }
                }
                else if (documentSnapshot.getString("issues") == null) {
                    issuesNoButton.performClick();
                }
            }
        });

        issuesNoButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                issuesNoButton.setBackground(selectedBG);

                userRef.update("issues", "No");

                return false;
            }
        });

        issuesNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                issuesNoButton.setBackground(selectedBG);
            }
        });
        
        issuesYesButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                issuesYesButton.setBackground(selectedBG);

                userRef.update("issues", "Yes");

                return false;
            }
        });

        issuesYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                issuesYesButton.setBackground(selectedBG);
            }
        });

        return rootView;
    }

    private void unSelectAllButtons() {
        issuesNoButton.setBackground(unSelectedBG);
        issuesYesButton.setBackground(unSelectedBG);
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