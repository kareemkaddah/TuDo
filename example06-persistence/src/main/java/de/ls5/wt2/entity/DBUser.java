package de.ls5.wt2.entity;

import de.ls5.wt2.AppUserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class DBUser extends DBIdentified{

    @Column(nullable = false,unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    private AppUserRole role;
    private String firstName, lastName, password;
    private String token;


    public DBUser(String username, AppUserRole role, String firstName, String lastname, String password,
                  String token) {
        this.username = username;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastname;
        this.password = password;
        this.token = token;

    }

    public DBUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AppUserRole getRole() {
        return role;
    }

    public void setRole(AppUserRole role) {
        this.role = role;
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

    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    // No Need to save the token in the database
    @Transient
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
