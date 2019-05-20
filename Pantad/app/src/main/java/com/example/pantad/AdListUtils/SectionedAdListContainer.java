package com.example.pantad.AdListUtils;


import com.example.pantad.Ad;

public class SectionedAdListContainer {


    private AdListWithSectionHeader[] lists;

    public SectionedAdListContainer(AdListWithSectionHeader[] lists){
        this.lists = lists;
    }

    // quick solution, probably pretty inefficient
    public boolean isHeader(int pos){
        int count = 0;
        for(AdListWithSectionHeader list: lists){
            if (count == pos){
                return true;
            }
            count+= list.size();
            if (count > pos){
                return false;
            }
        }
        return false;
    }

    public Ad getAd(int pos){
        if(pos == 0){
            return null;
        }
        int count = 1;
        for(AdListWithSectionHeader list: lists){
            if(count + list.getAdList().size() > pos){
                return list.getAdList().get(pos-count);
            }
            count+= list.size();
            if (count > pos){
                return null;
            }
        }
        return null;
    }

    public String getHeaderText(int pos){
        int count = 0;
        for(AdListWithSectionHeader list: lists){
            if (count == pos && list.size() > 0){
                return list.getHeaderText();
            }
            count+= list.size();
            if (count > pos){
                return null;
            }
        }
        return null;
    }

    public int size(){
        int count = 0;
        for(AdListWithSectionHeader list: lists){
            count += list.size();
        }
        return count;
    }

}
