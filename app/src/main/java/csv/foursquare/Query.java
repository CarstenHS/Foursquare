package csv.foursquare;

import android.content.Context;
import android.location.Location;

/**
 * The Query object is responsible for parsing relevant query data for querying the Foursquare
 * server.
 */
public class Query
{
    private Location lastLoc;
    private String id;
    private String secret;
    private Context ctx;
    private String query;
    private OnQueryResultReadyCallbacks listener;

    public Query(Location l, String id, String secret,
                 Context ctx, String queryStr,
                 OnQueryResultReadyCallbacks listener)
    {
        this.lastLoc = l;
        this.id = id;
        this.secret = secret;
        this.ctx = ctx;
        this.query = queryStr;
        this.listener = listener;
    }
    public Location getlocation(){return lastLoc;}
    public String getId(){return id;}
    public String getSecret(){return secret;}
    public Context getContext(){return ctx;}
    public String getQuery(){return query;}
    public OnQueryResultReadyCallbacks getListener(){return listener;}
}