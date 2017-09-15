package com.nikita.android.softfx;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XmlPullParserHandler {
    List<NewsItem> mNewsItems;
    private NewsItem mNewsItem;
    private String text;

    public XmlPullParserHandler() {
        mNewsItems = new ArrayList<>();
    }

    public List<NewsItem> getNewsItems() {
        return mNewsItems;
    }

    public List<NewsItem> parse(String rssXml) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(new StringReader(rssXml));

            int eventType = parser.getEventType();
            int titleCount = 0;
            int descriptionCount = 0;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("item")) {
                            mNewsItem = new NewsItem();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(tagName.equalsIgnoreCase("item")) {
                            mNewsItems.add(mNewsItem);
                        } else if (tagName.equalsIgnoreCase("title")) {
                            if (titleCount == 0) {
                                titleCount++;
                            } else {
                                mNewsItem.setTitle(text);
                            }
                        } else if (tagName.equalsIgnoreCase("description")) {
                            if (descriptionCount == 0) {
                                descriptionCount++;
                            } else  {
                                text = text.replaceAll("\\<.*?>","").substring(0, 150);
                                mNewsItem.setDescription(text);
                            }
                        } else if (tagName.equalsIgnoreCase("pubDate")) {
                            mNewsItem.setPubDate(text);
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return mNewsItems;
    }
}
