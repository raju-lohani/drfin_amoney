package com.drfin.drfin_amoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.drfin.drfin_amoney.fragments.AboutFragment;
import com.drfin.drfin_amoney.fragments.EditProfileFragment;
import com.drfin.drfin_amoney.fragments.LoanAmountDetailsFragment;
import com.drfin.drfin_amoney.fragments.LoanListFragment;

import java.util.Objects;

public class CommonDetailsActivity extends AppCompatActivity {
    String application_no = null;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String loanStatus = getIntent().getStringExtra("loanStatus");
        if (getIntent().hasExtra("application_no")) {
            application_no = intent.getStringExtra("application_no");
            if (intent.hasExtra("bundle")) {
                bundle = Objects.requireNonNull(intent.getExtras()).getBundle("bundle");
            }
        }

        if (intent.hasExtra("valid")) {
            switch (Objects.requireNonNull(intent.getStringExtra("valid"))) {
                case "about":
                    getSupportActionBar().setTitle("About Us");
                    TransformFragment(new AboutFragment());
                    break;

                case "LoanAmountDetails":
                    getSupportActionBar().setTitle("Amount Details");
                    TransformFragment(new LoanAmountDetailsFragment(application_no, bundle));
                    break;

                case "PreviousLoanList":
                    getSupportActionBar().setTitle("Loan Records");
                    TransformFragment(new LoanListFragment());
                    break;

                case "ProfileDetail":
                    getSupportActionBar().setTitle("Profile Detail");
                    TransformFragment(new EditProfileFragment());
                    break;

                default:
                    finish();
                    break;
            }
        }

    }

    private void TransformFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.Container_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}