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
 * The Model is responsible for the data. For validation of query string and for initing foursquare
 * venue search querying and getting the result.
 */
public class Model implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnQueryResultReadyCallbacks
{
    private Context ctx;
    private GoogleApiClient mGoogleApiClient;
    private Location lastLoc;
    private Thread currentThread;
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
        RequestQueue.getInstance().init(ctx);

        id = "";
        secret = "";
    }

    public void query4Square(String s)
    {
        if(lastLoc != null)
        {
            Query q = new Query(lastLoc, id, secret, ctx, s, this);
            if (currentThread != null)
                currentThread.interrupt();

            RequestQueue.getInstance().cancel();

            currentThread = new QueryThread(q);
            currentThread.start();
        }
    }

    public boolean isValidQuery(String s)
    {
        boolean valid = true;
        if(s.equals(""))
            return false;
        else
        {
            valid &= (s.charAt(0) != '\n');

            int i = 0;
            // testing for only spaces
            for (Character c : s.toCharArray())
            {
                if (c != ' ')
                    break;
                ++i;
            }
            valid &= (i != s.length());
            return valid;
        }
    }

    public void cleanup()
    {
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        if(currentThread != null)
            currentThread.interrupt();
        RequestQueue.getInstance().cancel();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        lastLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void OnQueryResultReady(List<Venue> venues)
    {
        VenueAdapter.getInstance().updateData(venues);
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
