package com.epam.training.booklibrary.entity;

import java.io.Serializable;

/**
 * The class contains realization for object Publishing house
 */
public class Publisher implements Serializable {
    private static final long serialVersionUID = 05L;

    // Identifier of publishing house
    private int id;
    // The name of publishing house in Russian
    private String nameRU;
    // The name of publishing house in English
    private String nameEN;

    /**
     * Constructor of a class by default
     */
    public Publisher() {
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
