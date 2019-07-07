package com.dwakenya.techsavanna.newdwa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HORActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hor);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Home Owner Resources");
       // myToolbar.setTitleTextColor(Color.WHITE);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.hos) {
            startActivity(new Intent(HORActivity.this, FeedBackActivity.class));
        }
    }
}
