package me.jsola.cwalker;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by j on 22/07/16.
 */
public class Place {


    private Location location;
    private String name;
    private String vicinity;
    private String photo;


    public Place (JSONObject placeJSON){

        location = new Location("random");
        try {
            location.setLatitude(placeJSON.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            location.setLongitude(placeJSON.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            name = placeJSON.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            vicinity = placeJSON.getString("vicinity");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
