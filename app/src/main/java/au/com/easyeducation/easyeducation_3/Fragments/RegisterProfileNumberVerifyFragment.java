package au.com.easyeducation.easyeducation_3.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Duration;

import java.security.Key;
import java.util.concurrent.TimeUnit;

import au.com.easyeducation.easyeducation_3.Activities.MainActivity;
import au.com.easyeducation.easyeducation_3.Activities.RegisterProfileDetailsActivity;
import au.com.easyeducation.easyeducation_3.Model.Institution;
import au.com.easyeducation.easyeducation_3.R;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class RegisterProfileNumberVerifyFragment extends Fragment {

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    private FirebaseAuth mAuth;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private String phoneNumber;
    private Button mVerifyButton;
    private TextView mResendVerificationTV;
    private TextView numberDisplayedTV;

    private EditText verificationCode1;
    private EditText verificationCode2;
    private EditText verificationCode3;
    private EditText verificationCode4;
    private EditText verificationCode5;
    private EditText verificationCode6;

    private OnFragmentInteractionListener mListener;

    private DocumentReference userRef;


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
        setUserVisibleHint(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.register_profile_number_verify_fragment, container, false);

        mVerifyButton = rootView.findViewById(R.id.registerVerifyButton);
        mResendVerificationTV = rootView.findViewById(R.id.registerResendVerificationTextView);
        numberDisplayedTV = rootView.findViewById(R.id.registerNumberDisplayedTV);

        verificationCode1 = rootView.findViewById(R.id.registerVerifyCode1);
        verificationCode2 = rootView.findViewById(R.id.registerVerifyCode2);
        verificationCode3 = rootView.findViewById(R.id.registerVerifyCode3);
        verificationCode4 = rootView.findViewById(R.id.registerVerifyCode4);
        verificationCode5 = rootView.findViewById(R.id.registerVerifyCode5);
        verificationCode6 = rootView.findViewById(R.id.registerVerifyCode6);

        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        grabDatabaseData();

        initializeEditTexts();

        initializeButtons();

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        //INSERT CUSTOM CODE HERE
//        Toast.makeText(getContext(), phoneNumber, Toast.LENGTH_SHORT).show();

        grabDatabaseData();
//        sendVerificationCode(phoneNumber);
        initializeButtons();
        mVerifyButton.performClick();
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Intent intent = new Intent(getContext(), MainActivity.class);
//                          Closes all activities
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
                            ((RegisterProfileDetailsActivity) getActivity()).setCurrentItem(5, true);
                        } else
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    //    private void sendVerificationCode(String number) {
    public void sendVerificationCode(String number) {
        if (number != null && number.length() > 4) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    number,
                    30,
                    TimeUnit.SECONDS,
                    TaskExecutors.MAIN_THREAD,
                    mCallBack
            );
        }
        else
            Toast.makeText(getContext(), "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                // Add code to set text of edit texts to match automatic code detection
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

    private void initializeEditTexts() {
        verificationCode1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (verificationCode1.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    verificationCode2.requestFocus();
                }
                return false;
            }
        });

        verificationCode2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (verificationCode2.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    verificationCode3.requestFocus();

                }
                if (verificationCode2.getText().length() == 0 && KeyEvent.KEYCODE_DEL == keyCode) {
                    verificationCode1.requestFocus();

                }
                return false;
            }
        });

        verificationCode3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (verificationCode3.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    verificationCode4.requestFocus();

                }
                if (verificationCode3.getText().length() == 0 && KeyEvent.KEYCODE_DEL == keyCode) {
                    verificationCode2.requestFocus();

                }
                return false;
            }
        });

        verificationCode4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (verificationCode4.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    verificationCode5.requestFocus();

                }
                if (verificationCode4.getText().length() == 0 && KeyEvent.KEYCODE_DEL == keyCode) {
                    verificationCode3.requestFocus();

                }
                return false;
            }
        });

        verificationCode5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (verificationCode5.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    verificationCode6.requestFocus();

                }
                if (verificationCode5.getText().length() == 0 && KeyEvent.KEYCODE_DEL == keyCode) {
                    verificationCode4.requestFocus();

                }
                return false;
            }
        });

        verificationCode6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (verificationCode6.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
//                    mVerifyButton.performClick();
//                    mVerifyButton.setPressed(true);
//                    verificationCode6.clearFocus();
//                    verificationCode6.setShowSoftInputOnFocus(false);
                    hideKeyboardFrom(getContext(), v);
                }
                if (verificationCode6.getText().length() == 0 && KeyEvent.KEYCODE_DEL == keyCode) {
                    verificationCode5.requestFocus();

                }
                return false;
            }
        });
    }

    private void initializeButtons() {
        mVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = verificationCode1.getText().toString().trim() + verificationCode2.getText().toString().trim() + verificationCode3.getText().toString().trim() + verificationCode4.getText().toString().trim() + verificationCode5.getText().toString().trim() + verificationCode6.getText().toString().trim();

                if (code.isEmpty() || code.length() < 5) {
                    verificationCode6.setError("Invalid verification code");
                    verificationCode1.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });

        mResendVerificationTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Verification code resent.", Toast.LENGTH_SHORT).show();
                sendVerificationCode(phoneNumber);
            }
        });
    }

    private void grabDatabaseData() {
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                phoneNumber = documentSnapshot.getString("number");
                numberDisplayedTV.setText("Input the 6 digit verification code sent to your mobile number: " + phoneNumber);
            }
        });
    }

    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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