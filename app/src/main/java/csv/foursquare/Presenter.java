package csv.foursquare;

import android.content.Context;

import java.util.List;

/**
 * Created by der_geiler on 29-01-2016.
 */
public class Presenter implements OnQueryResultReadyCallbacks
{
    private I_View view;
    private Model model;
    private static final String strSearch       = "Search: ";
    private static final String strSearching    = "Searching.. ";

    public Presenter(Object obj)
    {
        this.view = (I_View)obj;
        this.model = new Model((Context)obj, this);
    }

    public void searchStringChanged(String s)
    {
        if(s.equals("") == false)
        {
            view.setListviewVisibility(VenueAdapter.getInstance());
            model.query4Square(s);
            view.setSearchLabel(strSearching);
        }
        else
            view.setListviewVisibility(null);
    }

    @Override
    public void OnQueryResultReady()
    {
        if(model.getQueryCount() == 0)
            view.setSearchLabel(strSearch);
    }
    @Override
    public void OnQueryResultReady(List<Venue> venues){}
}
