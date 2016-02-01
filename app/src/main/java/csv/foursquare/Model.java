package csv.foursquare;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import java.util.List;

/**
 * Created by der_geiler on 29-01-2016.
 */
public class Model implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnQueryResultReadyCallbacks
{
    private Context ctx;
    private GoogleApiClient mGoogleApiClient;
    private Location lastLoc;
    private Thread currentThread;
    private int queryCount = 0;
    private OnQueryResultReadyCallbacks listener;

    // TODO: REmove
    private String id;
    private String secret;

    public Model(Context c, OnQueryResultReadyCallbacks listener)
    {
        ctx = c;
        this.listener = listener;

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
        onConnectionFailed(ConnectionResult.zzadR);
    }

    public int getQueryCount(){return queryCount;}
    public void query4Square(String s)
    {
        Query q = new Query(lastLoc, id, secret, ctx, s, this);
        if(currentThread != null)
            currentThread.interrupt();

        currentThread = new QueryThread(q);
        currentThread.start();
        ++queryCount;
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        lastLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void OnQueryResultReady(List<Venue> venues)
    {
        --queryCount;
        VenueAdapter.getInstance().updateData(venues);
        listener.OnQueryResultReady();
    }
    @Override
    public void OnQueryResultReady(){}
    @Override
    public void onConnectionSuspended(int i){}
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Toast.makeText(ctx,"ERROR: Can't get a GPS fix!", Toast.LENGTH_SHORT).show();
    }
}
