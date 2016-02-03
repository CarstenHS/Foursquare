package csv.foursquare;

import android.content.Context;

import java.util.List;

/**
 * The Presenter is responsible for getting the Search string from the view when the user changes
 * the search query string in the EditText.
 * Also responsible for setting visibility of the Listview of venues.
 */
public class Presenter
{
    private I_View view;
    private Model model;

    public Presenter(Object obj)
    {
        this.view = (I_View)obj;
        this.model = new Model((Context)obj);
    }

    public void searchStringChanged(String s)
    {
        if(model.isValidQuery(s))
        {
            view.setListviewVisibility(VenueAdapter.getInstance());
            model.query4Square(s);
        }
        else
            view.setListviewVisibility(null);
    }

    public void cleanup()
    {
        model.cleanup();
    }

}
