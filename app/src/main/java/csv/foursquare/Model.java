package csv.foursquare;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;

/**
 * Created by der_geiler on 29-01-2016.
 */
public class Model implements LoaderManager.LoaderCallbacks
{
    private LoaderManager loaderManager;
    private Context ctx;

    public Model(LoaderManager lm)
    {
        loaderManager = lm;
        loaderManager.initLoader(0, null, this);
    }

    public void query4Square(String s)
    {

    }

    public void searchStringChanged(String s)
    {
        loaderManager.restartLoader(0, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args)
    {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data)
    {

    }

    @Override
    public void onLoaderReset(Loader loader)
    {

    }
}
