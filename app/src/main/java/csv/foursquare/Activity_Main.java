package csv.foursquare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

public class Activity_Main extends AppCompatActivity implements I_View
{
    Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new Presenter(this);
        EditText etSearch = (EditText) findViewById(R.id.etSearchString);

        ListView listView = (ListView) findViewById(R.id.lvVenues);
        listView.setAdapter(VenueAdapter.getInstance());

        etSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                presenter.searchStringChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


}
