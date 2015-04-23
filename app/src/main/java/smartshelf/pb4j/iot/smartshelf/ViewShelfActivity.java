package smartshelf.pb4j.iot.smartshelf;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class ViewShelfActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shelf);

        final Button backButton = (Button) findViewById(R.id.backButton);
        final Button homeButton = (Button) findViewById(R.id.homeButton);

        backButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(ViewShelfActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        );

        homeButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v)
                    {
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
}
