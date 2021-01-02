package com.example.coronaupdate.ui.allcountries;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import com.example.coronaupdate.R;
import com.example.coronaupdate.databinding.ActivityAllCountryBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class AllCountryActivity extends AppCompatActivity{
    ActivityAllCountryBinding binding;
    AllCountryViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllCountryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.viewPager.setAdapter(new ViewPagerAdapter(this));
        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(AllCountryViewModel.class);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
        binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Today");
                    break;
                default:
                    tab.setText("Yesterday");
                    break;
            }
        });
        tabLayoutMediator.attach();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu, menu );
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQuery(viewModel.getQuery().getValue(), false);
        if(viewModel.isSearchViewExpanded()){
            searchView.setIconified(false);
            searchView.clearFocus();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                viewModel.getQuery().setValue(query);
                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if(hasFocus)
                viewModel.setSearchViewExpanded(true);
        });
        searchView.setOnCloseListener(() -> {
            viewModel.setSearchViewExpanded(false);
            return false;
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.newcase:
                viewModel.getSort().setValue(0);
                return true;
            case R.id.newdeath:
                viewModel.getSort().setValue(1);
                return true;
            case R.id.totalcase:
                viewModel.getSort().setValue(2);
                return true;
            case R.id.totaldeath:
                viewModel.getSort().setValue(3);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
