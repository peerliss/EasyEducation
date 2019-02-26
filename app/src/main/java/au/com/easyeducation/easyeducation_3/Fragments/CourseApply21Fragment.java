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

public class CourseApply21Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply21Fragment() {
        // Required empty public constructor
    }

    public static CourseApply21Fragment newInstance() {
        CourseApply21Fragment fragment = new CourseApply21Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    private Button mRPLNo;
    private Button mRPLYes;

    private Drawable selectedBG;
    private Drawable unSelectedBG;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_21, container, false);

        mRPLNo = rootView.findViewById(R.id.courseApplyRPL_No_Button);
        mRPLYes = rootView.findViewById(R.id.courseApplyRPL_Yes_Button);

        selectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_selected);
        unSelectedBG = getActivity().getDrawable(R.drawable.profile_buttons_border_unselected);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("applyinForRPL") != null) {
                    if (documentSnapshot.getString("applyinForRPL").matches("No")) {
                        mRPLNo.performClick();
                    }
                    if (documentSnapshot.getString("applyinForRPL").matches("Yes")) {
                        mRPLYes.performClick();
                    }
                }
                else if (documentSnapshot.getString("applyinForRPL") == null) {
                    mRPLYes.performClick();
                }
            }
        });

        mRPLNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                mRPLNo.setBackground(selectedBG);

                userRef.update("applyinForRPL", "No");

                return false;
            }
        });

        mRPLNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                mRPLNo.setBackground(selectedBG);
            }
        });

        mRPLYes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unSelectAllButtons();
                mRPLYes.setBackground(selectedBG);

                userRef.update("applyinForRPL", "Yes");

                return false;
            }
        });

        mRPLYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAllButtons();
                mRPLYes.setBackground(selectedBG);
            }
        });


        return rootView;
    }

    private void unSelectAllButtons() {
        mRPLNo.setBackground(unSelectedBG);
        mRPLYes.setBackground(unSelectedBG);
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