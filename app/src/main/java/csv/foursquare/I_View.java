package csv.foursquare;

import android.widget.BaseAdapter;

/**
 * I_View for loose coupling to the Activity / view.
 */
public interface I_View
{
    void setListviewVisibility(VenueAdapter adapter);
}
