package csv.foursquare;

import android.location.Location;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by der_geiler on 01-02-2016.
 */
public class QueryThread extends Thread
{
    private Query query;
    private static final int OK = 200;

    public QueryThread(Query q){query = q;}
    @Override
    public void run()
    {
        Location l = query.getlocation();
        String lat = String.valueOf(l.getLatitude());
        String lon = String.valueOf(l.getLongitude());

        String url = "https://api.foursquare.com/v2/venues/search?";
        url += "ll=" + lat + "," + lon;
        url += "&client_id=" + query.getId();
        url += "&client_secret=" + query.getSecret();
        url += "&v=20140806";

        String qStr;
        try     {qStr = URLEncoder.encode(query.getQuery(), "UTF-8");}
        catch   (UnsupportedEncodingException uee){qStr = query.getQuery();}

        url += "&query=" + qStr;

        final List<Venue> venues = new ArrayList<>();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    if((response != null) && (response.getJSONObject("meta").getInt("code") == OK))
                    {
                        JSONArray ja = response.getJSONObject("response").getJSONArray("venues");
                        String name;
                        String address;
                        int dist;
                        for (int i = 0; i < ja.length(); i++)
                        {
                            name = "";
                            address = "";
                            dist = -1;
                            JSONObject jo = ja.getJSONObject(i);
                            try{name = jo.getString("name");}
                            catch (JSONException ignored){}

                            try{jo = jo.getJSONObject("location");}
                            catch (JSONException ignored){}

                            try{address = jo.getString("address");}
                            catch (JSONException ignored){}

                            try{dist = jo.getInt("distance");}
                            catch (JSONException ignored){}

                            venues.add(new Venue(name, address, dist));
                        }
                        query.getListener().OnQueryResultReady(venues);
                    }
                    else
                    {
                        Toast.makeText(query.getContext(), "ERROR: Wrong response from server!", Toast.LENGTH_SHORT).show();
                        query.getListener().OnQueryResultReady(null);
                    }
                }
                catch (JSONException e)
                {
                    Toast.makeText(query.getContext(), "ERROR: Wrong response from server!", Toast.LENGTH_SHORT).show();
                    query.getListener().OnQueryResultReady(null);
                }
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(query.getContext(), "ERROR: Can't connect to the server!", Toast.LENGTH_SHORT).show();
                query.getListener().OnQueryResultReady(null);
            }
        });
        csv.foursquare.RequestQueue.getInstance().add(jsObjRequest);
    }
}
