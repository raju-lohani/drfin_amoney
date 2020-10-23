package com.drfin.drfin_amoney.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.drfin.drfin_amoney.CommonDetailsActivity;
import com.drfin.drfin_amoney.R;
import com.drfin.drfin_amoney.utils.PrefrenceHandler;
import com.drfin.drfin_amoney.utils.SelectFragmentCallbacks;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private TextView logout, profileTab, userMobile;
    private PrefrenceHandler pref;
    private SelectFragmentCallbacks selectFragmentCallbacks;

    public ProfileFragment(SelectFragmentCallbacks selectFragmentCallbacks) {
        this.selectFragmentCallbacks = selectFragmentCallbacks;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        pref = new PrefrenceHandler(Objects.requireNonNull(getContext()));
        init(view);
        logoutText();
        UserMobileText();
        textViewVisible();
        return view;
    }


    private void init(View view) {
        logout = view.findViewById(R.id.logout);
        TextView aboutus = view.findViewById(R.id.about);
        userMobile = view.findViewById(R.id.userMobile);
        profileTab = view.findViewById(R.id.profileTab);
        logout.setOnClickListener(this);
        profileTab.setOnClickListener(this);
        aboutus.setOnClickListener(this);
    }

    private void logoutText() {
        if (pref.getAuth_Token().isEmpty()) {
            logout.setText(getResources().getString(R.string.login));
        } else {
            logout.setText(getResources().getString(R.string.log_out));
        }
    }

    private void UserMobileText() {
        if (pref.getAuth_Token().isEmpty()) {
            userMobile.setText("");
        } else {
            userMobile.setText(pref.getMobileNo());
        }
    }


    private void textViewVisible() {

    }
    @SuppressLint({"MissingPermission", "HardwareIds"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                if (pref.getAuth_Token().isEmpty()) {
                    selectFragmentCallbacks.onActionChange(getContext(), "selectLogin");
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Warning..");
                    alert.setMessage(getResources().getString(R.string.logout_message));
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoginManager.getInstance().logOut();
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                pref.setAuth_Token("");
                                pref.setMobileNo("");
                                FirebaseAuth.getInstance().signOut();
                                selectFragmentCallbacks.onActionChange(getContext(), "logout");
                            } else {
                                pref.setAuth_Token("");
                                pref.setMobileNo("");
                                selectFragmentCallbacks.onActionChange(getContext(), "logout");

                            }
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
                break;

            case R.id.about:
                Intent i = new Intent(getActivity(), CommonDetailsActivity.class);
                i.putExtra("valid", "about");
                startActivity(i);
                break;

            case R.id.profileTab:
                i = new Intent(getActivity(), CommonDetailsActivity.class);
                i.putExtra("valid", "PreviousLoanList");
                startActivity(i);
                break;

        }
    }

}
