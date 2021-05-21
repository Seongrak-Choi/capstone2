package com.example.capstone2;

import android.widget.LinearLayout;

public class LibraryInfo {
    private String libraryName;
    private String libraryCode;
    private String seatUrl="N";

    public LibraryInfo(String libraryName,String libraryCode,String seatUrl){
        this.libraryName=libraryName;
        this.libraryCode=libraryCode;
        this.seatUrl=seatUrl;
    }

    public String getLibraryName(){return this.libraryName;}
    public String getLibraryCode(){return this.libraryCode;}
    public String getSeatUrl() {
        return seatUrl;
    }
}
