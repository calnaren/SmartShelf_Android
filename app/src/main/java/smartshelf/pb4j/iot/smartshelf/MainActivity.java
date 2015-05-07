package smartshelf.pb4j.iot.smartshelf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.ContactsContract;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import proj.tony.com.bearcastutil.BearCastUtil;

public class MainActivity extends ActionBarActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    protected Map<String, String> uuidToKey;
    protected Map<String, String> keyToUuid;
    //public TextView displayData;
    public static final int smapDelay = 5000;
    public static final int refreshPeriod = 5000;
    private Timer timer = null;
    private TimerTask timerTask;
    public boolean runFlag = false;
    private static BearCastUtil sBearCastUtil;

    @Override
    protected void onDestroy() {
        runFlag = false;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataHolder.getInstance().setCurrentActivity("main");
        runFlag = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sBearCastUtil = new BearCastUtil(this, "PB4J");
        final Button addItemButton = (Button) findViewById(R.id.button1);
        final Button locateItemButton = (Button) findViewById(R.id.button2);
        final Button viewShelfButton = (Button) findViewById(R.id.button3);
        final Button viewScheduleButton = (Button) findViewById(R.id.button4);

        addItemButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                        startActivity(intent);
                    }
                }
        );

        locateItemButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(MainActivity.this, LocateItemActivity.class);
                        startActivity(intent);
                    }
                }
        );

        viewShelfButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(MainActivity.this, ViewShelfActivity.class);
                        startActivity(intent);
                    }
                }
        );

        viewScheduleButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(MainActivity.this, ViewScheduleActivity.class);
                        startActivity(intent);
                    }
                }
        );

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean firstStart = settings.getBoolean("firstStart", true);

        if(firstStart) {
            //display your Message here

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstStart", false);
            editor.commit();
            initMaps();
            sendUpdate();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            sBearCastUtil.startBluetoothScan();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "BearCast Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onResume();
        try {
            sBearCastUtil.stopBlueToothScan();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "BearCast Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    private void initMaps() {
        uuidToKey = new HashMap<String, String>();
        //uuidToKey.put("dafe6489-e85a-5641-93be-f8c2a7ec72c4", "sine wave");
        uuidToKey.put("f9838331-359b-4b49-9089-9016317abdbe", "barcode");
        keyToUuid = new HashMap<String, String>();
        for(Map.Entry<String, String> entry : uuidToKey.entrySet()){
            keyToUuid.put(entry.getValue(), entry.getKey());
        }

    }

    protected void updateBarcodeText() {
        //displayData.setText(barcodeValue+"");
        //displayData.setAllCaps(true);
    }

    protected void checkSchedule() {
        String notifyString = "Please take ";
        Calendar calendar = Calendar.getInstance();
        int minutes = calendar.get(Calendar.MINUTE);
        int relativeMinutes = minutes%15;
        List<ShelfItem> items = DataHolder.getInstance().getItems();
        List<Integer> weights = DataHolder.getInstance().getShelf();
        List<String> barcodes = DataHolder.getInstance().getBarcodes();
        List<String> itemNames = DataHolder.getInstance().getItemNames();
        List<Integer> colors = DataHolder.getInstance().getColors();
        if (relativeMinutes == 0 && DataHolder.getInstance().isNewDay()) {
            DataHolder.getInstance().setNewDay(false);
            for (ShelfItem item: items) {
                boolean [] temp = {false, false, false};
                item.setTaken(temp);
            }
        }
        for (ShelfItem item: items) {
            if (item.getIndex() > -1) {
                boolean[] schedule = item.getSchedule();
                boolean[] taken = item.getTaken();
                boolean[] notified = item.getNotified();
                if (relativeMinutes < 1) {
                    if (colors.get(item.getIndex()) == 1) {
                        colors.set(item.getIndex(), 0);
                    }
                    notified[2] = false;
                    if (!taken[0] && schedule[0]) {
                        if (weights.get(item.getIndex()) < DataHolder.getInstance().getWEIGHT_THRESHOLD()) {
                            taken[0] = true;
                        }
                    }
                } else if (relativeMinutes < 5 && !taken[0] && schedule[0]) {
                    if (weights.get(item.getIndex()) < DataHolder.getInstance().getWEIGHT_THRESHOLD()) {
                        taken[0] = true;
                        if (colors.get(item.getIndex()) == 1) {
                            colors.set(item.getIndex(), 0);
                        }
                    }
                    else {
                        colors.set(item.getIndex(), 1);
                    }
                    if (!notified[0] && !taken[0]) {
                        //notifyString += item.getName() + " ";
                        notifyString += itemNames.get(barcodes.indexOf(item.getName()))+" ";
                        notified[0] = true;
                    }
                } else if (relativeMinutes < 6) {
                    if (colors.get(item.getIndex()) == 1) {
                        colors.set(item.getIndex(), 0);
                    }
                    notified[0] = false;
                    if (!taken[1] && schedule[1]) {
                        if (weights.get(item.getIndex()) < DataHolder.getInstance().getWEIGHT_THRESHOLD()) {
                            taken[1] = true;
                        }
                    }
                } else if (relativeMinutes < 10 && !taken[1] && schedule[1]) {
                    if (weights.get(item.getIndex()) < DataHolder.getInstance().getWEIGHT_THRESHOLD()) {
                        taken[1] = true;
                        if (colors.get(item.getIndex()) == 1) {
                            colors.set(item.getIndex(), 0);
                        }
                    }
                    else {
                        colors.set(item.getIndex(), 1);
                    }
                    if (!notified[1] && !taken[1]) {
                        //notifyString += item.getName() + " ";
                        notifyString += itemNames.get(barcodes.indexOf(item.getName())) + " ";
                        notified[1] = true;
                    }
                } else if (relativeMinutes < 11) {
                    if (colors.get(item.getIndex()) == 1) {
                        colors.set(item.getIndex(), 0);
                    }
                    notified[1] = false;
                    if (!taken[2] && schedule[2]) {
                        if (weights.get(item.getIndex()) < DataHolder.getInstance().getWEIGHT_THRESHOLD()) {
                            taken[2] = true;
                        }
                    }
                } else if (relativeMinutes < 15 && !taken[2] && schedule[2]) {
                    if (weights.get(item.getIndex()) < DataHolder.getInstance().getWEIGHT_THRESHOLD()) {
                        taken[2] = true;
                        if (colors.get(item.getIndex()) == 1) {
                            colors.set(item.getIndex(), 0);
                        }
                    }
                    else {
                        colors.set(item.getIndex(), 1);
                    }
                    if (!notified[2] && !taken[2]) {
                        //notifyString += item.getName() + " ";
                        notifyString += itemNames.get(barcodes.indexOf(item.getName()))+" ";
                        notified[2] = true;
                    }
                    DataHolder.getInstance().setNewDay(true);
                }
            }
        }
        if (!notifyString.equals("Please take ")) {
            Toast.makeText(getBaseContext(), notifyString, Toast.LENGTH_LONG).show();

            String[] data = new String[1];
            data[0] = notifyString;
            List<Integer> colors2 = DataHolder.getInstance().getColors();
            int i = 1;
            for (int color: colors) {
                data[i] = color+"";
                i++;
            }
            String[] dataTypes = {"string", "number", "number", "number", "number", "number", "number"};
            String templateName = "smartShelfTemplate.html";
            sBearCastUtil.deviceCast(data, dataTypes, templateName);
        }
        DataHolder.getInstance().setItems(items);
        DataHolder.getInstance().setColors(colors);
    }

    public void sendUpdate() {
        rescheduleTimer();
    }

    private void rescheduleTimer() {
        rescheduleTimer(smapDelay);
    }

    private void rescheduleTimer(int delay) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.querySmapView(null);
            }
        };
        timer.scheduleAtFixedRate(timerTask, delay, refreshPeriod);
    }

    private static String inputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class SmapQueryAsyncTask extends AsyncTask<String, Void, Boolean> {
        private String uuid;
        public static final String QUERY_LINE_BARCODE = "select data before now where uuid = 'f9838331-359b-4b49-9089-9016317abdbe'";
        public static final String QUERY_LINE_SHELF = "select data before now where uuid = 'e353dafc-2c5e-4c55-b245-83ac5a2bb6ab'";
        public SmapQueryAsyncTask(String uuid) {
            super();
            this.uuid = uuid;
        }
        @Override
        protected Boolean doInBackground(String...urls) {
            for (String url : urls) {
                final String uri = url;
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (runFlag) {
                            try {
                                HttpParams par = new BasicHttpParams();
                                HttpConnectionParams.setConnectionTimeout(par, 3000);
                                HttpConnectionParams.setSoTimeout(par, 5000);
                                DefaultHttpClient httpclient = new DefaultHttpClient(par);
                                //HttpPost httpPostReq = new HttpPost(uri);
                                HttpPost httpPostReq = new HttpPost("http://shell.storm.pm:8079/api/query");
                                //StringEntity se = new StringEntity(String.format(QUERY_LINE_BARCODE, uuid));
                                StringEntity se = new StringEntity(QUERY_LINE_BARCODE);

                                httpPostReq.setEntity(se);
                                HttpResponse httpResponse = httpclient.execute(httpPostReq);
                                InputStream inputStream = httpResponse.getEntity().getContent();
                                final String response = inputStreamToString(inputStream);
                                Log.d("httpPost", response);
                                JSONObject jsonResponse = new JSONObject(response.substring(1, response.length() - 1));
                                JSONArray readings = ((JSONArray) jsonResponse.getJSONArray("Readings")).getJSONArray(0);
                                String retUuid = jsonResponse.getString("uuid");

                                String value = readings.getString(1);

                                DataHolder.getInstance().setBarcode(value);

                                System.out.println(value);
                                StringEntity se_shelf = new StringEntity(QUERY_LINE_SHELF);
                                httpPostReq.setEntity(se_shelf);
                                HttpResponse httpResponse_shelf = httpclient.execute(httpPostReq);
                                InputStream inputStream_shelf = httpResponse_shelf.getEntity().getContent();
                                final String response_shelf = inputStreamToString(inputStream_shelf);
                                Log.d("httpPost", response_shelf);
                                JSONObject jsonResponse_shelf = new JSONObject(response_shelf.substring(1, response_shelf.length() - 1));
                                JSONArray readings_shelf = ((JSONArray) jsonResponse_shelf.getJSONArray("Readings")).getJSONArray(0);
                                String retUuid_shelf = jsonResponse_shelf.getString("uuid");

                                JSONArray value_shelf = readings_shelf.getJSONArray(1);
                                List<Integer> shelfWeights = new ArrayList<Integer>();
                                for (int iter = 0; iter < value_shelf.length(); iter++) {
                                    shelfWeights.add(Integer.parseInt(value_shelf.getString(iter)));
                                }
                                DataHolder.getInstance().setShelf(shelfWeights);

                                //FIXME: Should go into locate item rather than here
                                JSONObject message = new JSONObject();
                                /*LED led = DataHolder.getInstance().getLed();

                                message.put("r", led.getR()+"");
                                message.put("g", led.getG()+"");
                                message.put("b", led.getB()+"");
                                message.put("index", led.getIndex()+"");*/

                                List<Integer> colors = DataHolder.getInstance().getColors();
                                int iter2 = 0;
                                for (int color: colors) {
                                    message.put(iter2+"", color+"");
                                    iter2++;
                                }

                                int server_port = 2196;
                                InetAddress[] local = InetAddress.getAllByName("2001:470:66:3f9::2");
                                int msg_length = message.toString().length();
                                byte[] message2 = message.toString().getBytes();

                                DatagramSocket s = new DatagramSocket();
                                for (int iter = 0; iter < 1; iter++) {
                                    DatagramPacket p = new DatagramPacket(message2, msg_length, local[0], server_port);
                                    s.send(p);
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MainActivity.this.updateBarcodeText();
                                        MainActivity.this.checkSchedule();
                                    }
                                });

                            } catch (Exception e) {
                                //Log.d("httpPost", "failed");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Please Check Connection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }
                });
                t.run();
            }
            return true;
        }
        // onPostExecute displays the results of the AsyncTask.
        protected void onPostExecute(String result) {
        }
    }

    public void querySmapView(View v) {
        for(String uuid : uuidToKey.keySet()) {
            querySmap(uuid);
        }
    }

    private void querySmap(String uuid) {
        new SmapQueryAsyncTask(uuid).execute("http://shell.storm.pm:8079/api/query");
    }

}
