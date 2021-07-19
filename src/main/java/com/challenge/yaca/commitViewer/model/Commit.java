package com.challenge.yaca.commitViewer.model;

import java.util.Date;

public class Commit implements Comparable<Commit> {

    private final String id;
    private String author;
    private Date date;
    private String message;

    public Commit(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int compareTo(Commit o) {
        return this.date.compareTo(o.date);
    }
}
