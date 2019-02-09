package au.com.easyeducation.easyeducation_3.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationActivity;
import au.com.easyeducation.easyeducation_3.Activities.RegisterProfileDetailsActivity;
import au.com.easyeducation.easyeducation_3.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseApply1Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseApply1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseApply1Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CourseApply1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseApply1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseApply1Fragment newInstance() {
        CourseApply1Fragment fragment = new CourseApply1Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private EditText mName;
    private EditText mSurname;
    private EditText mDOB;

    String name;
    String surname;
    String fullname;

    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_1, container, false);

        mName = rootView.findViewById(R.id.courseApplyNameET);
        mSurname = rootView.findViewById(R.id.courseApplySurnameET);
        mDOB = rootView.findViewById(R.id.courseApplyDOB_ET);

        final Button mCourseApplyMaleButton = rootView.findViewById(R.id.courseApplyMaleButton);
        final Button mCourseApplyFemaleButton = rootView.findViewById(R.id.courseApplyFemaleButton);

        CountryCodePicker mCourseApplyCountry = rootView.findViewById(R.id.courseApplyCountry);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("name") != null) {
                    mName.setText(documentSnapshot.getString("name"));
                }
                if (documentSnapshot.getString("surname") != null) {
                    mSurname.setText(documentSnapshot.getString("surname"));
                }
                if (documentSnapshot.getString("dob") != null) {
                    mDOB.setText(documentSnapshot.getString("dob"));
                }
            }
        });

        mCourseApplyFemaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCourseApplyFemaleButton.setBackgroundColor(getResources().getColor(R.color.menu));

            }
        });

        mCourseApplyMaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateName()) {
                    Toast.makeText(getContext(), "Please check fields.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    userRef.update("name", name);
                    userRef.update("surname", surname);
                    userRef.update("fullname", fullname);

                    ((CourseApplicationActivity) getActivity()).setCurrentItem(1, true);
                }
            }
        });

        return rootView;
    }

    private boolean validateName() {
        boolean valid = true;

        name = mName.getText().toString().trim();
        surname = mSurname.getText().toString().trim();
        fullname = name + " " + surname;

        if (TextUtils.isEmpty(name)) {
            mName.setError("Required.");
            valid = false;
        } else {
            mName.setError(null);
        }

        if (TextUtils.isEmpty(surname)) {
            mSurname.setError("Required.");
            valid = false;
        } else {
            mSurname.setError(null);
        }

        return valid;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
