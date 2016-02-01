package csv.foursquare;

import android.content.Context;

import java.util.List;

/**
 * Created by der_geiler on 29-01-2016.
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
        model.query4Square(s);
    }

    /*
    @Override
    public void onConnected(Bundle bundle)
    {
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        int i = 0;
        i++;
    }
*/
}
