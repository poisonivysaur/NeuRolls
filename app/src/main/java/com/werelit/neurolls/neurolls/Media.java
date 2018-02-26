package com.werelit.neurolls.neurolls;

public class Media {
    /** name for the media */
    private String mMediaName;

    /** description for the media */
    private String mMediaAuthor;

    /** weight for the media */
    private double mMediaYear = 1800;

    public Media(String mediaName, String mediaAuthor, double entertainmenmentYear) {
        mMediaName = mediaName;
        mMediaAuthor = mediaAuthor;
        mMediaYear = entertainmenmentYear;
    }

    public String getmMediaName() {
        return mMediaName;
    }

    public void setmMediaName(String mMediaName) {
        this.mMediaName = mMediaName;
    }

    public String getmMediaAuthor() {
        return mMediaAuthor;
    }

    public void setmMediaAuthor(String mMediaAuthor) {
        this.mMediaAuthor = mMediaAuthor;
    }

    public double getmMediaYear() {
        return mMediaYear;
    }

    public void setmMediaYear(double mMediaYear) {
        this.mMediaYear = mMediaYear;
    }
}
