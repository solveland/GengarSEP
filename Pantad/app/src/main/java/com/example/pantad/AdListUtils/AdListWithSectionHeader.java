package com.example.pantad.AdListUtils;

import com.example.pantad.Annons;

import java.util.List;

public class AdListWithSectionHeader {

    private List<Annons> adList;
    private String headerText;

    public AdListWithSectionHeader(List<Annons> adList, String headerText) {
        this.adList = adList;
        this.headerText = headerText;
    }

    public List<Annons> getAdList() {
        return adList;
    }

    public String getHeaderText() {
        return headerText;
    }
}
