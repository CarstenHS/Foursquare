package csv.foursquare;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

/**
 * Created by der_geiler on 29-01-2016.
 */
public class Model implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnQueryResultReadyListener
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

        VenueAdapter.getInstance().init(ctx);

        /* Todo: Remove */
        SharedPreferences settings = ctx.getSharedPreferences("4square", 0);
        settings = ctx.getSharedPreferences("4square", 0);
        id = settings.getString("id", "");
        secret = settings.getString("secret", "");
    }

    public void query4Square(String s)
    {
        Query q = new Query(lastLoc, id, secret, ctx, s, this);
        new QueryThread(q).start();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        lastLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(lastLoc != null)
            query4Square("sushi");
    }

    @Override
    public void onConnectionSuspended(int i){}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){}

    @Override
    public void OnQueryResultReady(List<Venue> venues)
    {
        Log.d("CSV", "OnQueryResultReady - venues: " + String.valueOf(venues.size()));
        VenueAdapter.getInstance().updateData(venues);
    }
}
