package au.com.easyeducation.easyeducation_3.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationActivity;
import au.com.easyeducation.easyeducation_3.Activities.RegisterProfileDetailsActivity;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApply5Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CourseApply5Fragment() {
        // Required empty public constructor
    }

    public static CourseApply5Fragment newInstance() {
        CourseApply5Fragment fragment = new CourseApply5Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    private Button ieltsButton;
    private Button pteButton;

    private boolean isIELTSButtonPressed = false;
    private boolean isPTEButtonPressed = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_5, container, false);

        ieltsButton = rootView.findViewById(R.id.courseApplyIELTS_Button);
        pteButton = rootView.findViewById(R.id.courseApplyPTE_Button);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("englishTest") != null) {
                    if (documentSnapshot.getString("englishTest").matches("IElTS")) {
                        ieltsButton.performClick();
                    }
                    if (documentSnapshot.getString("englishTest").matches("PTE")) {
                        pteButton.performClick();
                    }
                }
            }
        });

        ieltsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isIELTSButtonPressed = true;
                if (isPTEButtonPressed) {
                    pteButton.setBackgroundColor(getResources().getColor(R.color.white));
                    isPTEButtonPressed = false;
                } else {
                    ieltsButton.setBackgroundColor(getResources().getColor(R.color.menu));
                    userRef.update("englishTest", "IELTS");
                }
                return false;
            }
        });

        pteButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isPTEButtonPressed = true;
                if (isIELTSButtonPressed) {
                    ieltsButton.setBackgroundColor(getResources().getColor(R.color.white));
                    isIELTSButtonPressed = false;
                } else {
                    pteButton.setBackgroundColor(getResources().getColor(R.color.menu));
                    userRef.update("englishTest", "PTE");
                }
                return false;
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
