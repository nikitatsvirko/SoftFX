package com.nikita.android.softfx;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nikita.android.softfx.adapters.NewsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private NewsPagerAdapter mNewsPagerAdapter;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager());
        mNewsPagerAdapter.addFragment(NewsListFragment.newInstance("https://widgets.spotfxbroker.com:8088/GetLiveNewsRss"), "Live");
        mNewsPagerAdapter.addFragment(NewsListFragment.newInstance("https://widgets.spotfxbroker.com:8088/GetAnalyticsRss"), "Analytics");
        mViewPager = (ViewPager) findViewById(R.id.news_fragment_pager);
        mViewPager.setAdapter(mNewsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.news_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
