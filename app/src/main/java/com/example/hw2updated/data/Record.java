package com.example.hw2updated.data;

import com.google.android.gms.maps.model.LatLng;

public class Record implements Comparable<Record> {
    private String name = "";
    private String time = "";
    private int score = 0;
    private LatLng latLng;

    public Record(String name,LatLng latLng)  {
        this.name = name;
        this.latLng = latLng;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public Record setLatLng(LatLng latLng) {
        this.latLng = latLng;
        return this;
    }

    public String getName() {
        return name;
    }

    public Record setName(String name) {
        this.name = name;
        return this;
    }

    public String getTime() {
        return time;
    }

    public Record setTime(String time) {
        this.time = time;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Record setScore(int score) {
        this.score = score;
        return this;
    }


    @Override
    public int compareTo(Record record) {
        return Integer.compare(record.getScore(),this.score);
    }
}
