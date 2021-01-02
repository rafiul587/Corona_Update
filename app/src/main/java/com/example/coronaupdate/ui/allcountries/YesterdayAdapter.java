package com.example.coronaupdate.ui.allcountries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.coronaupdate.databinding.CountryInfoBinding;
import com.example.coronaupdate.model.YesterdayModel;

import java.util.ArrayList;
import java.util.List;

public class YesterdayAdapter extends RecyclerView.Adapter<YesterdayAdapter.MyViewHolder> implements Filterable {

    public interface ItemClickListener {
        void onClick(YesterdayModel info);
    }
    private ItemClickListener listener;

    private Context context;
    private List<YesterdayModel> info;
    private List<YesterdayModel> searchedInfo;
    public YesterdayAdapter(Context context) {
        this.context = context;
        this.info = new ArrayList<>();
        this.searchedInfo = new ArrayList<>();
    }

    public List<YesterdayModel> getInfo() {
        return info;
    }

    public void setInfo(List<YesterdayModel> info){
        this.info = info;
    }

    public List<YesterdayModel> getSearchedInfo() {
        return searchedInfo;
    }

    public void setSearchedInfo(List<YesterdayModel> searchedInfo){
        this.searchedInfo = searchedInfo;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public YesterdayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CountryInfoBinding binding = CountryInfoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull YesterdayAdapter.MyViewHolder holder, int position) {
        holder.name.setText( searchedInfo.get( position ).getCountry());
        holder.newCases.setText( String.valueOf(searchedInfo.get( position ).getTodayCases()));
        holder.newDeath.setText( String.valueOf(searchedInfo.get( position ).getTodayDeaths()));
        holder.totalCases.setText( String.valueOf(searchedInfo.get( position ).getCases()));
        holder.totalDeath.setText( String.valueOf(searchedInfo.get( position ).getDeaths()));
        Glide
                .with(context)
                .load(searchedInfo.get(position).getCountryInfo().getFlag())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.flag);

    }

    @Override
    public int getItemCount() {
        return searchedInfo.size();
    }

    public void setClickLlistener (ItemClickListener itemClickListener){
        this.listener = itemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name, newCases, newDeath, totalCases, totalDeath;
        ImageView flag;

        public MyViewHolder(@NonNull CountryInfoBinding binding) {
            super(binding.getRoot());
            flag = binding.flag;
            name = binding.name;
            newCases = binding.newCaseStat;
            newDeath = binding.newDeathStat;
            totalCases = binding.totalCasesStat;
            totalDeath = binding.totalDeathStat;
            binding.getRoot().setTag( binding.getRoot() );
            binding.getRoot().setOnClickListener( this );
        }

        @Override
        public void onClick(View v) {
            listener.onClick(searchedInfo.get(getAdapterPosition()));
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    searchedInfo = info;
                } else {
                    List<YesterdayModel> filteredList = new ArrayList<>();
                    List<YesterdayModel> allCountryInfo = info;
                    for (YesterdayModel row : allCountryInfo) {

                        if (row.getCountry().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    searchedInfo = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values =  searchedInfo;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                List<YesterdayModel> values = (List<YesterdayModel>) filterResults.values;
                searchedInfo = values;
                notifyDataSetChanged();
            }
        };
    }
}