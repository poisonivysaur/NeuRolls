package com.werelit.neurolls.neurolls;

public class Media {
    /** name of the media */
    private String mMediaName;

    /** the genre of the media */
    private String mMediaGenre;

    /** year released or published of the media */
    private double mMediaYear = 1800;

    public Media(String mediaName, String mediaAuthor, double entertainmenmentYear) {
        mMediaName = mediaName;
        mMediaGenre = mediaAuthor;
        mMediaYear = entertainmenmentYear;
    }

    public String getmMediaName() {
        return mMediaName;
    }

    public void setmMediaName(String mMediaName) {
        this.mMediaName = mMediaName;
    }

    public String getmMediaGenre() {
        return mMediaGenre;
    }

    public void setmMediaGenre(String mMediaGenre) {
        this.mMediaGenre = mMediaGenre;
    }

    public double getmMediaYear() {
        return mMediaYear;
    }

    public void setmMediaYear(double mMediaYear) {
        this.mMediaYear = mMediaYear;
    }
}
