package com.epam.training.booklibrary.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Class the Author of the book contains realization for objects
 */
public class Author implements Serializable {
    private static final long serialVersionUID = 04L;

    // record identifier
    private int id;
    // Name of the author in Russian
    private String fioRU;
    // Name of the author in English
    private String fioEN;
    // Date of birth of the author
    private Date birthDay;


    /**
     * Constructor of a class by default
     */
    public Author() {
    }

    // getters and setters
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
