package com.example.myloginapplication.Model;

import android.os.Parcelable;

public class Member {
    private String username,emailId,password,address,evc,propertytype;
    private String noofrooms;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPropertytype() {
        return propertytype;
    }

    public void setPropertytype(String propertytype) {
        this.propertytype = propertytype;
    }

    public String getEvc() {
        return evc;
    }

    public void setEvc(String evc) {
        this.evc = evc;
    }

    public String getNoofrooms() {
        return noofrooms;
    }

    public void setNoofrooms(String noofrooms) {
        this.noofrooms = noofrooms;
    }
}
