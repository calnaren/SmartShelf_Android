package smartshelf.pb4j.iot.smartshelf;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LocateItemActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataHolder.getInstance().setCurrentActivity("locate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_item);

        final ListView listview = (ListView) findViewById(R.id.itemList);

        /*String[] values = new String[]{"Ibuprofen", "Murinae", "Graval",
                "Hydrocodone", "Zocor", "Lisinopril", "Norvasc", "Azithromycin",
                "Amoxicillin"};*/

        List<String> values = new ArrayList<String>();
        final List<ShelfItem> listItems = DataHolder.getInstance().getItems();
        List<String> barcodes = DataHolder.getInstance().getBarcodes();
        List<String> itemNames = DataHolder.getInstance().getItemNames();
        for (ShelfItem i: listItems) {
            int index = barcodes.indexOf(i.getName());
            values.add(itemNames.get(index));
        }

        /*for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }*/
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, values);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(LocateItemActivity.this, ViewShelfActivity.class);

                //LED led = DataHolder.getInstance().getLed();
                //led.setR(0x00);
                //led.setG(0xFF);
                //led.setB(0x00);
                int pos = listItems.get(position).getIndex();
                //led.setIndex(pos);
                //DataHolder.getInstance().setLed(led);

                List<Integer> colors = DataHolder.getInstance().getColors();
                int iter = 0;
                for (int color: colors) {
                    if (color == 2) {
                        colors.set(iter, 0);
                    }
                    iter++;
                }
                colors.set(pos, 2); //Set to green
                DataHolder.getInstance().setColors(colors);

                startActivity(intent);
            }
        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_locate_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
