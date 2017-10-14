package com.cruz.lantaw.Paginator;

import com.cruz.lantaw.models.Movie;

import java.util.ArrayList;

/**
 * Created by AsusPc on 9/26/2017.
 */

public class Paginator {
    public int TOTAL_NUM_ITEMS;
    public int ITEMS_PER_PAGE;
    public int ITEMS_REMAINING;
    public int LAST_PAGE;

    public String[] generatePage(int currentPage, String poster_image_thumbnails[]){
        TOTAL_NUM_ITEMS = poster_image_thumbnails.length;
        ITEMS_PER_PAGE = 9;
        ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        LAST_PAGE = TOTAL_NUM_ITEMS/ITEMS_PER_PAGE;

        int startItem = currentPage*ITEMS_PER_PAGE;
        int numOfData = ITEMS_PER_PAGE;

        String pageData[] = new String[poster_image_thumbnails.length];

        if (currentPage==LAST_PAGE && ITEMS_REMAINING>0){
            for (int i = startItem; i<startItem+ITEMS_REMAINING; i++){
                pageData[i]=poster_image_thumbnails[i];
            }
        }else{
            for (int i = startItem; i < startItem+numOfData; i++) {
                pageData[i]=poster_image_thumbnails[i];
            }
        }
        return pageData;
    }
}
