package com.werelit.neurolls.neurolls.model;

import android.graphics.Bitmap;

public class Media {

    /** the ID of the media */
    private String mediaID;

    /** name of the media */
    private String mMediaName;

    /** the genre of the media */
    private String mMediaGenre;

    /** year released or published of the media */
    private String mMediaYear = "9999";

    /** if the media was archived or not */
    private boolean isArchived = false;

    private String mImageDir = "";

    private Bitmap thumbnailBmp;

    private String notifSettings = "";

    public static final int CATEGORY_FILMS = 1;
    public static final int CATEGORY_BOOKS = 2;
    public static final int CATEGORY_GAMES = 3;

    public Media(){

    }

    public Media(String id, String mediaName, String mediaGenre, String releaseYear) {
        this(id, mediaName, mediaGenre, releaseYear, "test/sample/directory/image.jpg");
    }

    public Media(String id, String mediaName, String mediaGenre, String releaseYear, String imageDir) {
        mediaID = id;
        mMediaName = mediaName;
        mMediaGenre = mediaGenre;
        mMediaYear = releaseYear;
        mImageDir = imageDir;
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

    public String getmMediaYear() {
        return mMediaYear;
    }

    public void setmMediaYear(String mMediaYear) {
        this.mMediaYear = mMediaYear;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public String getImageDir() {
        return mImageDir;
    }

    public void setImageDir(String imageDir) {
        mImageDir = imageDir;
    }

    public String getNotifSettings() {
        return notifSettings;
    }

    public void setNotifSettings(String notifSettings) {
        this.notifSettings = notifSettings;
    }

    public Bitmap getThumbnailBmp() {
        return thumbnailBmp;
    }

    public void setThumbnailBmp(Bitmap thumbnailBmp) {
        this.thumbnailBmp = thumbnailBmp;
    }
}
