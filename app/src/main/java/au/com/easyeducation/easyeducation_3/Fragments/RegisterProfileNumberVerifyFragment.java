package au.com.easyeducation.easyeducation_3.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Duration;

import java.util.concurrent.TimeUnit;

import au.com.easyeducation.easyeducation_3.Activities.MainActivity;
import au.com.easyeducation.easyeducation_3.Activities.RegisterProfileDetailsActivity;
import au.com.easyeducation.easyeducation_3.Model.Institution;
import au.com.easyeducation.easyeducation_3.R;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class RegisterProfileNumberVerifyFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RegisterProfileNumberVerifyFragment() {
        // Required empty public constructor
    }

    public static RegisterProfileNumberVerifyFragment newInstance() {
        RegisterProfileNumberVerifyFragment fragment = new RegisterProfileNumberVerifyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText verificationCode1;

    String number;
    String verificationCode;

    private FirebaseAuth mAuth;
    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.register_profile_number_verify_fragment, container, false);

        Button mVerifyButton = rootView.findViewById(R.id.registerVerifyButton);
        TextView mResendVerification = rootView.findViewById(R.id.registerResendVerificationTextView);
        verificationCode1 = rootView.findViewById(R.id.registerVerifyCode1);

        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                number = documentSnapshot.getString("number");
            }
        });

        sendVerificationCode();

        mVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationCode = verificationCode1.getText().toString().trim();

                if (verificationCode.isEmpty() || verificationCode.length() < 6) {
                    verificationCode1.setError("Invalid Verification Code");
                    verificationCode1.requestFocus();
                }

                verifyCode(verificationCode);
            }
        });

        return rootView;
    }

    private void sendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
//                "+61452227384",
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code);
//        mCallBack.onVerificationCompleted(credential);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getContext(), MainActivity.class);

//                                Deletes all the previous open activities so the user is not sent
//                                to the login screen, closes app
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);
                        }
                        else
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCode = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // TODO: Renumber method, update argument and hook method into UI event
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
        // TODO: Update argument type and number
        void onFragmentInteraction(Uri uri);
    }
}