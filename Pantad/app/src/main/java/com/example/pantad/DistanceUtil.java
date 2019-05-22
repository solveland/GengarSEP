package com.example.pantad;

import android.location.Location;

public final class DistanceUtil {
    private DistanceUtil() {
    }

    public static String getDistance(double lat1, double lat2, double lon1,
                                     double lon2, double el1, double el2) {

        System.out.println(lat1);
        System.out.println(lat2);
        System.out.println(lon1);
        System.out.println(lon2);
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        distance = Math.sqrt(distance);

        String result = "";
        if(distance < 1000){
            result += distance;
            int last = 0;
            for(int i = 0; i < result.length(); i++){
                if(result.charAt(i) == '.'){
                    last = i;
                }
            }
            result = result.substring(0, last);
            result += " meter";
        }else if(distance < 10000 && distance >= 1000){
            double km = distance / 1000;
            result += km;
            int last = 0;
            for(int i = 0; i < result.length(); i++){
                if(result.charAt(i) == '.'){
                    if(result.charAt(i + 1) == '0')
                        last = i;
                    else
                        last = i + 2;
                }
            }
            result = result.substring(0, last) + " kilometer";
        }else if(distance >= 10000){
            double mile = distance / 10000;
            result += mile;
            int last = 0;
            for(int i = 0; i < result.length(); i++){
                if(result.charAt(i) == '.'){
                    if(result.charAt(i + 1) == '0')
                        last = i;
                    else
                        last = i + 2;
                }
            }
            result = result.substring(0, last) + " mil";
        }
        return result;
    }
}
