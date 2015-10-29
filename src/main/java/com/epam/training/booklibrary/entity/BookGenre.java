package com.epam.training.booklibrary.entity;

import java.io.Serializable;

/**
 * Created by URA on 30.09.2015.
 */
public class BookGenre implements Serializable {
    private static final long serialVersionUID = 011L;

    private int id;
    private String nameRU;
    private String nameEN;
    private int bookSectionID;
    private BookSection bookSection;

    public BookGenre() {
    }

    public BookGenre(int id, String nameRU, String nameEN, int bookSectionID, BookSection bookSection) {
        this.id = id;
        this.nameRU = nameRU;
        this.nameEN = nameEN;
        this.bookSectionID = bookSectionID;
        this.bookSection = bookSection;
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

    public int getBookSectionID() {
        return bookSectionID;
    }

    public void setBookSectionID(int bookSectionID) {
        this.bookSectionID = bookSectionID;
    }

    public BookSection getBookSection() {
        return bookSection;
    }

    public void setBookSection(BookSection bookSection) {
        this.bookSection = bookSection;
    }
}
