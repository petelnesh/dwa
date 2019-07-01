package com.dwakenya.techsavanna.newdwa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HOSActivity extends AppCompatActivity {
private CardView laundrycard,housekeepingcard,childcarecard,homenurcingcard,carteringcard,gardeningcard,hosghwcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Home Owner Service");
       // myToolbar.setTitleTextColor(Color.WHITE);

        laundrycard=findViewById(R.id.laundrycard);
        housekeepingcard=findViewById(R.id.housekeepingcard);
        homenurcingcard=findViewById(R.id.homenurcingcard);
        childcarecard=findViewById(R.id.childcarecard);
        carteringcard=findViewById(R.id.carteringcard);
        gardeningcard=findViewById(R.id.gardeningcard);
        hosghwcard=findViewById(R.id.hosghwcard);


        laundrycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HOSActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        housekeepingcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HOSActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        homenurcingcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HOSActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        childcarecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HOSActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        carteringcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HOSActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        gardeningcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HOSActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        hosghwcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HOSActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
