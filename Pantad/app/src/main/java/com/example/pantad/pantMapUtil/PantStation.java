package com.example.pantad.pantMapUtil;

import android.content.Context;


public class PantStation {

    public String title;
    public String address;
    public double lon;
    public double lat;
    private static PantStation[] stations;

    public static PantStation[] getStations(Context context){
        if (stations == null){
            String file = PantParser.readFileAsString("pant.txt",context);
            String[] fileSplit = file.split("\n");
            stations = new PantStation[fileSplit.length];
            for(int i = 0; i < fileSplit.length;i++){
                String[] split = fileSplit[i].split(";");
                stations[i] = new PantStation(split[0],split[1],Double.parseDouble(split[2]),Double.parseDouble(split[3]));
            }
        }
        return stations;
    }

    private PantStation(String title,String address,double lon,double lat){
        this.title = title;
        this.address = address;
        this.lon = lon;
        this.lat = lat;
    }

}
