package com.epam.training.booklibrary.datamodels.entity;

import java.io.Serializable;

/**
 * Class the Book contains realization for objects
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    // record identifier
    private int id;
    // identifier of the author of the book
    private int authorID;
    // author of the book
    private Author bookAuthor;
    // book genre identifier
    private int genreID;
    // book genre
    private BookGenre bookGenre;
    // identifier of publishing house
    private int publisherID;
    // publishing house
    private Publisher bookPublisher;
    // year of the edition
    private int publishYear;
    // ISBN code
    private String isbn;
    // name of the book
    private String name;
    // short description of the book
    private String shortDescription;
    // number of copies of the book
    private int numberCopies;
    // book cover
    private byte[] coverImage;
    // sign of the remove book
    private boolean isDeleted;

    /**
     * Constructor of a class by default
     */
    public Book() {
    }

    /**
     * Constructor of a class
     */
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

    // getters and setters
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
