package csv.foursquare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by der_geiler on 31-01-2016.
 */
public class VenueAdapter extends BaseAdapter
{
    private Context ctx;
    public VenueAdapter(Context ctx)
    {
        this.ctx = ctx;
    }

    @Override
    public int getCount()
    {
        return 5;
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.venue_row, null);
        }
        TextView tvVenue = (TextView)view.findViewById(R.id.venue); // title
        TextView tvAddress = (TextView)view.findViewById(R.id.address); // artist name
        TextView tvDist = (TextView)view.findViewById(R.id.distance); // duration

        // Setting all values in listview
        tvVenue.setText("Peters pølsevogn");
        tvAddress.setText("Valby Langgaed 777");
        tvDist.setText("512m");

        return view;
    }
}
