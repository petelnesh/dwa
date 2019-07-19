package com.dwakenya.techsavanna.newdwa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwakenya.techsavanna.newdwa.R.id;

public class FAQActivity extends AppCompatActivity {
    private int normal1=1;
    private int normal2=1;
    private int normal3=1;
    private int normal4=1;
    private int normal5=1;
    private int normal6=1;
    private int normal7=1;
    private int normal8=1;
    private int normal9=1;
    TextView Note1;
    LinearLayout Note2;
    TextView Note3;
    TextView Note4;
    TextView Note5;
    TextView Note6;
    TextView Note7;
    TextView Note8;
    TextView Note9;
    ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frequent);
    
        image1 = findViewById(id.image1);
        image2 = findViewById(id.image2);
        image3 = findViewById(id.image3);
        image4 = findViewById(id.image4);
        image5 = findViewById(id.image5);
        image6 = findViewById(id.image6);
        image7 = findViewById(id.image7);
        image8 = findViewById(id.image8);
        image9 = findViewById(id.image9);
        
        
        Note1 = findViewById(R.id.Note1);
        Note2 = findViewById(R.id.Note2);
        Note3 = findViewById(R.id.Note3);
        Note4 = findViewById(R.id.Note4);
        Note5 = findViewById(R.id.Note5);
        Note6 = findViewById(R.id.Note6);
        Note7 = findViewById(R.id.Note7);
        Note8 = findViewById(R.id.Note8);
        Note9 = findViewById(R.id.Note9);
        
    }
    
    public void onClick(View view){
        switch (view.getId()){
            case R.id.Quez1:
                if(normal1==1){
                    Note1.setVisibility(View.VISIBLE);
                    image1.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    
                    normal1++;
                }
                else {
                    normal1=1;
                    Note1.setVisibility(View.GONE);
                    image1.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
                
                break;
    
            case id.Quez2:
                if(normal2==1){
                    Note2.setVisibility(View.VISIBLE);
                    image2.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
//
//
                    normal2++;
                }
                else {
                    normal2=1;
                    Note2.setVisibility(View.GONE);
                    image2.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
        
                break;
            case R.id.Quez3:
                if(normal3==1){
                    Note3.setVisibility(View.VISIBLE);
                    image3.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            
                    normal3++;
                }
                else {
                    normal3=1;
                    Note3.setVisibility(View.GONE);
                    image3.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
        
                break;
    
            case R.id.Quez4:
                if(normal4==1){
                    Note4.setVisibility(View.VISIBLE);
                    image4.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            
                    normal4++;
                }
                else {
                    normal4=1;
                    Note4.setVisibility(View.GONE);
                    image4.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
        
                break;
    
            case R.id.Quez5:
                if(normal5==1){
                    Note5.setVisibility(View.VISIBLE);
                    image5.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            
                    normal5++;
                }
                else {
                    normal5=1;
                    Note5.setVisibility(View.GONE);
                    image5.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
        
                break;
    
            case R.id.Quez6:
                if(normal6==1){
                    Note6.setVisibility(View.VISIBLE);
                    image6.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            
                    normal6++;
                }
                else {
                    normal6=1;
                    Note6.setVisibility(View.GONE);
                    image6.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
        
                break;
    
            case R.id.Quez7:
                if(normal7==1){
                    Note7.setVisibility(View.VISIBLE);
                    image7.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            
                    normal7++;
                }
                else {
                    normal7=1;
                    Note7.setVisibility(View.GONE);
                    image7.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
        
                break;
    
            case R.id.Quez8:
                if(normal8==1){
                    Note8.setVisibility(View.VISIBLE);
                    image8.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            
                    normal8++;
                }
                else {
                    normal8=1;
                    Note8.setVisibility(View.GONE);
                    image8.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
        
                break;
    
            case R.id.Quez9:
                if(normal9==1){
                    Note9.setVisibility(View.VISIBLE);
                    image9.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            
                    normal9++;
                }
                else {
                    normal9=1;
                    Note9.setVisibility(View.GONE);
                    image9.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
        
                break;
        }
        
    }
}
