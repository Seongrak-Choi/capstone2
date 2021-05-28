package com.example.capstone2;

import java.util.ArrayList;
import java.util.Date;

public class WriteInfo {
    private String title;
    private String contents;
    private String nickName;
    private Date createdAt;
    private String documentValue;
    private String uID;
    private ArrayList<CommentInfo> comment;


    public WriteInfo(String title, String contents,String nickName,Date createdAt,String uID){
        this.title=title;
        this.contents=contents;
        this.nickName=nickName;
        this.createdAt=createdAt;
        this.uID=uID;

    }

    public String getTitle(){return this.title;}
    public void setTitle(String title){this.title=title;}
    public String getContents(){return this.contents;}
    public void setContents(String contents){this.contents=contents;}
    public String getNickName(){return this.nickName;}
    public void setNickName(String nickName){this.nickName=nickName;}
    public Date getCreatedAt(){return this.createdAt;}
    public void setCreatedAt(String createdAt){this.nickName=createdAt;}

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getDocumentValue() {
        return documentValue;
    }

    public void setDocumentValue(String documentValue) {
        this.documentValue = documentValue;
    }
    public ArrayList<CommentInfo> getComment() {
        return comment;
    }
    public void setComment(ArrayList<CommentInfo> comment) {
        this.comment = comment;
    }
}
