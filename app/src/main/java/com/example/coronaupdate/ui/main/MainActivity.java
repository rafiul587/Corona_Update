package com.example.coronaupdate.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.coronaupdate.R;
import com.example.coronaupdate.databinding.ActivityMainBinding;
import com.example.coronaupdate.databinding.BdInfoBinding;
import com.example.coronaupdate.databinding.WorldInfoBinding;
import com.example.coronaupdate.ui.allcountries.AllCountryActivity;
import com.example.coronaupdate.utils.Status;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private WorldInfoBinding bindingWorld;
    private BdInfoBinding bindingBd;
    private MainViewModel viewModel;
    private Animation animationUp;
    private Animation animationDown;
    private int counter = 0;
    Toast toast;
    private long lastBackPressTime = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        bindingWorld = binding.worldInfo;
        bindingBd = binding.bdInfo;
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbar);
        animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MainViewModel.class);
        viewModel.getWorldData().observe(this, worldModelResource -> {
            switch (worldModelResource.status){
                case SUCCESS:
                case ERROR:
                    if(worldModelResource.status == Status.ERROR && !viewModel.isErrorShowed()){
                        Toast.makeText(this, "Could not connect to internet", Toast.LENGTH_SHORT).show();
                        viewModel.setErrorShowed(true);
                    }
                    bindingWorld.progress.setVisibility(View.INVISIBLE);
                    bindingWorld.refresh.setClickable(true);
                    if(worldModelResource.data != null){
                        bindingWorld.update.setText(getDate(worldModelResource.data.getUpdated()));
                        bindingWorld.caseStat.setText(f(worldModelResource.data.getCases()));
                        bindingWorld.deathStat.setText(f(worldModelResource.data.getDeaths()));
                        bindingWorld.recoveredStat.setText(f(worldModelResource.data.getRecovered()));
                        bindingWorld.criticalStat.setText(f(worldModelResource.data.getCritical()));
                        bindingWorld.newCaseStat.setText(f(worldModelResource.data.getTodayCases()));
                        bindingWorld.newDeathStat.setText(f(worldModelResource.data.getTodayDeaths()));
                        bindingWorld.activeStat.setText(f(worldModelResource.data.getActive()));
                        bindingWorld.affectedCountryStat.setText(f(worldModelResource.data.getAffectedCountries()));
                    }
                    break;
                case LOADING:
                    bindingWorld.progress.setVisibility(View.VISIBLE);
                    bindingWorld.refresh.setClickable(false);
                    viewModel.setErrorShowed(false);
            }
        });
        bindingWorld.refresh.setOnClickListener(v -> {
            viewModel.refreshWorld();
        });
        viewModel.getBdData().observe(this, bdModelResource -> {
            switch (bdModelResource.status){
                case SUCCESS:
                case ERROR:
                    if(bdModelResource.status == Status.ERROR && !viewModel.isErrorShowed()){
                        Toast.makeText(this, "Could not connect to internet", Toast.LENGTH_SHORT).show();
                        viewModel.setErrorShowed(true);
                    }
                    bindingBd.progress.setVisibility(View.INVISIBLE);
                    bindingBd.refresh.setClickable(true);
                    if(bdModelResource.data != null){
                        bindingBd.update.setText(getDate(bdModelResource.data.getUpdated()));
                        bindingBd.newCaseStat.setText(g(bdModelResource.data.getTodayCases()));
                        bindingBd.newDeathStat.setText(g(bdModelResource.data.getTodayDeaths()));
                        bindingBd.totalCasesStat.setText(g(bdModelResource.data.getCases()));
                        bindingBd.totalDeathStat.setText(g(bdModelResource.data.getDeaths()));
                        bindingBd.totalRecoverStat.setText(g(bdModelResource.data.getRecovered()));
                        bindingBd.totalCriticalStat.setText(g(bdModelResource.data.getCritical()));
                        bindingBd.activeCasesStat.setText(g(bdModelResource.data.getActive()));
                        bindingBd.totalTestStat.setText(g(bdModelResource.data.getTests()));
                    }
                    break;
                case LOADING:
                    bindingBd.progress.setVisibility(View.VISIBLE);
                    bindingBd.refresh.setClickable(false);
                    viewModel.setErrorShowed(false);
            }
        });
        bindingBd.refresh.setOnClickListener(v -> {
            viewModel.refreshBd();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void expandOrCollaps(View view) {
        if (bindingWorld.activeCases.getVisibility() == View.GONE) {
            bindingWorld.newCase.setVisibility(View.VISIBLE);
            bindingWorld.newDeath.setVisibility(View.VISIBLE);
            bindingWorld.activeCases.setVisibility(View.VISIBLE);
            bindingWorld.affectedCountry.setVisibility(View.VISIBLE);
            bindingWorld.newCase.startAnimation(animationDown);
            bindingWorld.newDeath.startAnimation(animationDown);
            bindingWorld.activeCases.startAnimation(animationDown);
            bindingWorld.affectedCountry.startAnimation(animationDown);
            bindingWorld.seeMore.setImageResource(R.drawable.collapse);
        }
        else {
            TransitionManager.beginDelayedTransition(binding.constraint, new AutoTransition());
            bindingWorld.newCase.setVisibility(View.GONE);
            bindingWorld.newDeath.setVisibility(View.GONE);
            bindingWorld.activeCases.setVisibility(View.GONE);
            bindingWorld.affectedCountry.setVisibility(View.GONE);
            bindingWorld.newCase.startAnimation(animationUp);
            bindingWorld.newDeath.startAnimation(animationUp);
            bindingWorld.activeCases.startAnimation(animationUp);
            bindingWorld.affectedCountry.startAnimation(animationUp);
            bindingWorld.seeMore.setImageResource(R.drawable.expand);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void bdExpandOrCollaps(View view) {
        if (bindingBd.totalRecover.getVisibility() == View.GONE) {
            bindingBd.totalRecover.setVisibility(View.VISIBLE);
            bindingBd.bdCritical.setVisibility(View.VISIBLE);
            bindingBd.activeCases.setVisibility(View.VISIBLE);
            bindingBd.totalTest.setVisibility(View.VISIBLE);
            bindingBd.totalRecover.startAnimation(animationDown);
            bindingBd.bdCritical.startAnimation(animationDown);
            bindingBd.activeCases.startAnimation(animationDown);
            bindingBd.totalTest.startAnimation(animationDown);
            bindingBd.bdMore.setImageResource(R.drawable.collapse);
            sendScroll();
        }
        else {
            TransitionManager.beginDelayedTransition(binding.constraint, new AutoTransition());
            bindingBd.totalRecover.setVisibility(View.GONE);
            bindingBd.bdCritical.setVisibility(View.GONE);
            bindingBd.activeCases.setVisibility(View.GONE);
            bindingBd.totalTest.setVisibility(View.GONE);
            bindingBd.totalRecover.startAnimation(animationUp);
            bindingBd.bdCritical.startAnimation(animationUp);
            bindingBd.activeCases.startAnimation(animationUp);
            bindingBd.totalTest.startAnimation(animationUp);
            bindingBd.bdMore.setImageResource(R.drawable.expand);
        }
    }
    public void sendScroll() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            binding.scroll.smoothScrollBy(0, 20);
            if (counter == 50){
                counter = 0;
            }else {
                counter++;
                sendScroll();
            }
        },16);
    }

    public void godetails(View view) {
        startActivity(new Intent(this, AllCountryActivity.class));
    }
    private String f ( double num) {
        if (num % 1 == 0) {
            return String.format(Locale.getDefault(), "%,d", ((long) num));
        } else {
            DecimalFormat myFormatter = new DecimalFormat("###,###.##");
            return myFormatter.format(num);
        }
    }
    private String f (long num){
        return String.format(Locale.getDefault(),"%,d", num);
    }
    private String g (int num){
        DecimalFormat myFormatter = new DecimalFormat("##,##,###");
        return myFormatter.format(num);
    }
    private String getDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return formatter.format(time);
    }
    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Press back again to close this app", Toast.LENGTH_LONG);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.seeAll) {
            startActivity(new Intent(this, AllCountryActivity.class));
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
}
