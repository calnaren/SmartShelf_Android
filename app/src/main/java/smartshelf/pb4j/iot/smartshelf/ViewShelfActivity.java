package smartshelf.pb4j.iot.smartshelf;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;


public class ViewShelfActivity extends ActionBarActivity {

    final EditText[][] lights = new EditText[3][3];
    final ImageView [][] bottles = new ImageView[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataHolder.getInstance().setCurrentActivity("shelf");

        int currentIndex, r, g, b, color;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shelf);

        final Button backButton = (Button) findViewById(R.id.backButton);
        final Button homeButton = (Button) findViewById(R.id.homeButton);

        lights[0][0] = (EditText) findViewById(R.id.tray1_2);
        lights[0][1] = (EditText) findViewById(R.id.tray1_4);
        lights[0][2] = (EditText) findViewById(R.id.tray1_6);
        lights[1][0] = (EditText) findViewById(R.id.tray2_2);
        lights[1][1] = (EditText) findViewById(R.id.tray2_4);
        lights[1][2] = (EditText) findViewById(R.id.tray2_6);
        lights[2][0] = (EditText) findViewById(R.id.tray3_2);
        lights[2][1] = (EditText) findViewById(R.id.tray3_4);
        lights[2][2] = (EditText) findViewById(R.id.tray3_6);

        bottles[0][0] = (ImageView) findViewById(R.id.pillBottle0);
        bottles[0][1] = (ImageView) findViewById(R.id.pillBottle1);
        bottles[0][2] = (ImageView) findViewById(R.id.pillBottle2);
        bottles[1][0] = (ImageView) findViewById(R.id.pillBottle3);
        bottles[1][1] = (ImageView) findViewById(R.id.pillBottle4);
        bottles[1][2] = (ImageView) findViewById(R.id.pillBottle5);
        bottles[2][0] = (ImageView) findViewById(R.id.pillBottle6);
        bottles[2][1] = (ImageView) findViewById(R.id.pillBottle7);
        bottles[2][2] = (ImageView) findViewById(R.id.pillBottle8);

        List<Integer> weights = DataHolder.getInstance().getShelf();

        List<ShelfItem> tempItems = DataHolder.getInstance().getItems();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                bottles[i][j].setAlpha(0.0f);
            }
        }
        for (ShelfItem item: tempItems) {
            bottles[item.getIndex()/3][item.getIndex()%3].setAlpha(1.0f);
        }

        LED led = DataHolder.getInstance().getLed();
        currentIndex = led.getIndex();
        r = led.getR();
        g = led.getG();
        b = led.getB();
        color = (0xff<<24) | (r<<16) | (g<<8) | b;
        if (color == 0xff000000) {
            color = 0xffffffff;
        }
        lights[currentIndex/3][currentIndex%3].setBackgroundColor(color);
        System.out.println(currentIndex);

        backButton.setOnClickListener(
            new Button.OnClickListener() {
                public void onClick(View v)
                {
                    resetLights();
                    Intent intent = new Intent(ViewShelfActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        );

        homeButton.setOnClickListener(
            new Button.OnClickListener() {
                public void onClick(View v)
                {
                    resetLights();
                    Intent intent = new Intent(ViewShelfActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_shelf, menu);
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

    private void resetLights() {
        LED led = DataHolder.getInstance().getLed();
        led.setR(0);
        led.setG(0);
        led.setB(0);
        DataHolder.getInstance().setLed(led);
    }
}
