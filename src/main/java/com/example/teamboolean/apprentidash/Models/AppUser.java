package com.example.teamboolean.apprentidash.Models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(unique=true)
    String username;
    String password;
    String email;
    String phone;
    String firstName;
    String lastName;
    String managerName;

    @Column(columnDefinition = "boolean default true")
    boolean optEmail;

    @Column(columnDefinition = "boolean default true")
    boolean optText;

    @OneToMany (mappedBy = "user")
    List<Day> days;

    @OneToOne
    private
    Day Currentday;

    @OneToMany (mappedBy = "author")
    List<Discussion> discussions;

    @OneToMany (mappedBy = "author")
    List<Comment> comments;

    public AppUser(){}


    public AppUser(String username, String password, String firstName, String lastName, String managerName, String email){
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.managerName = managerName;
        this.email = email;
    }

    public AppUser(String username, String password, String firstName, String lastName, String managerName, String email, String phone) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.managerName = managerName;
        this.email = email;
        this.phone = phone;
    }

    public AppUser(String username, String password, String firstName, String lastName, String managerName, String email, String phone, boolean optEmail, boolean optText) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.managerName = managerName;
        this.email = email;
        this.phone = phone;
        this.optEmail = optEmail;
        this.optText = optText;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public Day getCurrentday() {
        return Currentday;
    }

    public void setCurrentday(Day currentday) {
        Currentday = currentday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOptEmail(boolean optEmail) {
        this.optEmail = optEmail;
    }

    public boolean isOptEmail() {
        return optEmail;
    }

    public void setOptText(boolean optText) {
        this.optText = optText;
    }

    public boolean isOptText() {
        return optText;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
