package co.edcall.app.Fragments;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import co.edcall.app.Activities.RegisterProfileDetailsNewActivity;
import co.edcall.app.R;

public class RegisterProfileInternationalFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Drawable selectedBackground;
    private Drawable unSelectedBackground;

    public RegisterProfileInternationalFragment() {
        // Required empty public constructor
    }

    public static RegisterProfileInternationalFragment newInstance() {
        RegisterProfileInternationalFragment fragment = new RegisterProfileInternationalFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.register_profile_international_student_fragment, container, false);

        selectedBackground = getResources().getDrawable(R.drawable.profile_buttons_border_selected);
        unSelectedBackground = getResources().getDrawable(R.drawable.profile_buttons_border_unselected);


        final Button isInternational = rootView.findViewById(R.id.registerProfileInternationalYes);
        final Button isNotInternational = rootView.findViewById(R.id.registerProfileInternationalNo);
        Button nextButton = rootView.findViewById(R.id.registerProfileInternationalNext);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("internationalStudent") != null) {
                    if (documentSnapshot.getString("internationalStudent").matches("No")) {
                        isNotInternational.performClick();
                    }
                    if (documentSnapshot.getString("internationalStudent").matches("Yes")) {
                        isInternational.performClick();
                    }
                }
            }
        });

        isInternational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.update("internationalStudent", "Yes");

                isInternational.setBackground(selectedBackground);
                isNotInternational.setBackground(unSelectedBackground);

                isInternational.setTextColor(getResources().getColor(R.color.white));
                isNotInternational.setTextColor(getResources().getColor(android.R.color.black));
            }
        });

        isNotInternational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.update("internationalStudent", "No");

                isInternational.setBackground(unSelectedBackground);
                isNotInternational.setBackground(selectedBackground);

                isInternational.setTextColor(getResources().getColor(android.R.color.black));
                isNotInternational.setTextColor(getResources().getColor(R.color.white));
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterProfileDetailsNewActivity) getActivity()).addFragment();

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