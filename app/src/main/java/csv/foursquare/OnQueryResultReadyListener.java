package csv.foursquare;

import java.util.List;

/**
 * Created by der_geiler on 01-02-2016.
 */
public interface OnQueryResultReadyListener
{
    public void OnQueryResultReady(List<Venue> venues);
}
