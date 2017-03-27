package com.mobilecartography.dresdenzooguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class InfoOrangutan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_orangutan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//get "description" information from  mainmap_activity
        Intent intent = getIntent();
        String content = intent.getStringExtra(getPackageName());
        TextView text = (TextView) findViewById(R.id.text_info);
        text.setText(content);
    }

	// action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
