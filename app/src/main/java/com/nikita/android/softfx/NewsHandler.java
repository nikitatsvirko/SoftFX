package com.nikita.android.softfx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nikita.android.softfx.database.NewsBaseHelper;
import com.nikita.android.softfx.database.NewsCursorWrapper;

import java.util.ArrayList;
import java.util.List;

import static com.nikita.android.softfx.database.NewsDbSchema.NewsTable;

public class NewsHandler {
    private static NewsHandler sNewsHandler;

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private String mNewsType;

    public static NewsHandler get(Context context, String newsType) {
        if (sNewsHandler == null) {
            sNewsHandler = new NewsHandler(context, newsType);
        }

        return sNewsHandler;
    }

    private NewsHandler(Context context, String newsType) {
        mContext = context.getApplicationContext();
        mNewsType = newsType;
        mDatabase = new NewsBaseHelper(mContext).getWritableDatabase();
    }

    public void addNews(NewsItem item) {
        ContentValues values = getContentValues(item);

        if (getNewsItem(item.getTitle()) != null) {
            switch (mNewsType) {
                case "live":
                    mDatabase.insert(NewsTable.LIVE_NAME, null, values);
                    break;
                case "analytics":
                    mDatabase.insert(NewsTable.ANALYTICS_NAME, null, values);
                    break;
                default:
                    break;
            }
        }
    }

    public void deleteNewsItem(NewsItem item) {
        String title = item.getTitle();

        switch (mNewsType) {
            case "live":
                mDatabase.delete(NewsTable.LIVE_NAME, NewsTable.Cols.TITLE + " = ?", new String[] { title });
                break;
            case "analytics":
                mDatabase.delete(NewsTable.ANALYTICS_NAME, NewsTable.Cols.TITLE + " = ?", new String[] { title });
                break;
            default:
                break;
        }
    }

    public NewsItem getNewsItem(String title) {
        NewsCursorWrapper cursor = queryNews(
                NewsTable.Cols.TITLE + " = ?",
                new String[] { title }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getNewsItem();
        } finally {
            cursor.close();
        }
    }

    public List<NewsItem> getNews() {
        List<NewsItem> items = new ArrayList<>();

        NewsCursorWrapper cursor = queryNews(null, null);

        if (cursor.moveToFirst()) {
            do {
                items.add(cursor.getNewsItem());
            } while (cursor.moveToNext());
        }

        cursor.close();

        return items;
    }

    private static ContentValues getContentValues(NewsItem item) {
        ContentValues values = new ContentValues();
        values.put(NewsTable.Cols.TITLE, item.getTitle());
        values.put(NewsTable.Cols.DESCRIPTION, item.getDescription());
        values.put(NewsTable.Cols.PUBDATE, item.getPubDate());

        return values;
    }

    private NewsCursorWrapper queryNews(String whereClause, String[] whereArgs) {
        Cursor cursor;

        mDatabase = new NewsBaseHelper(mContext).getReadableDatabase();

        switch (mNewsType) {
            case "live":
                 cursor = mDatabase.query(
                        NewsTable.LIVE_NAME,
                        null,
                        whereClause,
                        whereArgs,
                        null,
                        null,
                        null
                );
                break;
            case "analytics":
                cursor = mDatabase.query(
                        NewsTable.ANALYTICS_NAME,
                        null,
                        whereClause,
                        whereArgs,
                        null,
                        null,
                        null
                );
                break;
            default:
                cursor = mDatabase.query(null, null, null, null, null, null, null);
                break;
        }


        return new NewsCursorWrapper(cursor);
    }


}
