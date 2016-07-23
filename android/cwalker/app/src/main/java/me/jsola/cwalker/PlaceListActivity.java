package me.jsola.cwalker;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceListActivity extends Activity {

    private List<Place> totalPlaces;
    private List<Place> selectedPlaces;
    Drawable background;
    Button goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        selectedPlaces = new ArrayList<Place>();

        Bundle extras = getIntent().getExtras();

        JSONObject result = null;
        try {
            result = new JSONObject(extras.getString("PlacesDetected"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONArray places = null;
        try {
            places = result.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final List<Place> placesDetected = new ArrayList<Place>();

        for(int i=0; i < places.length(); i++){
            try {
                placesDetected.add(new Place(places.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        totalPlaces = placesDetected;

        final ListView listview = (ListView) findViewById(R.id.list);

        final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, placesDetected);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final Place item = (Place) parent.getItemAtPosition(position);

                Log.d("ON CLICK", "Position: " + position);

                if(selectedPlaces.contains(item)){
                    selectedPlaces.remove(item);
                    view.setBackground(background);
                }else {
                    background = view.getBackground();
                    view.setBackgroundColor(R.color.colorAccent);
                    selectedPlaces.add(item);
                }

                if(selectedPlaces.size() > 0){
                    goButton.setVisibility(Button.VISIBLE);
                } else {
                    goButton.setVisibility(Button.GONE);
                }




            }

        });

        goButton = (Button) findViewById(R.id.go_button);
        goButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Place[] curplaces = new Place[selectedPlaces.size()];
                for(int i = 0; i < selectedPlaces.size(); ++i){
                    curplaces[i] = selectedPlaces.get(i);
                }

                new GetRoute().execute(curplaces);
            }
        });


    }

    private class GetRoute extends AsyncTask<Place, Void, String> {

        HttpURLConnection urlConnection = null;

        LocationManager myLocationManager;

        public String makeURL (Location current, String... vicinities){
            StringBuilder urlString = new StringBuilder();
            urlString.append("https://maps.googleapis.com/maps/api/directions/json");
            urlString.append("?origin=");// from
            urlString.append(current.getLatitude());
            urlString.append(",");
            urlString
                    .append(Double.toString( current.getLongitude()));
            urlString.append("&destination=");// to
            urlString.append(current.getLatitude());
            urlString.append(",");
            urlString
                    .append(Double.toString( current.getLongitude()));
            urlString.append("&waypoints=");
            urlString.append("optimize:true");
            for(int i = 0; i < vicinities.length; ++i){
                try {
                    urlString.append("|" + URLEncoder.encode(vicinities[i],"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            urlString.append("&sensor=false&mode=walking&alternatives=true");

                urlString.append("&key=AIzaSyA5MPiWRvthVJ8UTURj0eIm0s4eisFlD-s");

            return urlString.toString();
        }

        @Override
        protected  void onPreExecute(){
            myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        @Override
        protected String doInBackground(Place... params) {
            // params comes from the execute() call: params[0] is the url.
            try {

                String[] vicinities = new String[params.length];

                for(int i = 0; i < params.length; ++i){
                    vicinities[i] = params[i].getVicinity();
                }

                String locationProvider = LocationManager.NETWORK_PROVIDER;


                Location currentLocation = myLocationManager.getLastKnownLocation(locationProvider);

                Log.d("ROUTE QUERY",makeURL(currentLocation,vicinities));

                URL url = new URL(makeURL(currentLocation,vicinities));

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                Log.v("RESPONSE", urlConnection.getRequestMethod());
                urlConnection.connect();
                Log.v("TESPONSE", urlConnection.getResponseCode()+"");



            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
            return readInputStreamToString(urlConnection);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.v("Get Near Places", "Query executed");
            Log.v("Get Near Places", result);

            Intent myIntent = new Intent(PlaceListActivity.this, MapsActivity.class);
            myIntent.putExtra("path", result);
            PlaceListActivity.this.startActivity(myIntent);

        }
    }

    private String readInputStreamToString(HttpURLConnection connection) {
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        }
        catch (Exception e) {
            Log.i("LOLOL", "Error reading InputStream");
            result = null;
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                    Log.i("LOLOL", "Error closing InputStream");
                }
            }
        }

        return result;
    }


}
