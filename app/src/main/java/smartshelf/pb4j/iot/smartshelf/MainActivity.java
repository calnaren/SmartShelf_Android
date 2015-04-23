package smartshelf.pb4j.iot.smartshelf;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button addItemButton = (Button) findViewById(R.id.button1);
        final Button locateItemButton = (Button) findViewById(R.id.button2);
        final Button viewShelfButton = (Button) findViewById(R.id.button3);
        final Button viewScheduleButton = (Button) findViewById(R.id.button4);

        addItemButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        addItemButton.setBackgroundColor(0xffaaaa00);
                        Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                        startActivity(intent);
                    }
                }
        );

        locateItemButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        locateItemButton.setBackgroundColor(0xffaaaa00);
                    }
                }
        );

        viewShelfButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        viewShelfButton.setBackgroundColor(0xffaaaa00);
                    }
                }
        );

        viewScheduleButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        viewScheduleButton.setBackgroundColor(0xffaaaa00);
                    }
                }
        );

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
}
