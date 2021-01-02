package com.example.coronaupdate.api;

import androidx.lifecycle.LiveData;

import com.example.coronaupdate.model.BdModel;
import com.example.coronaupdate.model.TodayModel;
import com.example.coronaupdate.model.WorldModel;
import com.example.coronaupdate.model.YesterdayModel;

import java.util.List;
import retrofit2.http.GET;

public interface ApiService {
    @GET("all")
    LiveData<ApiResponse<WorldModel>> getWorldInfo();

    @GET("countries/bd")
    LiveData<ApiResponse<BdModel>> getBdInfo();

    @GET("countries")
    LiveData<ApiResponse<List<TodayModel>>> getTodayInfo();

    @GET("countries?yesterday=true")
    LiveData<ApiResponse<List<YesterdayModel>>> getYesterdayInfo();
}
