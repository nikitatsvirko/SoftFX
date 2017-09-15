package com.nikita.android.softfx.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikita.android.softfx.NewsItem;
import com.nikita.android.softfx.R;

import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsHolder>{

    private Context mContext;
    private List<NewsItem> mNews;

    public NewsRecyclerAdapter(Context context, List<NewsItem> news) {
        mNews = news;
        mContext = context;
    }


    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.news_list_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        NewsItem item = mNews.get(position);
        holder.bindNewsItem(item);
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public void updateData(List<NewsItem> items) {
        mNews = items;
        notifyDataSetChanged();
    }

    class NewsHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextView;
        private TextView mPubDateTextView;
        private TextView mDescriptionTextView;


        public void bindNewsItem(final NewsItem newsItem) {
            mTitleTextView.setText(newsItem.getTitle());
            mPubDateTextView.setText(newsItem.getPubDate());
            mDescriptionTextView.setText(newsItem.getDescription());
        }

        public NewsHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            mPubDateTextView = (TextView) itemView.findViewById(R.id.pub_date_text_view);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.description_text_view);
        }
    }
}
