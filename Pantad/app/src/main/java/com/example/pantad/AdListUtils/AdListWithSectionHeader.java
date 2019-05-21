package com.example.pantad.AdListUtils;

import com.example.pantad.Ad;

import java.util.List;

public class AdListWithSectionHeader {

    private List<Ad> adList;
    private String headerText;

    public AdListWithSectionHeader(List<Ad> adList, String headerText) {
        this.adList = adList;
        this.headerText = headerText;
    }

    public int size(){
        if(adList.size() > 0){
            return adList.size() +1;
        }
        return 0;
    }

    public List<Ad> getAdList() {
        return adList;
    }

    public String getHeaderText() {
        return headerText;
    }
}
