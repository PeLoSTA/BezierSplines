package de.peterloos.beziersplines.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.peterloos.beziersplines.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_about);

        // prefer Action Bar Title with two lines
//        ActionBar actionBar = this.getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle(R.string.About);
//            //        actionBar.setSubtitle(this.getString(R.string.sub_title));
//            actionBar.setHomeButtonEnabled(true);
//        }
    }

}
