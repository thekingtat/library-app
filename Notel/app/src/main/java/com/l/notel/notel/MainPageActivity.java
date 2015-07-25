package com.l.notel.notel;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;


public class MainPageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        final TextView firstTextView = (TextView) findViewById(R.id.textView);

        Button button = (Button) findViewById(R.id.buttonInbox);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                firstTextView.setText("You clicked Inbox");
            }
        });

        button = (Button) findViewById(R.id.buttonOutbox);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                firstTextView.setText("You clicked Outbox");
            }
        });

        button = (Button) findViewById(R.id.buttonProfile);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                firstTextView.setText("You clicked Profile");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
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


}
