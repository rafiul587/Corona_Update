package com.example.coronaupdate.ui.allcountries;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coronaupdate.R;
import com.example.coronaupdate.databinding.FragmentYesterdayBinding;
import com.example.coronaupdate.model.YesterdayModel;
import com.example.coronaupdate.ui.details.DetailsYesterday;
import com.example.coronaupdate.utils.Status;

import java.util.Collections;


public class YesterdayFragment extends Fragment implements YesterdayAdapter.ItemClickListener{
    private FragmentYesterdayBinding binding;
    private AllCountryViewModel viewModel;
    private YesterdayAdapter recyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean UpOrDown = true;
    private boolean sorted = false;
    private boolean searched = false;
    private boolean refreshEnabled = false;

    public YesterdayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentYesterdayBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        recyclerAdapter = new YesterdayAdapter(getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setAdapter( recyclerAdapter);
        recyclerAdapter.setClickLlistener(this);
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(AllCountryViewModel.class);
        if(viewModel.getDataYesterday() == null){
            viewModel.loadDataY();
        }
        viewModel.getDataYesterday().observe(getViewLifecycleOwner(), listResource -> {
            switch (listResource.status){
                case SUCCESS:
                case ERROR:
                    if(listResource.status == Status.ERROR && !viewModel.isErrorShowedY()){
                        Toast.makeText(getActivity(), "Could not connect to internet", Toast.LENGTH_SHORT).show();
                        viewModel.setErrorShowedY(true);
                    }
                    binding.refreshLayout.setRefreshing(false);
                    if(!refreshEnabled){
                        binding.refreshLayout.setOnRefreshListener(() -> {
                            viewModel.refreshY();
                        });
                        refreshEnabled = true;
                    }
                    if(listResource.data != null){
                        recyclerAdapter.setInfo(listResource.data);
                        recyclerAdapter.setSearchedInfo(listResource.data);
                        if(recyclerAdapter.getItemCount() > 0){
                            binding.fab.setVisibility(View.VISIBLE);
                        }
                        if(!sorted){
                            viewModel.getSort().observe(getViewLifecycleOwner(), integer -> {
                                switch (integer){
                                    case 0:
                                        todayCases();
                                        break;
                                    case 1:
                                        todayDeath();
                                        break;
                                    case 2:
                                        totalCase();
                                        break;
                                    default:
                                        totalDeath();
                                }
                            });
                            sorted = true;
                        }
                        if(!searched){
                            viewModel.getQuery().observe(getViewLifecycleOwner(), s -> {
                                recyclerAdapter.getFilter().filter(s);
                            });
                        }
                    }
                    break;
                case LOADING:
                    binding.refreshLayout.setRefreshing(true);
                    viewModel.setErrorShowedY(false);
            }
        });
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    UpOrDown = false;
                    binding.fab.setImageResource(R.drawable.ic_up);
                }else if(!recyclerView.canScrollVertically(-1)) {
                    UpOrDown = true;
                    binding.fab.setImageResource(R.drawable.ic_down);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    UpOrDown = true;
                    binding.fab.setImageResource(R.drawable.ic_down);
                }
                else if( dy < 0 ){
                    UpOrDown = false;
                    binding.fab.setImageResource(R.drawable.ic_up);
                }
            }
        });
        binding.fab.setOnClickListener(v -> {
            if(UpOrDown) {
                binding.recyclerView.scrollToPosition(recyclerAdapter.getInfo().size() - 1);
                binding.recyclerView.post(() -> {
                    binding.recyclerView.smoothScrollToPosition(recyclerAdapter.getSearchedInfo().size() - 1);
                });
                binding.fab.setImageResource(R.drawable.ic_up);
                UpOrDown = false;
            }else{
                UpOrDown = true;
                binding.recyclerView.scrollToPosition(0);
                binding.fab.setImageResource(R.drawable.ic_down);
            }
        });
        return view;
    }

    public void todayCases(){
        Collections.sort(recyclerAdapter.getInfo(), (o1, o2) -> o2.getTodayCases().compareTo(o1.getTodayCases()));
        Collections.sort(recyclerAdapter.getSearchedInfo(), (o1, o2) -> o2.getTodayCases().compareTo(o1.getTodayCases()));
        recyclerAdapter.notifyDataSetChanged();
    }

    public void todayDeath(){
        Collections.sort(recyclerAdapter.getInfo(), (o1, o2) -> o2.getTodayDeaths().compareTo(o1.getTodayDeaths()));
        Collections.sort(recyclerAdapter.getSearchedInfo(), (o1, o2) -> o2.getTodayDeaths().compareTo(o1.getTodayDeaths()));
        recyclerAdapter.notifyDataSetChanged();
    }

    public void totalCase(){
        Collections.sort(recyclerAdapter.getInfo(), (o1, o2) -> o2.getCases().compareTo(o1.getCases()));
        Collections.sort(recyclerAdapter.getSearchedInfo(), (o1, o2) -> o2.getCases().compareTo(o1.getCases()));
        recyclerAdapter.notifyDataSetChanged();
    }

    public void totalDeath(){
        Collections.sort(recyclerAdapter.getInfo(), (o1, o2) -> o2.getDeaths().compareTo(o1.getDeaths()));
        Collections.sort(recyclerAdapter.getSearchedInfo(), (o1, o2) -> o2.getDeaths().compareTo(o1.getDeaths()));
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(YesterdayModel info) {
        Intent intent = new Intent(getActivity(), DetailsYesterday.class);
        intent.putExtra("info", info);
        startActivity(intent);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        closeKeyboard();
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}