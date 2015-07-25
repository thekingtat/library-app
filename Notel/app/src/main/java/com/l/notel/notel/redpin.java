package com.l.notel.notel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;


public class redpin extends ActionBarActivity {

    //Database
    SQLiteDatabase redpinDB = null;
    Button createDBButton, addLocationButton, deleteLocationButton,
            getLocationsButton, deleteDBButton;
    EditText locationIdEditText, symbolicIdEditText, mapIdEditText,
        mapXCordEditText, mapYCordEditText, accuracyEditText, locationListEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redpin);

        //Database
        createDBButton = (Button) findViewById(R.id.createDBButton);
        addLocationButton = (Button) findViewById(R.id.addLocationButton);
        deleteLocationButton = (Button) findViewById(R.id.deleteLocationButton);
        getLocationsButton = (Button) findViewById(R.id.getLocationsButton);
        deleteDBButton = (Button) findViewById(R.id.deleteDBButton);
        locationIdEditText = (EditText) findViewById(R.id.locationIdEditText);
        symbolicIdEditText = (EditText) findViewById(R.id.symbolicIdEditText);
        mapIdEditText = (EditText) findViewById(R.id.mapIdEditText);
        mapXCordEditText = (EditText) findViewById(R.id.mapXCordEditText);
        mapYCordEditText = (EditText) findViewById(R.id.mapYCordEditText);
        accuracyEditText = (EditText) findViewById(R.id.accuracyEditText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_redpin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createDatabase(View view){
        try{
            redpinDB = this.openOrCreateDatabase("redpin",
                    MODE_PRIVATE, null);
            redpinDB.execSQL("CREATE TABLE IF NOT EXISTS location " +
                    "(locationid integer primary key, " +
                    "symbolicid VARCHAR, " +
                    "mapId integer" +
                    "mapXCord integer," +
                    "mapYCord integer, " +
                    "accuracy integer);");

            //Populate manually
            String locationId = locationIdEditText.getText().toString();
            String symbolicId = symbolicIdEditText.getText().toString();
            String mapId = mapIdEditText.getText().toString();
            String mapXCord = mapXCordEditText.getText().toString();
            String mapYCord = mapYCordEditText.getText().toString();
            String accuracy = accuracyEditText.getText().toString();
            redpinDB.execSQL("INSERT INTO location (locationId, symbolicId" +
                    "mapId, mapXCord, mapYCord, accuracy) VALUES " +
                            "(9, o108, 3, 495, 225, 0)," +
                            "(13, br, 3, 607, 291, 0)," +
                            "(16, tr, 3, 609, 125)," +
                            "(17, tl, 3, 458, 132);");

            File database =
                    getApplicationContext().getDatabasePath("redpin.db");
            if(!database.exists()){
                Toast.makeText(this, "Database Created",
                        Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Database Missing",
                        Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.e("LOCATIONS ERROR", "Error Creating Database");
        }
        addLocationButton.setClickable(true);
        deleteDBButton.setClickable(true);
        getLocationsButton.setClickable(true);
        deleteDBButton.setClickable(true);
    }
    public void addLocation(View view){
        String locationId = locationIdEditText.getText().toString();
        String symbolicId = symbolicIdEditText.getText().toString();
        String mapId = mapIdEditText.getText().toString();
        String mapXCord = mapXCordEditText.getText().toString();
        String mapYCord = mapYCordEditText.getText().toString();
        String accuracy = accuracyEditText.getText().toString();
        redpinDB.execSQL("INSERT INTO location (locationId, symbolicId" +
                "mapId, mapXCord, mapYCord, accuracy) VALUES('" +
                locationId + "', '" + symbolicId + "', '" + mapId + "', '"
                + mapXCord + "', '" + mapYCord + "', '"+ accuracy + "');");
    }

    public void getLocations(View view){
        Cursor cursor = redpinDB.rawQuery("SELECT * FROM location", null);
        int locationIdColumn = cursor.getColumnIndex("locationId");
        int symbolicIdColumn = cursor.getColumnIndex("symbolicId");
        int mapIdColumn = cursor.getColumnIndex("mapId");
        int mapXCordColumn = cursor.getColumnIndex("mapXCord");
        int mapYCordColumn = cursor.getColumnIndex("mapYCord");
        int accuracyColumn = cursor.getColumnIndex("accuracy");

        cursor.moveToFirst();
        String locationList = "";

        if(cursor != null && (cursor.getCount() > 0)){
            do{
                String locationId = cursor.getString(locationIdColumn);
                String symbolicId = cursor.getString(symbolicIdColumn);
                String mapId = cursor.getString(mapIdColumn);
                String mapXCord = cursor.getString(mapXCordColumn);
                String mapYCord = cursor.getString(mapYCordColumn);
                String accuracy = cursor.getString(accuracyColumn);

                locationList = locationList + locationId + " : " + symbolicId +
                        " : " + mapId + " : " + mapXCord + " : " + mapYCord +
                        " : " + accuracy + "\n";
            }while(cursor.moveToNext());

            locationListEditText.setText(locationList);
        }else{
            Toast.makeText(this, "No Results to Show",
                    Toast.LENGTH_SHORT).show();
            locationListEditText.setText("");
        }
    }

    public void deleteLocation(View view){
        String id = locationIdEditText.getText().toString();
        redpinDB.execSQL("DELETE FROM location WHERE id = "+
                id + ";");
    }

    public void deleteDatabase(View view){
        this.deleteDatabase("location");
    }

    @Override
    protected void onDestroy() {
        redpinDB.close();
        super.onDestroy();
        //pause, resume?
    }
}
