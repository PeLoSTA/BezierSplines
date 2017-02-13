package de.peterloos.beziersplines.activities;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import de.peterloos.beziersplines.R;
import de.peterloos.beziersplines.adapters.ViewPagerAdapter;

/**
 * Project: Bézier Splines Simulation
 * Copyright (c) 2017 by PeLo on 23.01.2017. All rights reserved.
 * Contact info: peterloos@gmx.de
 */

public class DocumentationActivity extends AppCompatActivity implements OnPageChangeListener {

    private LinearLayout layoutPagerIndicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter viewPagerAdapter;

    private int[] imagesResources = {
            R.mipmap.device_2017_02_01,
            R.mipmap.device_2017_02_02,
            R.mipmap.device_2017_02_03,
            R.mipmap.device_2017_02_04
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_documentation);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // prefer action bar title with two lines
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_documentation);
            actionBar.setSubtitle(this.getString(R.string.app_main_title));
        }

        // retrieve control references
        ViewPager viewPager = (ViewPager) this.findViewById(R.id.pager_documentation);
        this.layoutPagerIndicator = (LinearLayout) this.findViewById(R.id.view_pager_count_dots);

        // setup pager's adapter
        this.viewPagerAdapter = new ViewPagerAdapter(this, this.imagesResources);
        viewPager.setAdapter(this.viewPagerAdapter);
        viewPager.setCurrentItem(0);

        // connect with event handlers
        viewPager.addOnPageChangeListener(this);

        this.setUiPageViewController();
    }

    private void setUiPageViewController() {

        this.dotsCount = viewPagerAdapter.getCount();
        this.dots = new ImageView[this.dotsCount];

        for (int i = 0; i < this.dotsCount; i++) {
            this.dots[i] = new ImageView(this);
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.nonselecteditem_dot);
            this.dots[i].setImageDrawable(drawable);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);
            this.layoutPagerIndicator.addView(this.dots[i], params);
        }

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.selecteditem_dot);
        this.dots[0].setImageDrawable(drawable);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < this.dotsCount; i++) {

            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.nonselecteditem_dot);
            this.dots[i].setImageDrawable(drawable);
        }

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.selecteditem_dot);
        this.dots[position].setImageDrawable(drawable);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
