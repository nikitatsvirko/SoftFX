package com.nikita.android.softfx.database;

public class NewsDbSchema {
    public static final class NewsTable {
        public static final String LIVE_NAME = "livenews";
        public static final String ANALYTICS_NAME = "analyticsnews";

        public static final class Cols {
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String PUBDATE = "pubdate";
        }
    }
}
