package csv.foursquare;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.location.Location;
import android.os.Bundle;

/*
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
*/
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

/**
 * Created by der_geiler on 29-01-2016.
 */
public class Model implements LoaderManager.LoaderCallbacks, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private LoaderManager loaderManager;
    private Context ctx;
    private GoogleApiClient mGoogleApiClient;
    private Location lastLoc;

    public Model(LoaderManager lm, Context c)
    {
        loaderManager = lm;
        loaderManager.initLoader(0, null, this);
        ctx = c;

        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    public void query4Square(String s)
    {
        String lat = String.valueOf(lastLoc.getLatitude());
        String lon = String.valueOf(lastLoc.getLongitude());

        String url = "https://api.foursquare.com/v2/venues/search?";
        url += "ll=" + lat + "," + lon;
        url += "&client_id=";
        url += "&client_secret=";
        url += "&v=20140806";
        url += "&query=" + s;

       // RequestQueue queue = Volley.newRequestQueue(ctx);

        /*
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        String result = response.toString();
                        result += "i";
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        int i = 0;
                        i++;
                    }
                });
                */
/*
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        String result = response.toString();
                        result += "i";
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                int i = 0;
                i++;
            }
        });

        queue.add(stringRequest);
        */
    }

    public void searchStringChanged(String s)
    {
        loaderManager.restartLoader(0, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args)
    {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data){}

    @Override
    public void onLoaderReset(Loader loader){}

    @Override
    public void onConnected(Bundle bundle)
    {
        lastLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i){}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){}
}
