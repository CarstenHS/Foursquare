package csv.foursquare;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by der_geiler on 29-01-2016.
 */
public class Model implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private Context ctx;
    private GoogleApiClient mGoogleApiClient;
    private Location lastLoc;

    // TODO: REmove
    private String id;
    private String secret;


    public Model(Context c)
    {
        ctx = c;

        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

        /* Todo: Remove */
        SharedPreferences settings = ctx.getSharedPreferences("4square", 0);
        SharedPreferences.Editor editor = settings.edit();
        settings = ctx.getSharedPreferences("4square", 0);
        id = settings.getString("id", "");
        secret = settings.getString("secret", "");
    }

    public void query4Square(String s)
    {
        String lat = String.valueOf(lastLoc.getLatitude());
        String lon = String.valueOf(lastLoc.getLongitude());

        String url = "https://api.foursquare.com/v2/venues/search?";
        url += "ll=" + lat + "," + lon;
        url += "&client_id=" + id;
        url += "&client_secret=" + secret;
        url += "&v=20140806";
        url += "&query=" + s;

        RequestQueue queue = Volley.newRequestQueue(ctx);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    List<Venue> venues = new ArrayList<>();
                    JSONArray ja = response.getJSONObject("response").getJSONArray("venues");
                    String name;
                    String address;
                    int dist;
                    for(int i = 0; i < ja.length(); i++)
                    {
                        name = "";
                        address = "";
                        dist = -1;
                        JSONObject  jo = ja.getJSONObject(i);
                        try{name = jo.getString("name");}       catch (JSONException ignored){}
                        try{jo = jo.getJSONObject("location");} catch (JSONException ignored){}
                        try{address = jo.getString("address");} catch (JSONException ignored){}
                        try{dist = jo.getInt("distance");}      catch (JSONException ignored){}
                        venues.add(new Venue(name, address, dist));
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                int i = 0;
                i++;
            }
        });

        queue.add(jsObjRequest);
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        lastLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        query4Square("sushi");
    }

    @Override
    public void onConnectionSuspended(int i){}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){}

    public class Venue
    {
        String name;
        String address;
        int distance;

        public Venue(String n, String a, int d)
        {
            name = n;
            address = a;
            distance = d;
        }
        public String getName(){return name;}
        public String getAddress(){return address;}
        public int getDist(){return distance;}
    }
}
