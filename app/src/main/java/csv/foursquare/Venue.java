package csv.foursquare;

/**
 * The Venue class is data holder for venue information for encapsulation and easy access when
 * updating the VenueAdapter.
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