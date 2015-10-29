package com.epam.training.booklibrary.entity;

import java.io.Serializable;

/**
 * Created by URA on 30.09.2015.
 */
public class OrderType implements Serializable {
    private static final long serialVersionUID = 06L;

    private int id;
    private String nameRU;
    private String nameEN;

    public OrderType() {
    }

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
