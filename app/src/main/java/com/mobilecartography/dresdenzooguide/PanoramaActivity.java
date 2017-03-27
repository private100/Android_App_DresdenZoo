package com.mobilecartography.dresdenzooguide;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PanoramaActivity extends AppCompatActivity{

    private LinearLayout mGallery;
    private int[] imgId;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);

        Button btn_effect= (Button) findViewById(R.id.button_panorama);
        btn_effect.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        mInflater = LayoutInflater.from(this);
		// store images id into a array
        imgId = new int[] { R.drawable.elephant, R.drawable.giraffe, R.drawable.lion,
                R.drawable.flamingo, R.drawable.orangutan, R.drawable.owl};
        initView();
    }

    private void initView() {
        mGallery = (LinearLayout) findViewById(R.id.gallery_id);
		//set up images in imageview
        for (int i = 0; i < imgId.length; i++) {
            View view = mInflater.inflate(R.layout.gallery, mGallery, false);
            ImageView img = (ImageView) view.findViewById(R.id.gallery_image);
            img.setImageResource(imgId[i]);
            mGallery.addView(view);
        }
    }

	//swith to introduction
    public void onClick_introduction(View view) {
        Intent intent = new Intent(this, IntroductionActivity.class);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

	//swith to mainmap
    public void onClick_mainmap(View view) {
        Intent intent = new Intent(this, MainMapActivity.class);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

	//swith to about
    public void onClick_about(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}
