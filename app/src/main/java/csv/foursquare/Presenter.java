package csv.foursquare;

import android.app.LoaderManager;
import android.view.View;

/**
 * Created by der_geiler on 29-01-2016.
 */
public class Presenter
{
    private I_View view;
    private Model model;

    public Presenter(I_View v, LoaderManager lm)
    {
        this.view = v;
        this.model = new Model(lm);
    }

    public void searchStringChanged(String s)
    {

    }
}
