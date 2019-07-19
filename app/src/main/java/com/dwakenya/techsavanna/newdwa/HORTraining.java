package com.dwakenya.techsavanna.newdwa;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class HORTraining extends AppCompatActivity {
    Toolbar succcess_toolbar;
    
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hortraining);
        succcess_toolbar = findViewById(R.id.success_toolbar);
        setSupportActionBar(succcess_toolbar);
        setTitle("Training Resources");
    }
    
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Video:
                startActivity(new Intent(HORTraining.this, Video.class));
                break;
            case R.id.Pdf:
                startActivity(new Intent(HORTraining.this, Pdf.class));
                break;
//            case R.id.Images:
//                startActivity(new Intent(HORTraining.this, ImageView.class));
//                break;
        }
    }
}
