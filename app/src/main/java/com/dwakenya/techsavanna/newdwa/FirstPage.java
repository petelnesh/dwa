package com.dwakenya.techsavanna.newdwa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dwakenya.techsavanna.newdwa.helpers.SessionManager;

import java.util.HashMap;

public class FirstPage extends AppCompatActivity {
CardView horcv,hoscv,eoscv;
Button btnlogin,btnsign;
    TextView eorname;
    private SharedPreferences permissionStatus;
    // Session Manager Class
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Home Page");
      //  myToolbar.setTitleTextColor(Color.);
        horcv=findViewById(R.id.hor);
        hoscv=findViewById(R.id.hos);
        eorname=findViewById(R.id.eorname);

        eoscv=findViewById(R.id.eos);

        btnlogin=findViewById(R.id.login);
        btnsign=findViewById(R.id.signin);

        // Session Manager
        session = new SessionManager(getApplicationContext());
eorname.setText("Home Assistants' Services");

        if (session.isLoggedIn()) {
            HashMap<String, String > user = session.getUserDetails();
        }

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirstPage.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirstPage.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        eoscv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirstPage.this,HASActivity.class);
                startActivity(intent);
            }
        });
        horcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirstPage.this,HORActivity.class);
                startActivity(intent);
            }
        });
       hoscv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirstPage.this,HOSActivity.class);
                startActivity(intent);
            }
        });
    }
}
