package diplomarbeit.audioant.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import diplomarbeit.audioant.R;

public class ListActivity extends AppCompatActivity {

    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_files);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listOfSounds = (ListView) findViewById(R.id.list);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listOfSounds.setAdapter(adapter);

        listItems.add("Mikrowelle");
        listItems.add("Türklingel");
        adapter.notifyDataSetChanged();
    }
}
