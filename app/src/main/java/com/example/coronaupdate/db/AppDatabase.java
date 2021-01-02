package com.example.coronaupdate.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.coronaupdate.model.BdModel;
import com.example.coronaupdate.model.TodayModel;
import com.example.coronaupdate.model.WorldModel;
import com.example.coronaupdate.model.YesterdayModel;

@Database(entities = {TodayModel.class, YesterdayModel.class, WorldModel.class, BdModel.class}, version = 1, exportSchema = false)
@TypeConverters(objectConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;
    private static final String DATABASE_NAME  = "corona_info";
    public abstract CountryDao countryDao();

    public static AppDatabase getsInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return sInstance;
    }
}
