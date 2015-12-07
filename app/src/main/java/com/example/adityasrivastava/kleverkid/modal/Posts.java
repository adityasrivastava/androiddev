package com.example.adityasrivastava.kleverkid.modal;

import java.util.List;


public class Posts {

    String status;
    int count;
    int pages;
    Object category;

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    List<PostDetails> posts;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PostDetails> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDetails> post) {
        this.posts = post;
    }
}
