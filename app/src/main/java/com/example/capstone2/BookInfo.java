package com.example.capstone2;

import java.util.Date;

public class BookInfo {
    private String name = "";
    private String author = "";
    private String imgLink = "";
    private String isbn13 = "";
    public String loanAvailable;
    private String hasBook="";
    private String publisher="";

//    public BookInfo(String name, String author, String imgLink, String isbn13){
//        this.name=name;
//        this.author=author;
//        this.imgLink=imgLink;
//        this.isbn13=isbn13;
//    }



    public String getAuthor() {
        return author;
    }

    public String getImgLink() {
        return imgLink;
    }

    public String getName() {
        return name;
    }

    public String getIsbn13() {
        return isbn13;
    }
    public String getLoanAvailable() {
        return loanAvailable;
    }

    public String getHasBook() {
        return hasBook;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setLoanAvailable(String loanAvailable) {
        this.loanAvailable = loanAvailable;
    }

    public void setHasBook(String hasBook) {
        this.hasBook = hasBook;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}

