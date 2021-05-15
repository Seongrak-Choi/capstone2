package com.example.capstone2;

import android.widget.LinearLayout;

public class LibraryInfo {
    private String libraryName;
    private int libraryCode;

    public LibraryInfo(String libraryName,int libraryCode){
        this.libraryName=libraryName;
        this.libraryCode=libraryCode;
    }

    public String getLibraryName(){return this.libraryName;}
    public int getLibraryCode(){return this.libraryCode;}
}
