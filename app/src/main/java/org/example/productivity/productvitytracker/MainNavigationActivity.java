package org.example.productivity.productvitytracker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainNavigationActivity extends AppCompatActivity {

    FragmentManager manager = getFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_navigation_activity);


        AddModuleFragment addNavFrag = new AddModuleFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add((R.id.main_act), addNavFrag, "AddProdModFragID");
        //TODO figure out how to launch the fragment as is in this blank activity then swipe or switch fragments without button touch?
        transaction.commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_navigation, menu);
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
