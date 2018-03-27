package com.werelit.neurolls.neurolls.model;

import android.graphics.Bitmap;

public class Film extends Media{

    /** the ID of the film */
    //private int filmID;

    /** director of the film */
    private String director;

    /** run time duration in minutes of the film */
    private int duration = 0;

    /** production team of the film */
    private String production;

    /** synopsis of the film */
    private String synopsis;

    private String dateToWatch = "";

    private boolean isWatched = false;

    public Film(){

    }

    public Film(String id, String mediaName, String mediaGenre, String releaseYear,
                String director, int duration, String production, String synopsis) {

        super(id, mediaName, mediaGenre, releaseYear);

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

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    public String getDateToWatch() {
        return dateToWatch;
    }

    public void setDateToWatch(String dateToWatch) {
        this.dateToWatch = dateToWatch;
    }

}
