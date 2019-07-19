package com.dwakenya.techsavanna.newdwa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HASTraining extends AppCompatActivity {
    Toolbar HASsuccess_toolbar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hastraining);
    
        HASsuccess_toolbar = findViewById(R.id.HASsuccess_toolbar);
        setSupportActionBar(HASsuccess_toolbar);
        setTitle("Training Resources");
    }
    
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Video:
                startActivity(new Intent(HASTraining.this, Video.class));
                break;
            case R.id.Pdf:
                startActivity(new Intent(HASTraining.this, PdfHAS.class));
                break;
//            case R.id.Images:
//                break;
        }
    }
}
