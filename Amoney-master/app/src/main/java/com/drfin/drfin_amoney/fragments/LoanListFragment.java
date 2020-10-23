package com.drfin.drfin_amoney.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.drfin.drfin_amoney.Adapters.LoanListAdapter;
import com.drfin.drfin_amoney.R;
import com.drfin.drfin_amoney.models.LoanListModel;
import com.drfin.drfin_amoney.models.LoanListResponse;
import com.drfin.drfin_amoney.utils.ApiClientService;
import com.drfin.drfin_amoney.utils.PrefrenceHandler;
import com.drfin.drfin_amoney.utils.RetrofitApiClient;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanListFragment extends Fragment {
    private PrefrenceHandler pref;
    private TextView not_found_message;
    private RecyclerView loan_list_recycler;
    private ArrayList<LoanListModel> loanListModels;
    private LinearLayout progress_bar_layout;

    public LoanListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_loan_list, container, false);
        pref = new PrefrenceHandler(Objects.requireNonNull(getContext()));
        init(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchLoanList();
    }

    private void init(View v) {
        loan_list_recycler = v.findViewById(R.id.loan_list);
        not_found_message = v.findViewById(R.id.not_found_message);
        progress_bar_layout = v.findViewById(R.id.progress_bar_layout);
        loanListModels = new ArrayList<>();

    }

    private void fetchLoanList() {
        progress_bar_layout.setVisibility(View.VISIBLE);
        ApiClientService apiClientService = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<LoanListResponse> loanList = apiClientService.loanList(pref.getAuth_Token());
        loanList.enqueue(new Callback<LoanListResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoanListResponse> call, @NonNull Response<LoanListResponse> response) {
                if (response.isSuccessful()) {
                    progress_bar_layout.setVisibility(View.GONE);
                    assert response.body() != null;

                    if (response.body().getResultCode().equals("200")) {
                        loanListModels = response.body().getApplicationList();
                        loan_list_recycler.setVisibility(View.VISIBLE);
                        not_found_message.setVisibility(View.GONE);
                        setRecyclerViewData();
                    }else {
                        loan_list_recycler.setVisibility(View.GONE);
                        not_found_message.setVisibility(View.VISIBLE);
                    }
                } else {
                    progress_bar_layout.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Some Error try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoanListResponse> call, @NonNull Throwable t) {
                progress_bar_layout.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setRecyclerViewData() {
        loan_list_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        loan_list_recycler.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.HORIZONTAL));
        LoanListAdapter loanListAdapter = new LoanListAdapter(loanListModels, getContext());
        loan_list_recycler.setAdapter(loanListAdapter);
    }
}