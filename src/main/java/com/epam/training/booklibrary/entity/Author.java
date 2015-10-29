package com.epam.training.booklibrary.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by URA on 30.09.2015.
 */
public class Author implements Serializable {
    private static final long serialVersionUID = 04L;

    private int id;
    private String fioRU;
    private String fioEN;
    private Date birthDay;

    public Author() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFioRU() {
        return fioRU;
    }

    public void setFioRU(String fioRU) {
        this.fioRU = fioRU;
    }

    public String getFioEN() {
        return fioEN;
    }

    public void setFioEN(String fioEN) {
        this.fioEN = fioEN;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }
}
