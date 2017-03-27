package com.mobilecartography.dresdenzooguide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AnimalsListActivity extends ListActivity{
    SQLiteDatabase database = null;
    Cursor dbCursor;
    DatabaseHelper dbHelper = new DatabaseHelper(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_list);
//      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
// 	I can not add "go back" button in action bar in ListActivity
        queryDataFromDatabase();
    }


	// query data from database
    public void queryDataFromDatabase() {
        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {
        }
        List<String> list_values = new ArrayList<>();
        try {
            database = dbHelper.getDataBase();
            dbCursor = database.rawQuery("SELECT Name FROM animals;", null);
            dbCursor.moveToFirst();
            int index = dbCursor.getColumnIndex("Name");
            while (!dbCursor.isAfterLast()) {
                String record = dbCursor.getString(index);
                list_values.add(record);
                dbCursor.moveToNext();
            }
        } finally {
            if (database != null) {
                dbHelper.close();
            }
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, list_values);
        setListAdapter(adapter);

        ListView lv = (ListView) findViewById(android.R.id.list);
		
		//set a Click listener Then when clicking each item, go to another activity
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                    if (adapter.getItem(position).equals("Elephant")) {
                        Intent intent = new Intent(AnimalsListActivity.this, InfoElephant.class);
                        startActivity(intent);
                    }
                    if (adapter.getItem(position).equals("African Lion")) {
                        Intent intent = new Intent(AnimalsListActivity.this, InfoLion.class);
                        startActivity(intent);
                    }
                    if (adapter.getItem(position).equals("Snow Owl")) {
                        Intent intent = new Intent(AnimalsListActivity.this, InfoOwl.class);
                        startActivity(intent);
                    }
                    if (adapter.getItem(position).equals("Giraffe")) {
                        Intent intent = new Intent(AnimalsListActivity.this, InfoGiraffe.class);
                        startActivity(intent);
                    }
                    if (adapter.getItem(position).equals("Carribean Flamingo")) {
                        Intent intent = new Intent(AnimalsListActivity.this, InfoFlamingo.class);
                        startActivity(intent);
                    }
                    if (adapter.getItem(position).equals("Orangutan")) {
                        Intent intent = new Intent(AnimalsListActivity.this, InfoOrangutan.class);
                        startActivity(intent);
                    }
            }
        });


    }


}