package au.com.easyeducation.easyeducation_3.Activities.ui.registerprofiledetails;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import au.com.easyeducation.easyeducation_3.R;

public class RegisterProfileDetailsFragment extends Fragment {

    private RegisterProfileDetailsViewModel mViewModel;

    public static RegisterProfileDetailsFragment newInstance() {
        return new RegisterProfileDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_profile_name_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RegisterProfileDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

}