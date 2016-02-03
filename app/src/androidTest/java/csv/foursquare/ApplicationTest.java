package csv.foursquare;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.util.Pair;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
/*
public class ApplicationTest extends ApplicationTestCase<Application>
{
    public ApplicationTest()
    {
        super(Application.class);
        //final InputStream file = getInstrumentation().getContext().getResources().getAssets().open("assets/" + filename + ".txt");
    }
}
*/

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ApplicationTest
{
    @Before
    public void wth()
    {
        String test = readFile("motherQuery.txt");
        int i = 0;
        ++i;
    }

    @Test
    public void test1()
    {
        // Set up the Parcelable object to send and receive.
        String test = readFile("motherQuery.txt");
        int i = 0;
        ++i;


        // Verify that the received data is correct.
        /*
        assertThat(venues.size(), is(1));
        assertThat(venues.get(0).first, is(TEST_STRING));
        assertThat(venues.get(0).second, is(TEST_LONG));
        */
    }

    private String readFile(String filename)
    {
        BufferedReader reader;
        StringBuilder sb = new StringBuilder();
        try
        {
            final InputStream file = getInstrumentation().getContext().getResources().getAssets().open("assets/" + filename);
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
}
