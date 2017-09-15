package com.nikita.android.softfx.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.nikita.android.softfx.database.NewsDbSchema.*;

public class NewsBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "news.db";

    public NewsBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + NewsTable.LIVE_NAME + "(" +
                " _id integer primary key autoincrement, " +
                NewsTable.Cols.TITLE + ", " +
                NewsTable.Cols.DESCRIPTION + ", " +
                NewsTable.Cols.PUBDATE +
                ")"
        );
        sqLiteDatabase.execSQL("create table " + NewsTable.ANALYTICS_NAME + "(" +
                " _id integer primary key autoincrement, " +
                NewsTable.Cols.TITLE + ", " +
                NewsTable.Cols.DESCRIPTION + ", " +
                NewsTable.Cols.PUBDATE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
