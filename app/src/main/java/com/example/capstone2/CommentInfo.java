package com.example.capstone2;

import java.util.Date;

public class CommentInfo implements Comparable {
    private String uID;
    private String documentValue;
    private String contents;
    private String nickName;
    private Date createdAt;
    private String postDocumentValue;
    private String title;

    @Override
    public int compareTo(Object o) {
        CommentInfo comment = (CommentInfo) o;
        int compare = this.createdAt.compareTo(comment.createdAt);
        if (compare>0){
            return 1;
        }
        else if(compare<1)
            return -1;
        else
            return 0;
    }

    public CommentInfo(String title,String contents, String nickName,String uID,String documentValue,String postDocumentValue, Date createdAt){
        this.contents=contents;
        this.nickName=nickName;
        this.createdAt=createdAt;
        this.uID=uID;
        this.documentValue=documentValue;
        this.postDocumentValue=postDocumentValue;
        this.title=title;
    }

    public CommentInfo(String contents, String nickName,String uID, Date createdAt){
        this.contents=contents;
        this.nickName=nickName;
        this.createdAt=createdAt;
        this.uID=uID;
        this.documentValue=documentValue;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPostDocumentValue() {
        return postDocumentValue;
    }

    public String getTitle() {
        return title;
    }
}
