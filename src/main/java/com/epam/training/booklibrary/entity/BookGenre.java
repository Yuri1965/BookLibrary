package com.epam.training.booklibrary.entity;

import java.io.Serializable;

/**
 * The class contains realization for objects a book Genre
 */
public class BookGenre implements Serializable {
    private static final long serialVersionUID = 011L;

    // record identifier
    private int id;
    // the name of a genre in Russian
    private String nameRU;
    // the name of a genre in English
    private String nameEN;
    // identifier of the section of literature
    private int bookSectionID;
    // section of literature
    private BookSection bookSection;

    /**
     * Constructor of a class by default
     */
    public BookGenre() {
    }

    /**
     * Constructor of a class
     */
    public BookGenre(int id, String nameRU, String nameEN, int bookSectionID, BookSection bookSection) {
        this.id = id;
        this.nameRU = nameRU;
        this.nameEN = nameEN;
        this.bookSectionID = bookSectionID;
        this.bookSection = bookSection;
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
