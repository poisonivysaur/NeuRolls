package com.werelit.neurolls.neurolls.model;

public class Film extends Media{

    /** run time duration in minutes of the film */
    private int duration = 0;

    /** director of the film */
    private String director;

    /** production team of the film */
    private String production;

    /** synopsis of the film */
    private String synopsis;

    public Film(String mediaName, String mediaGenre, double releaseYear,
                int duration, String director, String production, String synopsis) {

        super(mediaName, mediaGenre, releaseYear);

        this.duration = duration;
        this.director = director;
        this.production = production;
        this.synopsis = synopsis;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
