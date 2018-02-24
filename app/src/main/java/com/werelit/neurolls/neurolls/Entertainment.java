package com.werelit.neurolls.neurolls;

public class Entertainment {
    /** name for the entertainment */
    private String mEntertainmentName;

    /** description for the entertainment */
    private String mEntertainmentAuthor;

    /** weight for the entertainment */
    private double mEntertainmentYear = 1800;

    public Entertainment(String entertainmentName, String entertainmentAuthor, double entertainmenmentYear) {
        mEntertainmentName = entertainmentName;
        mEntertainmentAuthor = entertainmentAuthor;
        mEntertainmentYear = entertainmenmentYear;
    }

    public String getmEntertainmentName() {
        return mEntertainmentName;
    }

    public void setmEntertainmentName(String mEntertainmentName) {
        this.mEntertainmentName = mEntertainmentName;
    }

    public String getmEntertainmentAuthor() {
        return mEntertainmentAuthor;
    }

    public void setmEntertainmentAuthor(String mEntertainmentAuthor) {
        this.mEntertainmentAuthor = mEntertainmentAuthor;
    }

    public double getmEntertainmentYear() {
        return mEntertainmentYear;
    }

    public void setmEntertainmentYear(double mEntertainmentYear) {
        this.mEntertainmentYear = mEntertainmentYear;
    }
}
