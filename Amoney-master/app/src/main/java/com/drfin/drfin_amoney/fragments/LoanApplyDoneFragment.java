package com.drfin.drfin_amoney.fragments;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.drfin.drfin_amoney.R;

public class LoanApplyDoneFragment extends Fragment {
    String kyc;

    public LoanApplyDoneFragment(String kyc) {
        this.kyc=kyc;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=null;
        if (kyc.equals("kyc_done")) {
            view = inflater.inflate(R.layout.fragment_done_loan_layout, container, false);
            TextView title = view.findViewById(R.id.title);
            title.setVisibility(View.VISIBLE);
            title.setText("Loan Applied \n Successful");
        }else {
            view = inflater.inflate(R.layout.fragment_done_loan_layout, container, false);
            TextView title = view.findViewById(R.id.title);
            TextView message = view.findViewById(R.id.message);
            TextView status = view.findViewById(R.id.status);
            ImageView image = view.findViewById(R.id.image);
            image.setImageDrawable(getResources().getDrawable(R.drawable.no_found));
            image.setBackground(getResources().getDrawable(R.drawable.ic_brightness_1_black_24dp));
            message.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            title.setText("Loan\nRejected");
            status.setText("Reject");
        }
        return view;
    }
}