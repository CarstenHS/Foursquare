package csv.foursquare;

/**
 * Created by der_geiler on 01-02-2016.
 */
public class Venue
{
    String name;
    String address;
    int distance;

    public Venue(String n, String a, int d)
    {
        name = n;
        address = a;
        distance = d;
    }
    public String getName(){return name;}
    public String getAddress(){return address;}
    public int getDist(){return distance;}
}