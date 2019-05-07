package com.example.pantad.AdListUtils;


import com.example.pantad.Ad;

public class SectionedAdListContainer {


    private AdListWithSectionHeader[] lists;

    public SectionedAdListContainer(AdListWithSectionHeader[] lists){
        this.lists = lists;
    }

    // quick solution, probably pretty inefficient
    public boolean isSegment(int pos){
        int count = 0;
        for(AdListWithSectionHeader list: lists){
            if (count == pos){
                return true;
            }
            count+= list.getAdList().size() +1;
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
            count+= list.getAdList().size() +1;
            if (count > pos){
                return null;
            }
        }
        return null;
    }

    public String getHeaderText(int pos){
        int count = 0;
        for(AdListWithSectionHeader list: lists){
            if (count == pos){
                return list.getHeaderText();
            }
            count+= list.getAdList().size() +1;
            if (count > pos){
                return null;
            }
        }
        return null;
    }

    public int size(){
        int count = 0;
        for(AdListWithSectionHeader list: lists){
            count += list.getAdList().size() +1;
        }
        return count;
    }

}
