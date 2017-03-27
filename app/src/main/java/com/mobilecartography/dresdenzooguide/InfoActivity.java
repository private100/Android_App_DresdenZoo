package com.mobilecartography.dresdenzooguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        String text = intent.getStringExtra(getPackageName());
        TextView editText = (TextView) findViewById(R.id.text_info);
        editText.setText(text);
    }
}
