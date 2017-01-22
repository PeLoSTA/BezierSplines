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
 * Created by Peter on 19.10.2016.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private int[] resources;

    private String newLine;

    private String[] docsHeaders;
    private String[] docsDescriptions;

    public ViewPagerAdapter(Context context, int[] resources) {
        this.context = context;
        this.resources = resources;

        this.newLine = System.getProperty("line.separator");

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



        // this.docsDescriptions = Html.fromHtml(res.getStringArray(R.array.docs_app_modes_description));
        // textViewDocs.setText(Html.fromHtml(this.docsDescriptions[position], Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE));

        Spanned description = this.fromHtml (this.docsDescriptions[position]);
        textViewDocs.setText(description);



//        textViewHeader.setText("Header");
//
//        textViewDocs.append("eins");
//        textViewDocs.append(this.newLine);
//        textViewDocs.append("zwei");
//        textViewDocs.append(this.newLine);
//        textViewDocs.append("drei");
//        textViewDocs.append(this.newLine);
//        textViewDocs.append("vier");
//        textViewDocs.append(this.newLine);
//        textViewDocs.append("fuenf");
//        textViewDocs.append(this.newLine);
//        textViewDocs.append("sechs");
//        textViewDocs.append(this.newLine);
//        textViewDocs.append("sieben");
//        textViewDocs.append(this.newLine);
//        textViewDocs.append("acht");
//        textViewDocs.append(this.newLine);
//        textViewDocs.append("nein");
//        textViewDocs.append(this.newLine);
//        textViewDocs.append("zwhn");
//        textViewDocs.append(this.newLine);

        imageView.setImageResource(this.resources[position]);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @SuppressWarnings("deprecation")
    private static Spanned fromHtml (String html) {

        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.v("PeLo", "NEW API");
            result = Html.fromHtml(html, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);

            // TODO: Den Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE auf dem Samsung testen !!!
            // TODO: Es gäbe da auch einen LEGACY Parameter ... was immer der tut ?!?!

        }
        else {
            Log.v("PeLo", "OLD API");
            result = Html.fromHtml(html);
        }
        return result;
    }
}
