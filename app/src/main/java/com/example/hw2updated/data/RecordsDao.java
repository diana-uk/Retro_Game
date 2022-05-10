package com.example.hw2updated.data;

import com.example.hw2updated.utils.GameSP;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class RecordsDao {

    private final String RECORDS_KEY = "RECORD_LIST";

    public RecordsDao() {

    }


    public List<Record> readRecords(){
        List<Record> records;
        String json = GameSP.getInstance().getString(RECORDS_KEY,null);
        if (json !=null){
            records = new Gson().fromJson(json, new TypeToken<List<Record>>(){}.getType());
        }else{
            records = new ArrayList<>();
        }

        return records;
    }

    public void saveRecords(List<Record> records){
        String json = new Gson().toJson(records);
        GameSP.getInstance().putString(RECORDS_KEY,json);
    }
}
