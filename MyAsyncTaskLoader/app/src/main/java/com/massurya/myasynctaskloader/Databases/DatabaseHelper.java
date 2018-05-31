package com.massurya.myasynctaskloader.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.massurya.myasynctaskloader.ContentProvider.FavoriteColumns;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "catalogue_movie";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_MOVIE = "create table " + FavoriteColumns.TABLE_NAME + " (" +
                FavoriteColumns.COLUMN_ID + " integer primary key autoincrement, " +
                FavoriteColumns.COLUMN_TITLE + " text not null, " +
                FavoriteColumns.COLUMN_POSTER + " text not null, " +
                FavoriteColumns.COLUMN_RELEASE_DATE + " text not null, " +
                FavoriteColumns.COLUMN_VOTE + " text not null, " +
                FavoriteColumns.COLUMN_OVERVIEW + " text not null " +
                ");";

        sqLiteDatabase.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(String.format("DROP TABLE MOVIE IF EXIST%s", FavoriteColumns.TABLE_NAME));
        onCreate(sqLiteDatabase);
    }
}
