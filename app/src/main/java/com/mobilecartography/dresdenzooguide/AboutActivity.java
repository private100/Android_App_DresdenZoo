package com.mobilecartography.dresdenzooguide;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

		//set effect of button when in corresponding activity
        Button btn_effect= (Button) findViewById(R.id.button_about);
        btn_effect.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }
	
	
	//navigation bar 
    public void onClick_introduction(View view) {
        Intent intent = new Intent(this, IntroductionActivity.class);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    public void onClick_mainmap(View view) {
        Intent intent = new Intent(this, MainMapActivity.class);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    public void onClick_panorama(View view) {
        Intent intent = new Intent(this, PanoramaActivity.class);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

}
