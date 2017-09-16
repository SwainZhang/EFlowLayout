package com.example.emery.eflowlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FLowLayout  flayout = (FLowLayout) findViewById(R.id.fl_tvs);
        flayout.setOnItemClickListener(new FLowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {

               Toast.makeText(MainActivity.this,((TextView)view).getText(),Toast.LENGTH_SHORT).show();

            }
        });

        FLowTextLayout txtlayout = (FLowTextLayout) findViewById(R.id.fl_text);
        String [] strings=new String[]{"大当时的","的是非得失","的撒风是","似懂非懂手动","的撒风都是"};
        txtlayout.setTextList(Arrays.asList(strings));
        txtlayout.setOnItemClickListener(new FLowTextLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Toast.makeText(MainActivity.this,((TextView)view).getText(),Toast.LENGTH_SHORT).show();

            }
        });

    }
}
