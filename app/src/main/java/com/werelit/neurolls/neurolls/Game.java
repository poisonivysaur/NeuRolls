package com.werelit.neurolls.neurolls;

public class Game extends Media{

    /** platform of the game */
    private String platform;

    /** development production team or publisher of the game */
    private String publisher;

    /** storyline of the game */
    private String storyline;

    /** the series that the game is part of */
    private String series;

    public Game(String mediaName, String mediaGenre, double releaseYear,
                String platform, String publisher, String storyline, String series) {

        super(mediaName, mediaGenre, releaseYear);

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
}
