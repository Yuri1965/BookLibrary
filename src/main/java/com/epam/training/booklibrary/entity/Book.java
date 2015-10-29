package com.epam.training.booklibrary.entity;

import java.io.Serializable;

/**
 * Created by URA on 30.09.2015.
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 012L;

    private int id;
    private int authorID;
    private Author bookAuthor;
    private int genreID;
    private BookGenre bookGenre;
    private int publisherID;
    private Publisher bookPublisher;
    private int publishYear;
    private String isbn;
    private String name;
    private String shortDescription;
    private int numberCopies;
    private byte[] coverImage;
    private boolean isDeleted;

    public Book() {
    }

    public Book(int id, int authorID, Author bookAuthor, int genreID, BookGenre bookGenre, int publisherID,
                Publisher bookPublisher, int publishYear, String isbn, String name,
                int numberCopies, boolean isDeleted) {
        this.id = id;
        this.authorID = authorID;
        this.bookAuthor = bookAuthor;
        this.genreID = genreID;
        this.bookGenre = bookGenre;
        this.publisherID = publisherID;
        this.bookPublisher = bookPublisher;
        this.publishYear = publishYear;
        this.isbn = isbn;
        this.name = name;
        this.numberCopies = numberCopies;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public Author getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(Author bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public int getGenreID() {
        return genreID;
    }

    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    public BookGenre getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(BookGenre bookGenre) {
        this.bookGenre = bookGenre;
    }

    public int getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }

    public Publisher getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(Publisher bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getNumberCopies() {
        return numberCopies;
    }

    public void setNumberCopies(int numberCopies) {
        this.numberCopies = numberCopies;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
