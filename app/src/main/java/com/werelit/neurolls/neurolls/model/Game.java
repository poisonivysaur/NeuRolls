package com.werelit.neurolls.neurolls.model;

public class Game extends Media{

    /** the ID of the game */
    //private int gameID;

    /** platform of the game */
    private String platform;

    /** development production team or publisher of the game */
    private String publisher;

    /** the series that the game is part of */
    private String series;

    /** storyline of the game */
    private String storyline;

    private String dateToPlay = "";

    private boolean isPlayed = false;

    public Game(){

    }

    public Game(String id, String mediaName, String mediaGenre, String releaseYear,
                String platform, String publisher, String series, String storyline) {

        super(id, mediaName, mediaGenre, releaseYear);

        this.platform = platform;
        this.publisher = publisher;
        this.storyline = storyline;
        this.series = series;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getStoryline() {
        return storyline;
    }

    public void setStoryline(String storyline) {
        this.storyline = storyline;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }
}
