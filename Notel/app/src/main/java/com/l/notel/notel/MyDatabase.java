package com.l.notel.notel;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;
import android.database.Cursor;

import java.io.File;

/**
 * Created by thekingtat on 26/7/15.
 */
public class MyDatabase extends ActionBarActivity {

    //Database
    SQLiteDatabase redpinDB = null;

    public void loadDatabase(){
        try{
            redpinDB = this.openOrCreateDatabase("redpinDB",
                    MODE_PRIVATE, null);
            redpinDB.execSQL("CREATE TABLE IF NOT EXISTS location " +
                    "(locationid integer primary key, " +
                    "symbolicid VARCHAR, " +
                    "mapId integer" +
                    "mapXCord integer," +
                    "mapYCord integer, " +
                    "accuracy integer);");

            File database =
                    getApplicationContext().getDatabasePath("redpinDB.db");
            if(!database.exists()){
                Toast.makeText(this, "redpin Database Created",
                        Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "redpin Database Missing",
                        Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.e("CONTACTS ERROR", "Error Creating redpin Database");
        }
    }
}
