package com.example.coronaupdate.ui.details;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.coronaupdate.R;
import com.example.coronaupdate.databinding.DetailsActivityBinding;
import com.example.coronaupdate.model.YesterdayModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailsYesterday extends AppCompatActivity {
    DetailsActivityBinding binding;
    YesterdayModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        model = (YesterdayModel) getIntent().getSerializableExtra("info");
        binding.name.setText(model.getCountry());
        Glide.with(this)
                .load(model.getCountryInfo().getFlag())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.flag);
        updateRate();
        binding.update.setText(getDate(model.getUpdated()));
        String[] keys = new String[]{"New Cases", "New Deaths", "Total Cases", "Total Deaths", "Total Recovred", "Critical", "Active Cases", "Tests",
                "Tests Per Million", "Cases Per Million", "Deaths Per Million", "Active Per Million", "Recovered Per Million",
                "Critical Per Million", "Population", "Continent"};
        String[] values = new String[16];
        values[0] = f(model.getTodayCases());values[1] = f(model.getTodayDeaths());values[2] = f(model.getCases());values[3] = f(model.getDeaths());values[4] = f(model.getRecovered());
        values[5] = f(model.getCritical());values[6] = f(model.getActive());values[7] = f(model.getTests());values[8] = f(model.getTestsPerOneMillion());values[9] = f(model.getCasesPerOneMillion());
        values[10] = f(model.getDeathsPerOneMillion());values[11] = f(model.getActivePerOneMillion());values[12] = f(model.getRecoveredPerOneMillion());values[13] = f(model.getCriticalPerOneMillion());values[14] = f(model.getPopulation());
        values[15] = String.valueOf(model.getContinent());
        for(int i=0; i<16; i++){
            View view = getLayoutInflater().inflate(R.layout.item_details, null);
            TextView name = view.findViewById(R.id.name);
            TextView stat = view.findViewById(R.id.stat);
            if(i == 3){
                stat.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }else if(i == 4){
                stat.setTextColor(getResources().getColor(android.R.color.holo_green_light));
            }
            name.setText(keys[i]);
            stat.setText(values[i]);
            binding.layout.addView(view);
        }
    }
    private String getDate(Long updated) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return formatter.format(updated);
    }

    private void updateRate() {
        long closed = model.getCases() - model.getActive();
        String recoverRate = String.valueOf(((model.getRecovered()*100)/closed));
        String deathRate = String.valueOf(((model.getDeaths()*100)/closed));
        binding.caseStat.setText(f(model.getCases()));
        binding.recoverStat.setText(recoverRate + " %");
        binding.deathStat.setText(deathRate + " %");
        binding.closedStat.setText(f(closed));
    }
    private String f ( double num) {
        if (num % 1 == 0) {
            return String.format(Locale.getDefault(), "%,d", ((int) num));
        } else {
            DecimalFormat myFormatter = new DecimalFormat("###,###.##");
            return myFormatter.format(num);
        }
    }
    private String f (int num){
        return String.format(Locale.getDefault(),"%,d", num);
    }
    private String g (int num){
        DecimalFormat myFormatter = new DecimalFormat("##,##,###");
        return myFormatter.format(num);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}