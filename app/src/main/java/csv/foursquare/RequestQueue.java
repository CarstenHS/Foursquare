package csv.foursquare;

import android.content.Context;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

/**
 * The request queue is a one point of entry reference for the HTTP volley request queue.
 */
public class RequestQueue
{
    private static RequestQueue ourInstance = new RequestQueue();
    private Context ctx;
    private com.android.volley.RequestQueue queue;
    public static RequestQueue getInstance(){return ourInstance;}
    private RequestQueue(){}

    public void init(Context ctx)
    {
        this.ctx = ctx;
        queue = Volley.newRequestQueue(ctx);
    }
    public void add(JsonObjectRequest obj)
    {
        obj.setTag(ctx);
        queue.add(obj);
    }
    public void cancel(){queue.cancelAll(ctx);}
}
