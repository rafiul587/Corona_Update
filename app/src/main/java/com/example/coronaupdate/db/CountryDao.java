package com.example.coronaupdate.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.coronaupdate.model.BdModel;
import com.example.coronaupdate.model.TodayModel;
import com.example.coronaupdate.model.WorldModel;
import com.example.coronaupdate.model.YesterdayModel;

import java.util.List;

@Dao
public interface CountryDao {

    @Query("SELECT * FROM today")
    LiveData<List<TodayModel>> getToday();
    @Insert
    void insertToday(List<TodayModel> todayModels);
    @Query("DELETE FROM today")
    void deleteToday();

    @Query("SELECT * FROM yesterday")
    LiveData<List<YesterdayModel>> getYesterday();
    @Insert
    void insertYesterday(List<YesterdayModel> yesterdayModels);
    @Query("DELETE FROM yesterday")
    void deleteYesterday();

    @Query("SELECT * FROM world")
    LiveData<WorldModel> getWorld();
    @Insert
    void insertWorld(WorldModel worldModel);
    @Query("DELETE FROM world")
    void deleteWorld();

    @Query("SELECT * FROM bd")
    LiveData<BdModel> getBd();
    @Insert
    void insertBd(BdModel bdModel);
    @Query("DELETE FROM bd")
    void deleteBd();
}