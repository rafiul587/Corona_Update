package com.example.coronaupdate.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.coronaupdate.model.BdModel;
import com.example.coronaupdate.model.WorldModel;
import com.example.coronaupdate.repository.MainRepository;
import com.example.coronaupdate.utils.Resource;

public class MainViewModel extends AndroidViewModel {
    public static final String BASE_URL = "https://corona.lmao.ninja/v2/";
    private MainRepository repository;
    private LiveData<Resource<WorldModel>> worldData;
    private LiveData<Resource<BdModel>> bdData;
    private MutableLiveData<Boolean> refreshingWorld = new MutableLiveData<>();
    private MutableLiveData<Boolean> refreshingBd = new MutableLiveData<>();
    private boolean errorShowed = false;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new MainRepository(application);
        worldData = Transformations.switchMap(refreshingWorld, id -> repository.getWorldData());
        bdData = Transformations.switchMap(refreshingBd, id -> repository.getBdData());
        refreshingWorld.setValue(true);
        refreshingBd.setValue(true);
    }

    public LiveData<Resource<WorldModel>> getWorldData() {
        return worldData;
    }

    public LiveData<Resource<BdModel>> getBdData() {
        return bdData;
    }

    public void refreshWorld(){
        refreshingWorld.setValue(true);
    }

    public void refreshBd(){
        refreshingBd.setValue(true);
    }

    public boolean isErrorShowed() {
        return errorShowed;
    }

    public void setErrorShowed(boolean errorShowed) {
        this.errorShowed = errorShowed;
    }
}