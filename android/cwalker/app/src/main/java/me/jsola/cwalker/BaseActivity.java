package me.jsola.cwalker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class BaseActivity extends FragmentActivity implements OnMapReadyCallback {


    private final float MIN_RADIUS = 500;
    private GoogleMap mMap;
    private LocationManager myLocationManager;
    private LocationListener myLocationListener;
    private Location myLocation;
    private Marker myLocationMarker;
    private Circle myCircle;
    private Float myRadius ;


    private SeekBar radiusSelector;
    private TextView radiusDisplayer;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        radiusSelector = (SeekBar) findViewById(R.id.radius_selector);
        radiusDisplayer = (TextView) findViewById(R.id.radius_value);

        setRadiusDisplay(radiusSelector.getProgress());

        radiusSelector.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progresValue, boolean fromUser) {
                        setRadiusDisplay(progresValue);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        updateCircleRadius();
                    }
                });


    }


    public void setRadiusDisplay(int progress){
        radiusDisplayer.setText((int)(radiusSelector.getProgress() + MIN_RADIUS) + " m");
    }

    public void updateCircleRadius(){
        myCircle.setRadius((double)(radiusSelector.getProgress() + MIN_RADIUS));
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        startLocationTracking();
/*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    public void startLocationTracking() {

        myLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        setUpListener();
        setUpInitialLocation();

    }

    public void setUpListener(){
        // Define a listener that responds to location updates
        myLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                setNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
        Log.d("Location Tracking","Listener started");
    }

    public void setUpInitialLocation(){
        String locationProvider = LocationManager.NETWORK_PROVIDER;


        Location InitialLocation = myLocationManager.getLastKnownLocation(locationProvider);

        setUpInitialMarker(InitialLocation);
    }

    public void setUpInitialMarker(Location InitiaLocation){

        LatLng myLaLn = new LatLng(InitiaLocation.getLatitude(),
                InitiaLocation.getLongitude());

        MarkerOptions markerOpts = new MarkerOptions().position(myLaLn).title(
                "my Location");
        myLocationMarker = mMap.addMarker(markerOpts);

        myCircle = mMap.addCircle(new CircleOptions()
                .center(myLaLn)
                .radius(myRadius)
                .fillColor(Color.argb(35, 0, 50, 240))
                .strokeColor(Color.argb(80, 0, 50, 240))
                .strokeWidth(2)
                .zIndex(1));

        CameraPosition camPos = new CameraPosition.Builder().target(myLaLn)
                .zoom(14).bearing(0).tilt(0).build();

        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);


    }

    public void setNewLocation(Location CurrentLocation) {
        LatLng newLatLng = new LatLng(CurrentLocation.getLatitude(),
                CurrentLocation.getLongitude());

        updateMarker(newLatLng);
        updateCircle(newLatLng);
        updateCamera(newLatLng);

        Log.d("Location Tracking", "New Location\tLatitude: "+CurrentLocation.getLatitude() + "\tLongitude: "+CurrentLocation.getLongitude());

    }

    public void updateMarker(LatLng newLatLng){
        myLocationMarker.setPosition(newLatLng);
    }

    public void updateCircle(LatLng newLatLng){
        myCircle.setCenter(newLatLng);
    }

    public void updateCamera(LatLng newLatLng){
        CameraPosition camPos = new CameraPosition.Builder().target(newLatLng)
                .zoom(mMap.getCameraPosition().zoom).bearing(0).tilt(0).build();

        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Base Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://me.jsola.cwalker/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);

        myRadius = Float.valueOf(1000);
    }

    @Override
    public void onStop() {
        super.onStop();

        myLocationManager.removeUpdates(myLocationListener);



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Base Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://me.jsola.cwalker/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
