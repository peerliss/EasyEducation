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

public class CourseApply22Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply22Fragment() {
        // Required empty public constructor
    }

    public static CourseApply22Fragment newInstance() {
        CourseApply22Fragment fragment = new CourseApply22Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    private Button mNameChangedNo;
    private Button mNameChangedYes;

    private Drawable selectedBG;
    private Drawable unSelectedBG;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_22, container, false);

        mNameChangedNo = rootView.findViewById(R.id.courseApplyNameChanged_No_Button);
        mNameChangedYes = rootView.findViewById(R.id.courseApplyNameChanged_Yes_Button);

        selectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_selected);
        unSelectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_unselected);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("nameChanged") != null) {
                    if (documentSnapshot.getString("nameChanged").matches("No")) {
                        mNameChangedNo.performClick();
                    }
                    if (documentSnapshot.getString("nameChanged").matches("Yes")) {
                        mNameChangedYes.performClick();
                    }
                }
                else if (documentSnapshot.getString("nameChanged") == null) {
                    mNameChangedNo.performClick();
                }
            }
        });

        mNameChangedNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                mNameChangedNo.setBackground(selectedBG);

                userRef.update("nameChanged", "No");

                return false;
            }
        });

        mNameChangedNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                mNameChangedNo.setBackground(selectedBG);
            }
        });

        mNameChangedYes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                mNameChangedYes.setBackground(selectedBG);

                userRef.update("nameChanged", "Yes");

                return false;
            }
        });

        mNameChangedYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                mNameChangedYes.setBackground(selectedBG);
            }
        });


        return rootView;
    }

    private void unSelectAllButtons() {
        mNameChangedNo.setBackground(unSelectedBG);
        mNameChangedYes.setBackground(unSelectedBG);
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