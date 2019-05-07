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

    public List<Ad> getAdList() {
        return adList;
    }

    public String getHeaderText() {
        return headerText;
    }
}
