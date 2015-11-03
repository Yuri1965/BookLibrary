package com.epam.training.booklibrary.entity;

import java.io.Serializable;

/**
 * The class contains realization for objects order Type
 */
public class OrderType implements Serializable {
    private static final long serialVersionUID = 06L;

    // the identifier like the order
    private int id;
    // the name like the order in Russian
    private String nameRU;
    // the name like the order in English
    private String nameEN;

    /**
     * Constructor of a class by default
     */
    public OrderType() {
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
