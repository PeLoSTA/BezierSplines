package de.peterloos.beziersplines.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.peterloos.beziersplines.R;

/**
 * Project: Bézier Splines Simulation
 * Copyright (c) 2017 by PeLo on 23.01.2017. All rights reserved.
 * Contact info: peterloos@gmx.de
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private int[] resources;

    private String[] docsHeaders;
    private String[] docsDescriptions;

    public ViewPagerAdapter(Context context, int[] resources) {
        this.context = context;
        this.resources = resources;

        Resources res = this.context.getResources();
        this.docsHeaders = res.getStringArray(R.array.docs_app_modes_headers);
        this.docsDescriptions = res.getStringArray(R.array.docs_app_modes_description);
    }

    @Override
    public int getCount() {
        return this.resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = LayoutInflater.from(this.context);
        View itemView = inflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        TextView textViewHeader = (TextView) itemView.findViewById(R.id.text_docs_header);

        TextView textViewDocs = (TextView) itemView.findViewById(R.id.text_docs_contents);
        textViewDocs.setMovementMethod(new ScrollingMovementMethod());

        textViewHeader.setText(this.docsHeaders[position]);
        Spanned description = fromHtml(this.docsDescriptions[position]);
        textViewDocs.setText(description);

        imageView.setImageResource(this.resources[position]);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @SuppressWarnings("deprecation")
    private static Spanned fromHtml(String html) {

        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.v("PeLo", "NEW API");
            // TODO: Needs to be tested with Nougat !!! Maybe Html.FROM_HTML_MODE_LEGACY is correct ?!?
            result = Html.fromHtml(html, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);
        } else {
            Log.v("PeLo", "OLD API");
            result = Html.fromHtml(html);
        }
        return result;
    }
}
