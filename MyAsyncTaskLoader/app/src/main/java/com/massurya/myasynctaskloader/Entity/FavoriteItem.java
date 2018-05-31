package com.massurya.myasynctaskloader.Entity;

import android.database.Cursor;

import static android.provider.BaseColumns._ID;
import static com.massurya.myasynctaskloader.ContentProvider.DatabaseContract.getColumnInt;
import static com.massurya.myasynctaskloader.ContentProvider.DatabaseContract.getColumnString;
import static com.massurya.myasynctaskloader.ContentProvider.FavoriteColumns.COLUMN_OVERVIEW;
import static com.massurya.myasynctaskloader.ContentProvider.FavoriteColumns.COLUMN_POSTER;
import static com.massurya.myasynctaskloader.ContentProvider.FavoriteColumns.COLUMN_RELEASE_DATE;
import static com.massurya.myasynctaskloader.ContentProvider.FavoriteColumns.COLUMN_TITLE;
import static com.massurya.myasynctaskloader.ContentProvider.FavoriteColumns.COLUMN_VOTE;

public class FavoriteItem {
    private String name;
    private String desc;
    private String date;
    private String image;
    private String rating;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FavoriteItem(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.name = getColumnString(cursor, COLUMN_TITLE);
        this.image = getColumnString(cursor, COLUMN_POSTER);
        this.date = getColumnString(cursor, COLUMN_RELEASE_DATE);
        this.rating = getColumnString(cursor, COLUMN_VOTE);
        this.desc = getColumnString(cursor, COLUMN_OVERVIEW);
    }
}
