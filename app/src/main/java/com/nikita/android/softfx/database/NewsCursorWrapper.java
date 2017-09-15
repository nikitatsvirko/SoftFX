package com.nikita.android.softfx.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nikita.android.softfx.NewsItem;

import static com.nikita.android.softfx.database.NewsDbSchema.*;

public class NewsCursorWrapper extends CursorWrapper {
    public NewsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public NewsItem getNewsItem() {
        String titleString = getString(getColumnIndex(NewsTable.Cols.TITLE));
        String descriptionString = getString(getColumnIndex(NewsTable.Cols.DESCRIPTION));
        String pubDateString = getString(getColumnIndex(NewsTable.Cols.PUBDATE));

        NewsItem item = new NewsItem();
        item.setTitle(titleString);
        item.setDescription(descriptionString);
        item.setPubDate(pubDateString);

        return item;
    }
}
