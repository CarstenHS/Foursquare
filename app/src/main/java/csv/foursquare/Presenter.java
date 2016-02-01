package csv.foursquare;

import android.app.LoaderManager;
import android.content.Context;

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
        //model.query4Square("sushi");
    }

    public void searchStringChanged(String s)
    {

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
