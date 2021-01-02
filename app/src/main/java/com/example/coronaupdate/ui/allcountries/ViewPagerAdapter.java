package com.example.coronaupdate.ui.allcountries;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.coronaupdate.ui.allcountries.TodayFragment;
import com.example.coronaupdate.ui.allcountries.YesterdayFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super( fragmentActivity );
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.e("Created", "ViewpagerAdded");
        switch (position) {
            case 0:
                return new TodayFragment();
            default:
                return new YesterdayFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
