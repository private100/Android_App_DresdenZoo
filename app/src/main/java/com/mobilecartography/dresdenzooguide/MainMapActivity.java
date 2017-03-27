package com.mobilecartography.dresdenzooguide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainMapActivity extends AppCompatActivity {

    GoogleMap map;
    // The map switching menu items.
    MenuItem handedmapMenuItem = null;
    MenuItem hybridmapMenuItem = null;
    MenuItem basemapMenuItem = null;

	
	//custom marker's infowindow
    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.custom_infowindow, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());
            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        Button btn_effect = (Button) findViewById(R.id.button_mainmap);
        btn_effect.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
		
		//set up google map as base map
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.0371601, 13.7543426), 16));

		//add handed map
        drawHandedMap();

		//redraw the infowindow
        map.setInfoWindowAdapter(new MyInfoWindowAdapter());



    }

	//draw handed map
    public void drawHandedMap() {
        LatLngBounds newarkBounds = new LatLngBounds(
                new LatLng(51.0342651, 13.7506361),       // South west corner
                new LatLng(51.0395067, 13.7592472));      // North east corner
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.map2))
                .positionFromBounds(newarkBounds);

        GroundOverlay imageOverlay = map.addGroundOverlay(newarkMap);
    }


	//realise adding animal markers
    public void queryDataAnimalFromDatabase() {
        SQLiteDatabase database = null;
        Cursor dbCursor_name;
        Cursor dbCursor_lat;
        Cursor dbCursor_lon;
        Cursor dbCursor_icon;
        Cursor dbCursor_info;
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {
        }
//        GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        List<String> list_values = new ArrayList<String>();
        List<Double> list_lat = new ArrayList<Double>();
        List<Double> list_lon = new ArrayList<Double>();
        List<String> list_icon = new ArrayList<>();
        List<String> list_info = new ArrayList<String>();

        try {
            database = dbHelper.getDataBase();
            dbCursor_name = database.rawQuery("SELECT Name FROM animals;", null);
            dbCursor_lat = database.rawQuery("SELECT Lat FROM animals;", null);
            dbCursor_lon = database.rawQuery("SELECT Lon FROM animals;", null);
            dbCursor_icon = database.rawQuery("SELECT Icon FROM animals;", null);
            dbCursor_info = database.rawQuery("SELECT Description FROM animals;", null);
            dbCursor_name.moveToFirst();
            dbCursor_lat.moveToFirst();
            dbCursor_lon.moveToFirst();
            dbCursor_icon.moveToFirst();
            dbCursor_info.moveToFirst();
            int index1 = dbCursor_name.getColumnIndex("Name");
            int index2 = dbCursor_lat.getColumnIndex("Lat");
            int index3 = dbCursor_lon.getColumnIndex("Lon");
            int index4 = dbCursor_icon.getColumnIndex("Icon");
            int index5 = dbCursor_info.getColumnIndex("Description");
            while (!dbCursor_name.isAfterLast()) {
                String record = dbCursor_name.getString(index1);
                list_values.add(record);
                dbCursor_name.moveToNext();

                Double lat = dbCursor_lat.getDouble(index2);
                list_lat.add(lat);
                dbCursor_lat.moveToNext();

                Double lon = dbCursor_lon.getDouble(index3);
                list_lon.add(lon);
                dbCursor_lon.moveToNext();

                final String icon = dbCursor_icon.getString(index4);
                list_icon.add(icon);
                final int iconID = getResources().getIdentifier(icon, "drawable", getPackageName());
                dbCursor_icon.moveToNext();

                final String info = dbCursor_info.getString(index5);
                list_info.add(info);
                dbCursor_info.moveToNext();


                Marker animal_marker = map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(iconID))
                        .position(new LatLng(lat, lon))
                        .anchor(0.5f, 0.5f).title(record)
                        .snippet(info));


                final SQLiteDatabase finalDatabase = database;
                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
//we try to only use one activity to show all window information, but there seems something wrong with the database query here
//                        if(marker.getTitle().equals("Elephant"))
//                        {
//                            Cursor tCursor = finalDatabase.rawQuery("SELECT Description FROM animals WHERE Name = Elephant;", null);
//                            int indext = tCursor.getColumnIndex("Description");
//                            String information= tCursor.getString(indext);
//                            Intent intent = new Intent(MainMapActivity.this, InfoActivity.class);
//                            intent.putExtra(getPackageName(),information);
//                            startActivity(intent);
//                        }


                        if(marker.getTitle().equals("Elephant")) {
                            Intent intent = new Intent(MainMapActivity.this, InfoElephant.class);
                            startActivity(intent);
                        }

                        if(marker.getTitle().equals("African Lion")) {
                            Intent intent = new Intent(MainMapActivity.this, InfoLion.class);
                            intent.putExtra(getPackageName(),icon);
                            startActivity(intent);
                        }
                        if(marker.getTitle().equals("Snow Owl")) {
                            Intent intent = new Intent(MainMapActivity.this, InfoOwl.class);
                            startActivity(intent);
                        }
                        if(marker.getTitle().equals("Giraffe")) {
                            Intent intent = new Intent(MainMapActivity.this, InfoGiraffe.class);
                            startActivity(intent);
                        }
                        if(marker.getTitle().equals("Carribean Flamingo")) {
                            Intent intent = new Intent(MainMapActivity.this, InfoFlamingo.class);
                            startActivity(intent);
                        }
                        if(marker.getTitle().equals("Orangutan")) {
                            Intent intent = new Intent(MainMapActivity.this, InfoOrangutan.class);
                            intent.putExtra(getPackageName(),info);
                            startActivity(intent);
                        }
                    }
                });

            }
        } finally {
            if (database != null) {
                dbHelper.close();
            }
        }
    }
	
	
	//realise adding restaurant markers
    public void queryDataRestaurantFromDatabase() {
        SQLiteDatabase database = null;
        Cursor dbCursor_lat;
        Cursor dbCursor_lon;
        Cursor dbCursor_icon;
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {
        }
        List<Double> list_lat = new ArrayList<Double>();
        List<Double> list_lon = new ArrayList<Double>();
        List<String> list_icon = new ArrayList<>();

        try {
            database = dbHelper.getDataBase();
            dbCursor_lat = database.rawQuery("SELECT Lat FROM restaurant;", null);
            dbCursor_lon = database.rawQuery("SELECT Lon FROM restaurant;", null);
            dbCursor_icon = database.rawQuery("SELECT Icon FROM restaurant;", null);
            dbCursor_lat.moveToFirst();
            dbCursor_lon.moveToFirst();
            dbCursor_icon.moveToFirst();
            int index1 = dbCursor_lat.getColumnIndex("Lat");
            int index2 = dbCursor_lon.getColumnIndex("Lon");
            int index3 = dbCursor_icon.getColumnIndex("Icon");
            while (!dbCursor_lat.isAfterLast()) {
                Double lat = dbCursor_lat.getDouble(index1);
                list_lat.add(lat);
                dbCursor_lat.moveToNext();

                Double lon = dbCursor_lon.getDouble(index2);
                list_lon.add(lon);
                dbCursor_lon.moveToNext();

                String icon = dbCursor_icon.getString(index3);
                list_icon.add(icon);
                int iconID = getResources().getIdentifier(icon, "drawable", getPackageName());
                dbCursor_icon.moveToNext();


                map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(iconID))
                        .position(new LatLng(lat, lon))
                        .anchor(0.5f, 0.5f).title("Restaurant"));
            }
        } finally {
            if (database != null) {
                dbHelper.close();
            }
        }
    }

	//add wc markers
    public void queryDataWCFromDatabase() {
        SQLiteDatabase database = null;
        Cursor dbCursor_lat;
        Cursor dbCursor_lon;
        Cursor dbCursor_icon;
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {
        }
        List<Double> list_lat = new ArrayList<Double>();
        List<Double> list_lon = new ArrayList<Double>();
        List<String> list_icon = new ArrayList<>();

        try {
            database = dbHelper.getDataBase();
            dbCursor_lat = database.rawQuery("SELECT Lat FROM wc;", null);
            dbCursor_lon = database.rawQuery("SELECT Lon FROM wc;", null);
            dbCursor_icon = database.rawQuery("SELECT Icon FROM wc;", null);
            dbCursor_lat.moveToFirst();
            dbCursor_lon.moveToFirst();
            dbCursor_icon.moveToFirst();
            int index1 = dbCursor_lat.getColumnIndex("Lat");
            int index2 = dbCursor_lon.getColumnIndex("Lon");
            int index3 = dbCursor_icon.getColumnIndex("Icon");
            while (!dbCursor_lat.isAfterLast()) {
                Double lat = dbCursor_lat.getDouble(index1);
                list_lat.add(lat);
                dbCursor_lat.moveToNext();

                Double lon = dbCursor_lon.getDouble(index2);
                list_lon.add(lon);
                dbCursor_lon.moveToNext();

                String icon = dbCursor_icon.getString(index3);
                list_icon.add(icon);
                int iconID = getResources().getIdentifier(icon, "drawable", getPackageName());
                dbCursor_icon.moveToNext();


                map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(iconID))
                        .position(new LatLng(lat, lon))
                        .anchor(0.5f, 0.5f).title("Toilet"));
            }
        } finally {
            if (database != null) {
                dbHelper.close();
            }
        }
    }


	
	//add menu in action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);

        // Get the map switching menu items.
        handedmapMenuItem = menu.getItem(0);
        hybridmapMenuItem = menu.getItem(1);
        basemapMenuItem = menu.getItem(2);
        basemapMenuItem.setChecked(true);
        return true;
    }

	//item of menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selection.
        switch (item.getItemId()) {
            case R.id.handedmap:
                drawHandedMap();
                return true;
            case R.id.hybridmap:
                map = ((SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map)).getMap();
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.basemap:
                map = ((SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map)).getMap();
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void onClick_animal(View view) {
        Button btn_animal = (Button) findViewById(R.id.button_animal);
        Button btn_restaurant = (Button) findViewById(R.id.button_restaurant);
        Button btn_wc = (Button) findViewById(R.id.button_wc);
        btn_animal.setTypeface(null, Typeface.BOLD);
        btn_animal.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn_restaurant.setTypeface(null, Typeface.NORMAL);
        btn_restaurant.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btn_wc.setTypeface(null, Typeface.NORMAL);
        btn_wc.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        map.clear();
        queryDataAnimalFromDatabase();
        drawHandedMap();
    }

    public void onClick_restaurant(View view) {
        Button btn_animal = (Button) findViewById(R.id.button_animal);
        Button btn_restaurant = (Button) findViewById(R.id.button_restaurant);
        Button btn_wc = (Button) findViewById(R.id.button_wc);
        btn_restaurant.setTypeface(null, Typeface.BOLD);
        btn_restaurant.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn_animal.setTypeface(null, Typeface.NORMAL);
        btn_animal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btn_wc.setTypeface(null, Typeface.NORMAL);
        btn_wc.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        map.clear();
        queryDataRestaurantFromDatabase();
        drawHandedMap();
    }

    public void onClick_wc(View view) {
        Button btn_animal = (Button) findViewById(R.id.button_animal);
        Button btn_wc = (Button) findViewById(R.id.button_wc);
        Button btn_restaurant = (Button) findViewById(R.id.button_restaurant);
        btn_wc.setTypeface(null, Typeface.BOLD);
        btn_wc.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn_animal.setTypeface(null, Typeface.NORMAL);
        btn_animal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btn_restaurant.setTypeface(null, Typeface.NORMAL);
        btn_restaurant.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        map.clear();
        queryDataWCFromDatabase();
        drawHandedMap();
    }

    public void onClick_animalslist(View view) {
        Intent intent = new Intent(this, AnimalsListActivity.class);
        startActivity(intent);
    }

	
	
	//navigation bar
    public void onClick_introduction(View view) {
        Intent intent = new Intent(this, IntroductionActivity.class);
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

    public void onClick_about(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

}
