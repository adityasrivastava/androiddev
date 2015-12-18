package com.example.adityasrivastava.cardview.modal;

import java.io.Serializable;

/**
 * Created by adi on 6/12/15.
 */
public class PostDetails implements Serializable {

    private int id;
    private String title;
    private String thumbnail;
    private String content;
    private String date;
    private String url;
    private String excerpt;

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {

        return "\nID: "+this.id+" \nTitle: "+this.title+" \nThumbnail: "+this.thumbnail+" \nDate: "+this.date+" \nURL: "+this.url+" \nContent: "+this.content;
    }
}
