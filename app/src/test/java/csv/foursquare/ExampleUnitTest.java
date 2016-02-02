package csv.foursquare;

import android.location.Location;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest
{
    /*
    @Test
    public void addition_isCorrect() throws Exception
    {
        assertEquals(4, 2 + 2);
    }
*/
    public interface OnQueryResultReadyCallbacks
    {
        public void OnQueryResultReady(List<Venue> venues);
        public void OnQueryResultReady();
    }

    public class DummyCaller implements OnQueryResultReadyCallbacks
    {
        private final DummyCollaborator dummyCollaborator;
        private List<String> result = new ArrayList<String>();

        public DummyCaller(DummyCollaborator dummyCollaborator)
        {
            this.dummyCollaborator = dummyCollaborator;
        }

        public void doSomethingAsynchronously() {
            dummyCollaborator.doSomethingAsynchronously(this);
        }

        public List<String> getResult() {
            return this.result;
        }

        @Override
        public void OnQueryResultReady(List<Venue> venues)
        {

        }

        @Override
        public void OnQueryResultReady()
        {

        }
    }

    public class DummyCollaborator
    {

        public DummyCollaborator()
        {
            // empty
        }
        private Query query = new Query();
        private static final int OK = 200;

        public void doSomethingAsynchronously (final OnQueryResultReadyCallbacks callback)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    Location l = query.getlocation();
                    String lat = String.valueOf(l.getLatitude());
                    String lon = String.valueOf(l.getLongitude());

                    String url = "https://api.foursquare.com/v2/venues/search?";
                    url += "ll=" + lat + "," + lon;
                    url += "&client_id=" + query.getId();
                    url += "&client_secret=" + query.getSecret();
                    url += "&v=20140806";
                    url += "&query=" + query.getQuery();

                    final List<Venue> venues = new ArrayList<>();
                    RequestQueue queue = Volley.newRequestQueue(query.getContext());
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            try
                            {
                                if(response.getJSONObject("meta").getInt("code") == OK)
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
                                        }
                                        try
                                        {
                                            jo = jo.getJSONObject("location");
                                        } catch (JSONException ignored)
                                        {
                                        }
                                        try
                                        {
                                            address = jo.getString("address");
                                        } catch (JSONException ignored)
                                        {
                                        }
                                        try
                                        {
                                            dist = jo.getInt("distance");
                                        } catch (JSONException ignored)
                                        {
                                        }
                                        venues.add(new Venue(name, address, dist));
                                    }
                                    query.getListener().OnQueryResultReady(venues);
                                }
                                else
                                {
                                    query.getListener().OnQueryResultReady(null);
                                }
                            }
                            catch (JSONException e)
                            {
                                query.getListener().OnQueryResultReady(null);
                            }
                        }
                    }, new Response.ErrorListener()
                    {

                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            query.getListener().OnQueryResultReady(null);
                        }
                    });

                    queue.add(jsObjRequest);
                }
            }).start();
        }
    }

    public class DummyCollaboratorCallerTest
    {

        // Class under test
        private DummyCaller dummyCaller;

        @Mock
        private DummyCollaborator mockDummyCollaborator;

        @Captor
        private ArgumentCaptor<OnQueryResultReadyCallbacks> dummyCallbackArgumentCaptor;

        @Before
        public void setUp() {
            MockitoAnnotations.initMocks(this);
            dummyCaller = new DummyCaller(mockDummyCollaborator);
        }

        @Test
        public void testDoSomethingAsynchronouslyUsingArgumentCaptor()
        {
            // Let's call the method under test
            dummyCaller.doSomethingAsynchronously();

            // Let's call the callback. ArgumentCaptor.capture() works like a matcher.
            verify(mockDummyCollaborator, times(1)).doSomethingAsynchronously(
                    dummyCallbackArgumentCaptor.capture());

            // Some assertion about the state before the callback is called
            assertThat(dummyCaller.getResult().isEmpty(), is(true));

            // Once you're satisfied, trigger the reply on callbackCaptor.getValue().
            //dummyCallbackArgumentCaptor.getValue().onSuccess(results);

            // Some assertion about the state after the callback is called
            //assertThat(dummyCaller.getResult(), is(equalTo(results)));
        }
    }
/*
    @Test
    public void testDoSomethingAsynchronouslyUsingDoAnswer() {
        // Let's do a synchronous answer for the callback
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((DummyCallback)invocation.getArguments()[0]).onSuccess(results);
                return null;
            }
        }).when(mockDummyCollaborator).doSomethingAsynchronously(
                any(DummyCallback.class));

        // Let's call the method under test
        dummyCaller.doSomethingAsynchronously();

        // Verify state and interaction
        verify(mockDummyCollaborator, times(1)).doSomethingAsynchronously(
                any(DummyCallback.class));
        assertThat(dummyCaller.getResult(), is(equalTo(results)));
    }
*/
}