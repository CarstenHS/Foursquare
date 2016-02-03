package csv.foursquare;

import android.test.mock.MockContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class UnitTestQuery
{
    // Class under test
    private DummyModel dummyModel;
    private MockContext mockContext;

    public interface OnQueryResultReadyCallbacks
    {
        public void OnQueryResultReady(List<Venue> venues);
        public void OnQueryResultReady();
    }

    public class DummyModel implements OnQueryResultReadyCallbacks
    {
        private List<Venue> result = new ArrayList<Venue>();

        public DummyModel(){}

        public List<Venue> getResult() {
            return this.result;
        }

        @Override
        public void OnQueryResultReady(List<Venue> venues)
        {
            result = venues;
        }

        @Override
        public void OnQueryResultReady(){}
    }

    public static class Parser
    {
        public final static int RES_NOT_SET         = -10;
        public final static int RES_OK              = 0;
        public final static int RES_NAME            = -1;
        public final static int RES_LOC             = -2;
        public final static int RES_ADDRESS         = -3;
        public final static int RES_DIST            = -4;
        public final static int RES_SERVER_ERROR    = -5;
        public final static int RES_JSON            = -6;
        static private int result = RES_NOT_SET;

        public Parser(){}

        static public void parseResponse(JSONObject response, OnQueryResultReadyCallbacks callback)
        {
            final int OK = 200;
            List<Venue> venues = new ArrayList<>();
            try
            {
                if (response.getJSONObject("meta").getInt("code") == OK)
                {
                    JSONArray ja = response.getJSONObject("response").getJSONArray("venues");
                    String name;
                    String address;
                    int dist;
                    for (int i = 0; i < ja.length(); i++)
                    {
                        name = "";
                        address = "";
                        dist = -1;
                        JSONObject jo = ja.getJSONObject(i);
                        try
                        {
                            name = jo.getString("name");
                        } catch (JSONException ignored)
                        {
                            result = RES_NAME;
                        }
                        try
                        {
                            jo = jo.getJSONObject("location");
                        } catch (JSONException ignored)
                        {
                            result = RES_LOC;
                        }
                        try
                        {
                            address = jo.getString("address");
                        } catch (JSONException ignored)
                        {
                            result = RES_ADDRESS;
                        }
                        try
                        {
                            dist = jo.getInt("distance");
                        } catch (JSONException ignored)
                        {
                            result = RES_DIST;
                        }
                        venues.add(new Venue(name, address, dist));
                        result = RES_OK;
                    }
                    callback.OnQueryResultReady(venues);
                } else
                {
                    callback.OnQueryResultReady(null);
                }
            } catch (JSONException e)
            {
                callback.OnQueryResultReady(null);
            }
        }
    }

    private String readFile(String filename)
    {
        BufferedReader reader;
        StringBuilder sb = new StringBuilder();
        try
        {
            final InputStream file = this.getClass().getClassLoader().getResourceAsStream(filename);
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            while(line != null)
            {
                sb.append(line);
                line = reader.readLine();
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
        return sb.toString();
    }

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        mockContext = new MockContext();
        dummyModel = new DummyModel();
    }

    @Test
    public void testParsing()
    {
        String response = readFile("motherQuery.txt");
        response = response.replace("\"", "'");
        JSONObject jObj = null;
        int i = 0;
        try
        {
            jObj = new JSONObject(response);
            ++i;
        } catch (JSONException e)
        {
            e.printStackTrace();
            ++i;
        }

        Parser.parseResponse(jObj, new DummyModel());
        assertThat(dummyModel.getResult().isEmpty(), is(false));
    }

}