package smartshelf.pb4j.iot.smartshelf;

import android.app.ActionBar;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ViewScheduleActivity extends ActionBarActivity {

    private GridView medicineGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        TableLayout medicineTable = (TableLayout) findViewById(R.id.medicineTable);

        final Button backButton = (Button) findViewById(R.id.backButton);
        final Button homeButton = (Button) findViewById(R.id.homeButton);

        List<ShelfItem> items = DataHolder.getInstance().getItems();
        List<TextView> medicines = new ArrayList<TextView>();

        TableRow header = new TableRow(this);
        header.setLayoutParams(new ViewGroup.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        TextView nameHeader = new TextView(this);
        nameHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
        nameHeader.setText("Item Name");

        TextView shelfNumberHeader = new TextView(this);
        shelfNumberHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
        shelfNumberHeader.setText("Shelf");

        TextView morningHeader = new TextView(this);
        TextView afternoonHeader = new TextView(this);
        TextView nightHeader = new TextView(this);
        morningHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
        afternoonHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
        nightHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
        morningHeader.setText("Morning ");
        afternoonHeader.setText("Afternoon");
        nightHeader.setText("Night   ");

        header.addView(nameHeader);
        header.addView(shelfNumberHeader);
        header.addView(morningHeader);
        header.addView(afternoonHeader);
        header.addView(nightHeader);
        medicineTable.addView(header, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        for (ShelfItem item: items) {
            TableRow temp = new TableRow(this);
            temp.setLayoutParams(new ViewGroup.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            TextView nameText = new TextView(this);
            nameText.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
            TextView numberText = new TextView(this);
            nameText.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
            int index = DataHolder.getInstance().getBarcodes().indexOf(item.getName());
            if (index > 0) {
                String tempName = DataHolder.getInstance().getItemNames().get(index);
                nameText.setText(tempName);
                numberText.setText(item.getIndex()+"");

                TextView morning = new TextView(this);
                TextView afternoon = new TextView(this);
                TextView night = new TextView(this);

                morning.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
                afternoon.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
                night.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
                int morningColor = item.getSchedule()[0] ? (item.getTaken()[0] ? 0xff00ff00 : 0xffff0000) : 0xffffffff;
                morning.setBackgroundColor(morningColor);
                morning.setText("      ");
                int afternoonColor = item.getSchedule()[1] ? (item.getTaken()[1] ? 0xff00ff00 : 0xffff0000) : 0xffffffff;
                afternoon.setBackgroundColor(afternoonColor);
                afternoon.setText("      ");
                int nightColor = item.getSchedule()[2] ? (item.getTaken()[2] ? 0xff00ff00 : 0xffff0000) : 0xffffffff;
                night.setBackgroundColor(nightColor);
                night.setText("      ");

                temp.addView(nameText);
                temp.addView(numberText);
                temp.addView(morning);
                temp.addView(afternoon);
                temp.addView(night);

                medicineTable.addView(temp, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }

        backButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(ViewScheduleActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        );

        homeButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(ViewScheduleActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        DataHolder.getInstance().setCurrentActivity("schedule");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_schedule, menu);
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
