package au.com.easyeducation.easyeducation_3.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import au.com.easyeducation.easyeducation_3.Activities.RegisterProfileDetailsActivity;
import au.com.easyeducation.easyeducation_3.R;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class RegisterProfileNameFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RegisterProfileNameFragment() {
        // Required empty public constructor
    }

    public static RegisterProfileNameFragment newInstance() {
        RegisterProfileNameFragment fragment = new RegisterProfileNameFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText mName;
    private EditText mSurname;

    String name;
    String surname;
    String fullname;

    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.register_profile_name_fragment, container, false);

        mName = rootView.findViewById(R.id.registerName);
        mSurname = rootView.findViewById(R.id.registerSurname);

        final Button mNameNextButton = rootView.findViewById(R.id.registerNameNextButton);

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
            }
        });

        mNameNextButton.setOnClickListener(new View.OnClickListener() {
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

                    ((RegisterProfileDetailsActivity) getActivity()).setCurrentItem(1, true);
                }
            }
        });

        mSurname.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            mNameNextButton.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
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