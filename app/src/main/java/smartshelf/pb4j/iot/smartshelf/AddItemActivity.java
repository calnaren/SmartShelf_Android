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
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AddItemActivity extends ActionBarActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    protected Map<String, String> uuidToKey;
    protected Map<String, String> keyToUuid;
    //public TextView displayData;
    public static final int smapDelay = 5000;
    public static final int refreshPeriod = 5000;
    private Timer timer = null;
    private TimerTask timerTask;
    private String sineWaveValue;
    public TextView displayData;
    public boolean runFlag = false;

    @Override
    protected void onDestroy() {
        runFlag = false;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        runFlag = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        final Button backButton = (Button) findViewById(R.id.backButton);
        final Button homeButton = (Button) findViewById(R.id.homeButton);

        displayData = (TextView) findViewById(R.id.displayData);

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

    protected void updateSineWaveText() {
        displayData.setText(sineWaveValue+"");
        //displayData.setAllCaps(true);
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
                AddItemActivity.this.querySmapView(null);
            }
        };
        timer.scheduleAtFixedRate(timerTask, delay, refreshPeriod);
    }

    protected boolean updatePref(String value) {
        sineWaveValue = value;
        return true;
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
        public static final String QUERY_LINE = "select data before now where uuid = 'f9838331-359b-4b49-9089-9016317abdbe'";
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
                                DefaultHttpClient httpclient = new DefaultHttpClient();
                                //HttpPost httpPostReq = new HttpPost(uri);
                                HttpPost httpPostReq = new HttpPost("http://shell.storm.pm:8079/api/query");
                                //StringEntity se = new StringEntity(String.format(QUERY_LINE, uuid));
                                StringEntity se = new StringEntity(QUERY_LINE);

                                httpPostReq.setEntity(se);
                                HttpResponse httpResponse = httpclient.execute(httpPostReq);
                                InputStream inputStream = httpResponse.getEntity().getContent();
                                final String response = inputStreamToString(inputStream);
                                Log.d("httpPost", response);
                                JSONObject jsonResponse = new JSONObject(response.substring(1, response.length() - 1));
                                JSONArray readings = ((JSONArray) jsonResponse.getJSONArray("Readings")).getJSONArray(0);
                                String retUuid = jsonResponse.getString("uuid");
                                String value = readings.getString(1);
                                AddItemActivity.this.updatePref(value);
                                System.out.println(value);

                                //FIXME: Should go into locate item rather than here
                                JSONObject message = new JSONObject();
                                message.put("r", "255");
                                message.put("g", "255");
                                message.put("b", "0");
                                message.put("index", "3");

                                int server_port = 2196;
                                InetAddress[] local = InetAddress.getAllByName("2001:470:66:3f9::2");
                                System.out.println(local[0]);
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
                                        AddItemActivity.this.updateSineWaveText();
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
