package com.werelit.neurolls.neurolls.model;

public class Book extends Media{

    /** author of the book */
    private String author;

    /** publication company of the book */
    private String publisher;

    /** description of the book */
    private String description;

    public Book(){

    }

    public Book(String mediaName, String mediaGenre, int releaseYear,
                String author, String publisher, String description) {

        super(mediaName, mediaGenre, releaseYear);

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
}
