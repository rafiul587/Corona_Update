package com.example.coronaupdate.db;

import androidx.room.TypeConverter;

import com.example.coronaupdate.model.CountryInfo;
import com.google.gson.Gson;

public class objectConverter {
    @TypeConverter
    public static CountryInfo fromString(String value) {
        return new Gson().fromJson(value, CountryInfo.class);
    }
    @TypeConverter
    public static String fromObject(CountryInfo countryInfo) {
        return new Gson().toJson(countryInfo);
    }
}
