package com.example.coronaupdate.ui.allcountries;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.coronaupdate.model.TodayModel;
import com.example.coronaupdate.model.YesterdayModel;
import com.example.coronaupdate.utils.Resource;
import com.example.coronaupdate.repository.AllCountryRepository;

import java.util.List;

public class AllCountryViewModel extends AndroidViewModel {
    private AllCountryRepository repository;
    private LiveData<Resource<List<TodayModel>>> dataToday;
    private LiveData<Resource<List<YesterdayModel>>> dataYesterday;
    private MutableLiveData<Integer> sort = new MutableLiveData<>();
    private MutableLiveData<String> query = new MutableLiveData<>();
    private boolean searchViewExpanded = false;
    private MutableLiveData<Boolean> refreshingT = new MutableLiveData<>();
    private MutableLiveData<Boolean> refreshingY = new MutableLiveData<>();
    private boolean errorShowedT = false;
    private boolean errorShowedY = false;

    public AllCountryViewModel(@NonNull Application application) {
        super(application);
        repository = new AllCountryRepository(application);
    }

    public void loadDataT() {
        dataToday = Transformations.switchMap(refreshingT, id -> repository.getDataToday());
        refreshingT.setValue(true);
    }

    public void loadDataY() {
        dataYesterday = Transformations.switchMap(refreshingY, id -> repository.getDataYesterday());
        refreshingY.setValue(true);
    }

    public LiveData<Resource<List<TodayModel>>> getDataToday() {
        return dataToday;
    }
    public LiveData<Resource<List<YesterdayModel>>> getDataYesterday() {
        return dataYesterday;
    }

    public MutableLiveData<Integer> getSort() {
        return sort;
    }
    public MutableLiveData<String> getQuery() {
        return query;
    }

    public boolean isSearchViewExpanded() {
        return searchViewExpanded;
    }

    public void setSearchViewExpanded(boolean searchViewExpanded) {
        this.searchViewExpanded = searchViewExpanded;
    }

    public void refreshT(){
        refreshingT.setValue(true);
    }

    public void refreshY(){
        refreshingY.setValue(true);
    }

    public boolean isErrorShowedT() {
        return errorShowedT;
    }

    public void setErrorShowedT(boolean errorShowedT) {
        this.errorShowedT = errorShowedT;
    }

    public boolean isErrorShowedY() {
        return errorShowedY;
    }

    public void setErrorShowedY(boolean errorShowedY) {
        this.errorShowedY = errorShowedY;
    }
}