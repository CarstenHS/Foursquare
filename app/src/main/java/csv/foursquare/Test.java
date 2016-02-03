package csv.foursquare;

import android.content.Context;
import android.test.mock.MockContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by der_geiler on 03-02-2016.
 */
public class Test
{
    private DummyModel dummyModel;
    private Context ctx;
    private static String testResult = "";

    public Test(Context ctx)
    {
        this.ctx = ctx;
        TestParser();
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
        public final static int RES_SERVER_ERROR    = -5;
        public final static int RES_JSON            = -6;
        static private int result = RES_NOT_SET;

        public Parser(){}
        public static int getResult(){return result;}

        static public void parseResponse(JSONObject response, OnQueryResultReadyCallbacks callback)
        {
            final int OK = 200;
            List<Venue> venues = new ArrayList<>();
            try
            {
                if ((response != null) && (response.getJSONObject("meta").getInt("code") == OK))
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
                        } catch (JSONException ignored){}
                        try
                        {
                            jo = jo.getJSONObject("location");
                        } catch (JSONException ignored){}
                        try
                        {
                            address = jo.getString("address");
                        } catch (JSONException ignored){}
                        try
                        {dist = jo.getInt("distance");}
                        catch (JSONException ignored){}
                        venues.add(new Venue(name, address, dist));
                    }
                    result = RES_OK;
                    callback.OnQueryResultReady(venues);
                } else
                {
                    callback.OnQueryResultReady(null);
                    result = RES_SERVER_ERROR;
                }
            } catch (JSONException e)
            {
                callback.OnQueryResultReady(null);
                result = RES_JSON;
            }
        }
    }

    private String readFile(String filename)
    {
        BufferedReader reader;
        StringBuilder sb = new StringBuilder();
        try
        {
            reader = new BufferedReader(new InputStreamReader(ctx.getAssets().open(filename)));
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

    private JSONObject getJSONObj(String input)
    {
        JSONObject jObj = null;
        try
        {
            jObj = new JSONObject(input);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jObj;
    }

    private void TestParser()
    {
        JSONObject jObj = getJSONObj(readFile("motherQuery.txt"));
        dummyModel = new DummyModel();
        Parser.parseResponse(jObj, dummyModel);
        if(dummyModel.getResult().size() != 6)
            testResult += "Aa\n";
        if(Parser.getResult() != Parser.RES_OK)
            testResult += "Ab\n";
        Venue v = dummyModel.getResult().get(1);
        if( v.getName().equals("Mother Wine") == false)
            testResult += "Ac\n";
        if( v.getAddress().equals("Gl Mønt 33") == false)
            testResult += "Ad\n";
        if( v.getDist() != 4662)
            testResult += "Ae\n";

        jObj = getJSONObj(readFile("empty.txt"));
        Parser.parseResponse(jObj, dummyModel);
        if(Parser.getResult() != Parser.RES_SERVER_ERROR)
            testResult += "Ba\n";
        if(dummyModel.getResult() != null)
            testResult += "Bb\n";

        jObj = getJSONObj(readFile("serverBadRequest.txt"));
        Parser.parseResponse(jObj, dummyModel);
        if(Parser.getResult() != Parser.RES_SERVER_ERROR)
            testResult += "Ca\n";
        if(dummyModel.getResult() != null)
            testResult += "Cb\n";

        jObj = getJSONObj(readFile("noName_0.txt"));
        Parser.parseResponse(jObj, dummyModel);
        if(!dummyModel.getResult().get(0).getName().equals(""))
            testResult += "Da\n";

        jObj = getJSONObj(readFile("missingAddresDistance_1.txt"));
        Parser.parseResponse(jObj, dummyModel);
        v = dummyModel.getResult().get(1);
        if(!v.getAddress().equals(""))
            testResult += "Ea\n";
        if(v.getDist() != -1)
            testResult += "Eb\n";
    }
}
