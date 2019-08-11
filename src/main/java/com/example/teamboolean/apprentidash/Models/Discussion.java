package com.example.teamboolean.apprentidash.Models;

import org.apache.tomcat.jni.Local;
import org.hibernate.validator.internal.util.stereotypes.ThreadSafe;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Entity
public class Discussion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    AppUser author;

    @OneToMany (mappedBy = "parentDiscussion")
    List<Comment> comments;

    @DateTimeFormat(pattern="yyyy-mm-dd HH:mm:ss")
    LocalDateTime createdAt;

    String title;
    String body;
    int thumbsUp;

    public Discussion() {}

    public Discussion(AppUser author, String title, String body) {
        this.author = author;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(int thumbsUp) {
        this.thumbsUp = thumbsUp;
    }
}
