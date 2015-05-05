package smartshelf.pb4j.iot.smartshelf;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.List;


public class SetSchedule extends ActionBarActivity {

    private CheckBox morning;
    private CheckBox afternoon;
    private CheckBox night;
    private Button proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);

        morning = (CheckBox) findViewById(R.id.morning);
        afternoon = (CheckBox) findViewById(R.id.afternoon);
        night = (CheckBox) findViewById(R.id.night);
        proceed = (Button) findViewById(R.id.proceed);

        proceed.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        List<ShelfItem> items = DataHolder.getInstance().getItems();
                        int size = items.size();
                        boolean [] sched = {morning.isChecked(), afternoon.isChecked(), night.isChecked()};
                        items.get(size-1).setSchedule(sched);
                        DataHolder.getInstance().setItems(items);
                        Intent intent = new Intent(SetSchedule.this, PlaceActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_schedule, menu);
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
