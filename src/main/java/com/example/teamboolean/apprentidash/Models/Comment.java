package com.example.teamboolean.apprentidash.Models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    AppUser author;

    @ManyToOne
    Discussion parentDiscussion;

    @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss")
    LocalDateTime createdAt;

    int thumbsUp;
    String body;

    public Comment() {}

    public Comment(AppUser author, Discussion parentDiscussion, String body) {
        this.author = author;
        this.parentDiscussion = parentDiscussion;
        this.body = body;
        this.createdAt = LocalDateTime.now(ZoneId.of("UTC-07:00"));
        this.thumbsUp = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppUser getAuthor() {
        return author;
    }

    public void setAuthor(AppUser author) {
        this.author = author;
    }

    public Discussion getParentDiscussion() {
        return parentDiscussion;
    }

    public void setParentDiscussion(Discussion parentDiscussion) {
        this.parentDiscussion = parentDiscussion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(int thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
