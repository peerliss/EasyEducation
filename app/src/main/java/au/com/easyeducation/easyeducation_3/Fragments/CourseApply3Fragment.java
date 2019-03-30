package au.com.easyeducation.easyeducation_3.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import au.com.easyeducation.easyeducation_3.Activities.CourseApplicationNewActivity;
import au.com.easyeducation.easyeducation_3.R;

public class CourseApply3Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button nextButton;

    public CourseApply3Fragment() {
        // Required empty public constructor
    }

    public static CourseApply3Fragment newInstance() {
        CourseApply3Fragment fragment = new CourseApply3Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText mUSI_1;
    private EditText mUSI_2;
    private EditText mUSI_3;
    private EditText mUSI_4;
    private EditText mUSI_5;
    private EditText mUSI_6;
    private EditText mUSI_7;
    private EditText mUSI_8;
    private EditText mUSI_9;
    private EditText mUSI_10;

    private String usiCode;

    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_course_apply_3, container, false);

        mUSI_1 = rootView.findViewById(R.id.courseApplyUSI_ET1);
        mUSI_2 = rootView.findViewById(R.id.courseApplyUSI_ET2);
        mUSI_3 = rootView.findViewById(R.id.courseApplyUSI_ET3);
        mUSI_4 = rootView.findViewById(R.id.courseApplyUSI_ET4);
        mUSI_5 = rootView.findViewById(R.id.courseApplyUSI_ET5);
        mUSI_6 = rootView.findViewById(R.id.courseApplyUSI_ET6);
        mUSI_7 = rootView.findViewById(R.id.courseApplyUSI_ET7);
        mUSI_8 = rootView.findViewById(R.id.courseApplyUSI_ET8);
        mUSI_9 = rootView.findViewById(R.id.courseApplyUSI_ET9);
        mUSI_10 = rootView.findViewById(R.id.courseApplyUSI_ET10);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userRef = db.collection("users").document(mAuth.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("usi") != null) {
                    String usi = documentSnapshot.getString("usi");
                    mUSI_1.setText(String.valueOf(usi.charAt(0)));
                    mUSI_2.setText(String.valueOf(usi.charAt(1)));
                    mUSI_3.setText(String.valueOf(usi.charAt(2)));
                    mUSI_4.setText(String.valueOf(usi.charAt(3)));
                    mUSI_5.setText(String.valueOf(usi.charAt(4)));
                    mUSI_6.setText(String.valueOf(usi.charAt(5)));
                    mUSI_7.setText(String.valueOf(usi.charAt(6)));
                    mUSI_8.setText(String.valueOf(usi.charAt(7)));
                    mUSI_9.setText(String.valueOf(usi.charAt(8)));
                    mUSI_10.setText(String.valueOf(usi.charAt(9)));
                    usiCode = usi;
                }
            }
        });

        initializeEditTexts();

        nextButton = getActivity().findViewById(R.id.courseApplicationNextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFields()) {
                    return;
                }
                ((CourseApplicationNewActivity) getActivity()).addFragment(4);
            }
        });

        return rootView;
    }

    private boolean validateFields() {
        boolean valid = true;
        if (usiCode == null) {
            mUSI_10.setError("Required");
            valid = false;
        } else {
            mUSI_10.setError(null);
        }

        return valid;
    }

    private void initializeEditTexts() {
        mUSI_1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mUSI_1.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    mUSI_2.requestFocus();
                }
                return false;
            }
        });

        mUSI_2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mUSI_2.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    mUSI_3.requestFocus();
                }
                if (mUSI_2.getText().length() == 0 && KeyEvent.KEYCODE_DEL == keyCode) {
                    mUSI_1.requestFocus();
                }
                return false;
            }
        });

        mUSI_3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mUSI_3.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    mUSI_4.requestFocus();

                }
                if (mUSI_3.getText().length() == 0 && KeyEvent.KEYCODE_DEL == keyCode) {
                    mUSI_2.requestFocus();

                }
                return false;
            }
        });

        mUSI_4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mUSI_4.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    mUSI_5.requestFocus();

                }
                if (mUSI_4.getText().length() == 0 && KeyEvent.KEYCODE_DEL == keyCode) {
                    mUSI_3.requestFocus();

                }
                return false;
            }
        });

        mUSI_5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mUSI_5.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    mUSI_6.requestFocus();

                }
                if (mUSI_5.getText().length() == 0 && KeyEvent.KEYCODE_DEL == keyCode) {
                    mUSI_4.requestFocus();

                }
                return false;
            }
        });

        mUSI_6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mUSI_6.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    mUSI_7.requestFocus();

                }
                if (mUSI_6.getText().length() == 0 && KeyEvent.KEYCODE_DEL == keyCode) {
                    mUSI_5.requestFocus();

                }
                return false;
            }
        });

        mUSI_7.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mUSI_7.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    mUSI_8.requestFocus();

                }
                if (mUSI_7.getText().length() == 0 && KeyEvent.KEYCODE_DEL == keyCode) {
                    mUSI_6.requestFocus();

                }
                return false;
            }
        });

        mUSI_8.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mUSI_8.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    mUSI_9.requestFocus();

                }
                if (mUSI_8.getText().length() == 0 && KeyEvent.KEYCODE_DEL == keyCode) {
                    mUSI_7.requestFocus();

                }
                return false;
            }
        });

        mUSI_9.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mUSI_9.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    mUSI_10.requestFocus();

                }
                if (mUSI_9.getText().length() == 0 && KeyEvent.KEYCODE_DEL == keyCode) {
                    mUSI_8.requestFocus();

                }
                return false;
            }
        });

        mUSI_10.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mUSI_10.getText().length() == 1 && KeyEvent.KEYCODE_DEL != keyCode) {
                    hideKeyboardFrom(getContext(), v);

                    usiCode = mUSI_1.getText().toString().trim();
                    usiCode = usiCode + mUSI_2.getText().toString().trim();
                    usiCode = usiCode + mUSI_3.getText().toString().trim();
                    usiCode = usiCode + mUSI_4.getText().toString().trim();
                    usiCode = usiCode + mUSI_5.getText().toString().trim();
                    usiCode = usiCode + mUSI_6.getText().toString().trim();
                    usiCode = usiCode + mUSI_7.getText().toString().trim();
                    usiCode = usiCode + mUSI_8.getText().toString().trim();
                    usiCode = usiCode + mUSI_9.getText().toString().trim();
                    usiCode = usiCode + mUSI_10.getText().toString().trim();

                    if (usiCode.length() == 10) {
                        userRef.update("usi", usiCode);
                    }
                    else {
                        mUSI_10.setError("Required");
                    }
                }
                if (mUSI_10.getText().length() == 0 && KeyEvent.KEYCODE_DEL == keyCode) {
                    mUSI_9.requestFocus();
                }
                return false;
            }
        });
    }


    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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