package csv.foursquare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * The Venueadaptor is responsible for displaying the listView items
 */
public class VenueAdapter extends BaseAdapter
{
    static private VenueAdapter instance = new VenueAdapter();
    private VenueAdapter(){}

    public static VenueAdapter getInstance(){return instance;}

    private Context ctx;
    private List<Venue> data;

    public void init(Context ctx)
    {
        this.ctx = ctx;
    }
    public void updateData(List<Venue> data)
    {
        if(data != null)
        {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount(){return data != null ? data.size() : 0;}

    @Override
    public Object getItem(int position)
    {
        return (data != null) ? data.get(position) : null;
    }

    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if(data.size() != 0)
        {
            if (convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.venue_row, null);
            }
            TextView tvVenue = (TextView) view.findViewById(R.id.venue); // title
            TextView tvAddress = (TextView) view.findViewById(R.id.address); // artist name
            TextView tvDist = (TextView) view.findViewById(R.id.distance); // duration

            // Setting all values in listview
            tvVenue.setText(data.get(position).getName());
            tvAddress.setText(data.get(position).getAddress());
            tvDist.setText(String.valueOf(data.get(position).getDist()) + "m");
        }
        return view;
    }
}
