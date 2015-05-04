package smartshelf.pb4j.iot.smartshelf;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class PlaceActivity extends ActionBarActivity {

    public static final int refreshPeriod = 5000;
    private Timer timer = null;
    private TimerTask timerTask;
    public TextView displayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataHolder.getInstance().setCurrentActivity("place");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        displayData = (TextView) findViewById(R.id.displayData);

        if (!DataHolder.getInstance().isPlaceFlag()) {
            rescheduleTimer(refreshPeriod);
            DataHolder.getInstance().setPlaceFlag(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place, menu);
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

    private void rescheduleTimer(int delay) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                PlaceActivity.this.updateData();
            }
        };
        timer.scheduleAtFixedRate(timerTask, delay, refreshPeriod);
    }

    public void updateData() {
        //update the widgets over here
        new UpdatingUIElements().execute();
    }

    public void checkItemDetected() {
        List<Integer> shelfInfo = DataHolder.getInstance().getShelf();
        String text = "";
        for (Integer s: shelfInfo) {
            text += s + " ";
        }
        displayData.setText(DataHolder.getInstance().getItems().size()+"");

        List<Integer> previous = DataHolder.getInstance().getPreviousShelf();
        int j = 0;
        boolean flag = false;
        int index = -1;
        List<ShelfItem> items = DataHolder.getInstance().getItems();
        ArrayList<Integer> used = new ArrayList<Integer>();
        for (ShelfItem item: items) {
            used.add(item.getIndex());
        }
        for (Integer i: DataHolder.getInstance().getShelf()) {
            if (!used.contains(j)) {
                if (i - (previous.get(j)) > 10) {
                    flag = true;
                    index = j;
                    List<ShelfItem> tempItems = DataHolder.getInstance().getItems();
                    tempItems.get(tempItems.size() - 1).setIndex(index);
                    tempItems.get(tempItems.size() - 1).setWeight(i);
                    DataHolder.getInstance().setItems(tempItems);
                    DataHolder.getInstance().setLed(new LED(index, 0, 255, 0));
                }
            }
            j++;
        }

        if (flag) {
            Intent intent = new Intent(PlaceActivity.this, ViewShelfActivity.class);
            PlaceActivity.this.startActivity(intent);
        }
    }

    private class UpdatingUIElements extends AsyncTask<String, Void, Boolean> {
        public UpdatingUIElements() {
            super();
        }
        @Override
        protected Boolean doInBackground(String...urls) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (DataHolder.getInstance().getCurrentActivity() == "place") {
                                PlaceActivity.this.checkItemDetected();
                            }
                        }
                    });
                }
            });
            t.run();
            return true;
        }
        // onPostExecute displays the results of the AsyncTask.
        protected void onPostExecute(String result) {
        }
    }
}
