package com.example.coronaupdate.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.coronaupdate.api.ApiResponse;
import com.example.coronaupdate.model.YesterdayModel;
import com.example.coronaupdate.utils.AppExecutors;
import com.example.coronaupdate.utils.LiveDataCallAdapterFactory;
import com.example.coronaupdate.utils.Resource;
import com.example.coronaupdate.model.TodayModel;
import com.example.coronaupdate.db.AppDatabase;
import com.example.coronaupdate.db.CountryDao;
import com.example.coronaupdate.api.ApiService;
import com.example.coronaupdate.ui.main.MainViewModel;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllCountryRepository {
    private AppDatabase db;
    private CountryDao countryDao;
    private final ApiService service;
    private final AppExecutors appExecutors;

    public AllCountryRepository(Application application) {
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

    public LiveData<Resource<List<TodayModel>>> getDataToday() {
        return new NetworkBoundResource<List<TodayModel>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<TodayModel> item) {
                countryDao.deleteToday();
                countryDao.insertToday(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<TodayModel> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<TodayModel>> loadFromDb() {
                return countryDao.getToday();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<TodayModel>>> createCall() {
                return service.getTodayInfo();
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<YesterdayModel>>> getDataYesterday() {
        return new NetworkBoundResource<List<YesterdayModel>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<YesterdayModel> item) {
                countryDao.deleteYesterday();
                countryDao.insertYesterday(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<YesterdayModel> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<YesterdayModel>> loadFromDb() {
                return countryDao.getYesterday();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<YesterdayModel>>> createCall() {
                return service.getYesterdayInfo();
            }
        }.asLiveData();
    }
}
