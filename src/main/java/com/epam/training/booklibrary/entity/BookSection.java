package com.epam.training.booklibrary.entity;

import java.io.Serializable;

/**
 * The class contains realization for objects the Section of literature
 */
public class BookSection implements Serializable {
    private static final long serialVersionUID = 07L;

    // identifier of the section of literature
    private int id;
    // the name of the section in Russian
    private String nameRU;
    // the name of the section in English
    private String nameEN;


    /**
     * Constructor of a class by default
     */
    public BookSection() {
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameRU() {
        return nameRU;
    }

    public void setNameRU(String nameRU) {
        this.nameRU = nameRU;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }
}
