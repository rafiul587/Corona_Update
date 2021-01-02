package com.example.coronaupdate.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import com.example.coronaupdate.api.ApiResponse;
import com.example.coronaupdate.api.ApiService;
import com.example.coronaupdate.db.AppDatabase;
import com.example.coronaupdate.db.CountryDao;
import com.example.coronaupdate.model.BdModel;
import com.example.coronaupdate.model.WorldModel;
import com.example.coronaupdate.ui.main.MainViewModel;
import com.example.coronaupdate.utils.AppExecutors;
import com.example.coronaupdate.utils.LiveDataCallAdapterFactory;
import com.example.coronaupdate.utils.Resource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainRepository {
    private AppDatabase db;
    private CountryDao countryDao;
    private final ApiService service;
    private final AppExecutors appExecutors;

    public MainRepository(Application application) {
        db = AppDatabase.getsInstance(application);
        countryDao = db.countryDao();
        appExecutors =  AppExecutors.getInstance();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainViewModel.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();
        service = retrofit.create(ApiService.class);
    }

    public LiveData<Resource<WorldModel>> getWorldData() {
        return new NetworkBoundResource<WorldModel>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull WorldModel item) {
                countryDao.deleteWorld();
                countryDao.insertWorld(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable WorldModel data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<WorldModel> loadFromDb() {
                return countryDao.getWorld();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WorldModel>> createCall() {
                return service.getWorldInfo();
            }
        }.asLiveData();
    }

    public LiveData<Resource<BdModel>> getBdData() {
        return new NetworkBoundResource<BdModel>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull BdModel item) {
                countryDao.deleteBd();
                countryDao.insertBd(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable BdModel data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<BdModel> loadFromDb() {
                return countryDao.getBd();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<BdModel>> createCall() {
                return service.getBdInfo();
            }
        }.asLiveData();
    }
}
