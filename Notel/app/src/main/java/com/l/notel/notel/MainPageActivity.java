package com.l.notel.notel;

import com.l.notel.notel.org.redpin.android.ApplicationContext;
import com.l.notel.notel.org.redpin.android.core.Fingerprint;
import com.l.notel.notel.org.redpin.android.core.Location;
import com.l.notel.notel.org.redpin.android.core.Map;
import com.l.notel.notel.org.redpin.android.core.Measurement;
import com.l.notel.notel.org.redpin.android.core.Vector;
import com.l.notel.notel.org.redpin.android.core.measure.WiFiReading;
import com.l.notel.notel.org.redpin.android.net.InternetConnectionManager;
import com.l.notel.notel.org.redpin.android.net.Response;
import com.l.notel.notel.org.redpin.android.net.SynchronizationManager;
import com.l.notel.notel.org.redpin.android.net.home.FingerprintRemoteHome;
import com.l.notel.notel.org.redpin.android.net.home.LocationRemoteHome;
import com.l.notel.notel.org.redpin.android.net.home.RemoteEntityHomeCallback;
import com.l.notel.notel.org.redpin.android.net.wifi.WifiSniffer;
import com.l.notel.notel.org.redpin.android.ui.list.MainListActivity;
import com.l.notel.notel.org.redpin.android.ui.list.SearchListActivity;
import com.l.notel.notel.org.redpin.android.ui.mapview.MapView;
import com.l.notel.notel.org.redpin.android.util.ExceptionReporter;

import android.app.AlertDialog;
import android.widget.LinearLayout;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.widget.TextView;
import android.widget.Button;

import com.l.notel.notel.org.redpin.android.ui.MapViewActivity;


public class MainPageActivity extends ActionBarActivity {
    private static final String TAG = MainPageActivity.class.getSimpleName();
    MapView mapView;
    ImageButton locateButton;
    ImageButton addLocationButton;
    TextView mapName;
    ProgressDialog progressDialog;

    WifiSniffer mWifiService;
    Location mLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Init for background services of Redpin
        ApplicationContext.init(getApplicationContext());
        ExceptionReporter.register(this);

        registerReceiver(connectionChangeReceiver, new IntentFilter(
                InternetConnectionManager.CONNECTIVITY_ACTION));
        startService(new Intent(MainPageActivity.this,
                SynchronizationManager.class));
        bindService(new Intent(this, InternetConnectionManager.class),
                mICMConnection, Context.BIND_AUTO_CREATE);

        startService(new Intent(MainPageActivity.this,InternetConnectionManager.class));


        startWifiSniffer();

        //end background service init
        setContentView(R.layout.activity_main_page);

