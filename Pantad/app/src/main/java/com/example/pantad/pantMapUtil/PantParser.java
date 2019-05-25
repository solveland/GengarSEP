package com.example.pantad.pantMapUtil;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.pantad.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class PantParser {

    public static String readFileAsString(String fileName, Context context) {
        AssetManager assetManager = context.getAssets();
        InputStream input;
        String text = "";

        try {
            input = assetManager.open(fileName);

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            // byte buffer into a string
            text = new String(buffer);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Log.v("TAG", "Text File: " + text);
        return text;
    }

    private static List<String> textIntoList(String file, char firstChar, String prefix, char postfix){
        List<String> list = new ArrayList<>();

        int i = 0;
        while(i < file.length()){
            if(file.charAt(i) == firstChar){
                if(file.substring(i, i + prefix.length()).equals(prefix)){
                    String s = "";
                    i = i + prefix.length();
                    while(!(file.charAt(i) == postfix)){
                        s += file.charAt(i);
                        i++;
                    }
                    list.add(s);
                }
            }i++;
        }

        return list;
    }

    public static List <PantTriplet<String, String, String>> textIntoTrip(Context context, String fileName, char firstChar1, char firstChar2, char firstChar3, PantTriplet<String, String, String> prefixes, char postfix ){
        List<PantTriplet<String, String, String>> result = new ArrayList<>();
        String text = readFileAsString(fileName, context);
        List<String> firstList = textIntoList(text, firstChar1, prefixes.getLat(), postfix);
        List<String> secondList = textIntoList(text, firstChar2, prefixes.getLng(), postfix);
        List<String> thirdList = textIntoList(text, firstChar3, prefixes.getAddress(), postfix);
        if(firstList.size() == secondList.size() && firstList.size() == thirdList.size()){
            for(String s : firstList){
                int i = firstList.indexOf(s);
                result.add(new PantTriplet<>(s, secondList.get(i), thirdList.get(i)));
            }
        }
        return result;
    }
}
