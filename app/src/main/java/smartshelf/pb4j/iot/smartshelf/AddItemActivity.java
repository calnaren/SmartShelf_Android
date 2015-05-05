package smartshelf.pb4j.iot.smartshelf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AddItemActivity extends ActionBarActivity {

    public static final int refreshPeriod = 5000;
    private Timer timer = null;
    private TimerTask timerTask;

    public TextView displayData;
    public TextView shelfData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataHolder.getInstance().setCurrentActivity("add");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        final Button backButton = (Button) findViewById(R.id.backButton);
        final Button homeButton = (Button) findViewById(R.id.homeButton);

        displayData = (TextView) findViewById(R.id.displayData);
        shelfData = (TextView) findViewById(R.id.shelfData);

        backButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        );

        homeButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        );

        if (!DataHolder.getInstance().isAddFlag()) {
            rescheduleTimer(refreshPeriod);
            DataHolder.getInstance().setAddFlag(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
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
                AddItemActivity.this.updateData();
            }
        };
        timer.scheduleAtFixedRate(timerTask, delay, refreshPeriod);
    }

    public void updateData() {
        //update the widgets over here
        new UpdatingUIElements().execute();
    }

    public void updateBarcode() {
        String barcode = DataHolder.getInstance().getBarcode();
        String previousBarcode = DataHolder.getInstance().getPreviousBarcode();
        if (previousBarcode.equals("")) {
            DataHolder.getInstance().setPreviousBarcode(barcode);
            displayData.setText("Waiting for barcode...");
        }
        else if (!barcode.equals(previousBarcode)) {
            List<ShelfItem> temp = DataHolder.getInstance().getItems();
            ShelfItem newItem = new ShelfItem();
            newItem.setName(barcode);
            boolean [] tempSchedule = {true, false, true};
            newItem.setSchedule(tempSchedule);
            temp.add(newItem);
            DataHolder.getInstance().setItems(temp);
            /*Intent intent = new Intent(AddItemActivity.this, PlaceActivity.class);
            AddItemActivity.this.startActivity(intent);*/
            Intent intent = new Intent(AddItemActivity.this, SetSchedule.class);
            AddItemActivity.this.startActivity(intent);
        }
    }

    public void updateShelf() {
        List<Integer> shelfInfo = DataHolder.getInstance().getShelf();
        String text = "";
        for (Integer s: shelfInfo) {
            text += s + " ";
        }
        shelfData.setText(text);
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
                            if (DataHolder.getInstance().getCurrentActivity() == "add") {
                                AddItemActivity.this.updateBarcode();
                                AddItemActivity.this.updateShelf();
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