        //final TextView firstTextView = (TextView) findViewById(R.id.textView);
//Horizontal scroll for user profile
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_in_userprofile_hsv);
        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), R.drawable.chiopic));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
        }


    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {

        unregisterReceiver(connectionChangeReceiver);

        stopWifiSniffer();

        stopService(new Intent(MainPageActivity.this,
                SynchronizationManager.class));

        stopService(new Intent(MainPageActivity.this,InternetConnectionManager.class));

        unbindService(mICMConnection);

        super.onDestroy();
    }


    /**
     * Initiates a scan for a new measurement, creates a location and afterwards displays it on the map
     */
    private void addNewLocation() {

        Map currentMap = mapView.getCurrentMap();

        if (currentMap == null) {
            new AlertDialog.Builder(this).setPositiveButton(
                    android.R.string.ok, null)
                    .setTitle(R.string.map_view_title).setMessage(
                    R.string.map_view_no_map_selected).create().show();
            Log.w(TAG, "addNewLocation: no current map shown");
            return;
        }

        progressDialog.show();

        Location location = new Location();

        location.setMap(currentMap);

        firstMeasurement = true;
        mLocation = location;
        mWifiService.forceMeasurement();

    }

    //show location: need to edit
    protected void showLocation(Location loc) {
        if (loc == null)
            return;

        Map m = (Map) loc.getMap();

        if (m != null) {
            mapName.setText(m.getMapName());
        }

        mapView.showLocation(loc, true);
    }
    /**
     * Locates the client
     */
    private void locate() {
        progressDialog.show();

        mLocation = null;
        mWifiService.forceMeasurement();

    }

    /**
     * Sets the connectivity mode of the view
     *
     * @param isOnline <code>True</code> if the client can connect to the server, <code>false</code> otherwise
     */
    private void setOnlineMode(boolean isOnline) {
        //mapView.setModifiable(isOnline);
        //locateButton.setEnabled(isOnline);
        //addLocationButton.setEnabled(isOnline);

    }

    /**
     * Starts the sniffer and registers the receiver
     */
    private void startWifiSniffer() {
        bindService(new Intent(this, WifiSniffer.class), mWifiConnection,
                Context.BIND_AUTO_CREATE);
        registerReceiver(wifiReceiver,
                new IntentFilter(WifiSniffer.WIFI_ACTION));
        Log.i(TAG, "Started WifiSniffer");
    }

    /**
     * Stops the sniffer and unregisters the receiver
     */
    private void stopWifiSniffer() {
        if (mWifiService != null) {
            mWifiService.stopMeasuring();
        }
        unbindService(mWifiConnection);
        unregisterReceiver(wifiReceiver);
        Log.i(TAG, "Stopped WifiSniffer");
    }

    /**
     * {@link InternetConnectionManager} {@link ServiceConnection} to check
     * current online state
     */
    private ServiceConnection mICMConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            InternetConnectionManager mManager = ((InternetConnectionManager.LocalBinder) service)
                    .getService();
            setOnlineMode(mManager.isOnline());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

    };

    /**
     * Receives notifications about connectivity changes
     */
    private BroadcastReceiver connectionChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            setOnlineMode((intent.getFlags() & InternetConnectionManager.ONLINE_FLAG)== InternetConnectionManager.ONLINE_FLAG);
        }

    };

    private boolean firstMeasurement = false;
    /**
     * Receives notifications about new available measurements
     */
    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Measurement m = mWifiService.retrieveLastMeasurement();

            if (m == null)
                return;

            if (mLocation != null) {
                // Interval Scanning
                Fingerprint fp = new Fingerprint(mLocation, m);
                FingerprintRemoteHome.setFingerprint(fp,
                        new RemoteEntityHomeCallback() {

                            @Override
                            public void onResponse(Response<?> response) {

                                if (firstMeasurement) {
                                    progressDialog.hide();
                                    mapView.addNewLocation(mLocation);
                                    firstMeasurement = false;
                                }

                                Log
                                        .i(TAG,
                                                "addNewLocation: setFingerprint successfull");
                            }

                            @Override
                            public void onFailure(Response<?> response) {
                                progressDialog.hide();
                                Log.i(TAG,
                                        "addNewLocation: setFingerprint failed: "
                                                + response.getStatus() + ", "
                                                + response.getMessage());
                            }
                        });

            } else {
                // Localization
                LocationRemoteHome.getLocation(m,
                        new RemoteEntityHomeCallback() {

                            @Override
                            public void onFailure(Response<?> response) {
                                progressDialog.hide();

                                new AlertDialog.Builder(MainPageActivity.this).setMessage(response.getMessage()).setPositiveButton(android.R.string.ok, null).create().show();

                            }

                            @Override
                            public void onResponse(Response<?> response) {
                                progressDialog.hide();
                                Location l = (Location) response.getData();
                                showLocation(l);

                            }

                        });
                mWifiService.stopMeasuring();
            }

        }
    };
    /**
     * {@link ServiceConnection} for the {@link WifiSniffer}
     */
    private ServiceConnection mWifiConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mWifiService = ((WifiSniffer.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mWifiService = null;
        }

    };
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
        if (id == R.id.action_redpin_makemap) {
            Intent goToRedPinIntent = new Intent (this, MapViewActivity.class);
            startActivity(goToRedPinIntent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
