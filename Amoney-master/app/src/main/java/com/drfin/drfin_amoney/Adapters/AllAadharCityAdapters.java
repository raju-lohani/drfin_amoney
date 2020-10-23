package com.drfin.drfin_amoney.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drfin.drfin_amoney.R;
import com.drfin.drfin_amoney.models.AadharCityListModel;
import com.drfin.drfin_amoney.models.CityListModel;
import com.drfin.drfin_amoney.utils.SelectCityStateInterface;

import java.util.List;

public class AllAadharCityAdapters extends RecyclerView.Adapter<AllAadharCityAdapters.ViewHolder> {
    private List<AadharCityListModel> cityListModels;
    private Context context;
    private SelectCityStateInterface selectCityStateInterface;
    private int extraFlag;

    public AllAadharCityAdapters(Context context, List<AadharCityListModel> stateListModels, SelectCityStateInterface selectCityStateInterface, int extraFlag) {
        this.cityListModels = stateListModels;
        this.context = context;
        this.selectCityStateInterface = selectCityStateInterface;
        this.extraFlag = extraFlag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_list_row, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.State_Name.setText(cityListModels.get(position).getCity_name());
        holder.State_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (extraFlag == 1) {
                    selectCityStateInterface.onGetStateOrCity(context, "city", cityListModels.get(position).getCity_name(),"");
                }else {
                    if (extraFlag == 2) {
                        selectCityStateInterface.onGetStateOrCity(context, "aadhar_city", cityListModels.get(position).getCity_name(),"");
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return cityListModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView State_Name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            State_Name = itemView.findViewById(R.id.state_name);
        }
    }
}
