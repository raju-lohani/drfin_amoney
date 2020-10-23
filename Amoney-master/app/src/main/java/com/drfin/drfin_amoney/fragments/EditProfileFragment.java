package com.drfin.drfin_amoney.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.drfin.drfin_amoney.R;
import com.drfin.drfin_amoney.utils.PrefrenceHandler;

public class EditProfileFragment extends Fragment implements View.OnClickListener{
    private TextView phone, email;
    private PrefrenceHandler pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        init(view);
        getProfileDetail();
        return view;
    }

    private void init(View view){
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        Button saveBtn = view.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);
    }

    private void getProfileDetail() {
//        phone.setText(pref.getMobileNo());
//        email.setText(pref.getGemail());
    }


    @Override
    public void onClick(View v) {

    }
}

