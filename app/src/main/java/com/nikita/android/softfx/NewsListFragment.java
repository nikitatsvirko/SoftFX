package com.nikita.android.softfx;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nikita.android.softfx.adapters.NewsRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment {

    private final String REQUEST_TAG = "Volley Request";

    private RecyclerView mNewsRecyclerView;
    private NewsRecyclerAdapter mNewsRecyclerAdapter;

    private String mRssUrl;
    private String mNewsType;
    private List<NewsItem> mNewsItems = new ArrayList<>();

    private RequestQueue mRequestQueue;
    private StringRequest mRequest;

    public static NewsListFragment newInstance(String url) {
        NewsListFragment fragment = new NewsListFragment();

        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mRssUrl = getArguments().getString("url");

        if (mRssUrl != null) {
            if (mRssUrl.contains("Live")) {
                mNewsType = "live";
            } else if (mRssUrl.contains("Analytics")) {
                mNewsType = "analytics";
            }
        }

        if (isOnline()) {
            loadNews(mRssUrl);
            Log.d("FRAGMENT " + mNewsType, "Loading from network");
        } else {
            NewsHandler newsHandler = NewsHandler.get(getActivity(), mNewsType);
            mNewsItems = newsHandler.getNews();
            Log.d("FRAGMENT " + mNewsType, "Loading from db");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);

        mNewsRecyclerView = rootView.findViewById(R.id.news_list_recycler_view);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNewsRecyclerAdapter = new NewsRecyclerAdapter(getContext(), mNewsItems);
        mNewsRecyclerView.setAdapter(mNewsRecyclerAdapter);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(REQUEST_TAG);
        }
    }

    private boolean isOnline() {
        boolean isOnline;

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        isOnline = netInfo != null && netInfo.isConnectedOrConnecting();

        return isOnline;
    }

    private void loadNews(String rssUrl) {
        mRequestQueue = Volley.newRequestQueue(getContext());

        mRequest = new StringRequest(rssUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("TAG", mRssUrl);
                    XmlPullParserHandler parser = new XmlPullParserHandler();
                    mNewsItems = parser.parse(response);
                    mNewsRecyclerAdapter.updateData(mNewsItems);
                    saveNews(mNewsItems, mNewsType);
                } catch (Exception e) {
                    Log.e(REQUEST_TAG, "Error: " + e.getMessage());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(REQUEST_TAG, "Error: " + error.getMessage());
                    }
                });

        mRequest.setTag(REQUEST_TAG);
        mRequestQueue.add(mRequest);
    }

    private void saveNews(List<NewsItem> newsItems, String newsType) {
        for (int i = 0; i < newsItems.size(); i++) {
            NewsHandler.get(getActivity(), mNewsType).addNews(newsItems.get(i));
        }
    }
}
