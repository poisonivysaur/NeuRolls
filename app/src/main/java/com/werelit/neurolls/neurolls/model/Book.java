package com.werelit.neurolls.neurolls.model;

public class Book extends Media{

    /** the ID of the book */
    //private String bookID;

    /** author of the book */
    private String author;

    /** number of pages of the book */
    private int pages = 0;

    /** publication company of the book */
    private String publisher;

    /** description of the book */
    private String description;

    private String dateToRead = "";
    private String timeToRead = "";

    private boolean isRead = false;

    public Book(){

    }

    public Book(String id, String mediaName, String mediaGenre, String releaseYear,
                String author, int pages, String publisher, String description) {

        super(id, mediaName, mediaGenre, releaseYear);

        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.pages = pages;
    }

    // scaffholding method to be deleted when no longer useful
    public Book(String id, String mediaName, String mediaGenre, String releaseYear,
                String author, String publisher, String description) {

        super(id, mediaName, mediaGenre, releaseYear);

        this.author = author;
        this.publisher = publisher;
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getDateToRead() {
        return dateToRead;
    }

    public void setDateToRead(String dateToRead) {
        this.dateToRead = dateToRead;
    }

    public String getTimeToRead() {
        return timeToRead;
    }

    public void setTimeToRead(String timeToRead) {
        this.timeToRead = timeToRead;
    }
}
