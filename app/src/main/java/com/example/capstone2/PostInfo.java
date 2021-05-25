package com.example.capstone2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class PostInfo implements Serializable,Comparable{
    private String title;
    private String contents;
    private String nickName;
    private Date createdAt;
    private String documentValue;
    private String uID;
    private ArrayList<CommentInfo> comment = new ArrayList<>(

    );
    @Override
    public int compareTo(Object o) {
        PostInfo post = (PostInfo)o;
        int compare = this.createdAt.compareTo(post.createdAt);
        if (compare>0){
            return 1;
        }
        else if(compare<1)
            return -1;
        else
            return 0;
    }

    public PostInfo(String title, String contents, String nickName,String documentValue, Date createdAt){
        this.title=title;
        this.contents=contents;
        this.nickName=nickName;
        this.createdAt=createdAt;
        this.documentValue=documentValue;
    }

    public PostInfo(){

    }


    public String getTitle(){return this.title;}
    public void setTitle(String title){this.title=title;}
    public String getContents(){return this.contents;}
    public void setContents(String contents){this.contents=contents;}
    public String getNickName(){return this.nickName;}
    public void setNickName(String nickName){this.nickName=nickName;}
    public Date getCreatedAt(){return this.createdAt;}
    public void setCreatedAt(String createdAt){this.nickName=createdAt;}
    public String getDocumentValue() {
        return documentValue;
    }
    public void setDocumentValue(String documentValue) {
        this.documentValue = documentValue;
    }
    public String getuID() {
        return uID;
    }
    public void setuID(String uID) {
        this.uID = uID;
    }

    public ArrayList<CommentInfo> getComment() {
        return comment;
    }
    public void setComment(ArrayList<CommentInfo> comment) {
        this.comment = comment;
    }
}

