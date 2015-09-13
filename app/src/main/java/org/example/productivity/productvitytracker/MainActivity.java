package org.example.productivity.productvitytracker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity{

    private static final String LOG_TAG = "TestInput";
    public static boolean isProductive;     //TODO change this so somehow status is set with button click, global is not ideal
    FragmentManager manager = getFragmentManager();

    //TODO put all buttons and such in a fragment so the main activity itself is essetially blank except for the fragments that woll be added/removed

    //Needed UI components
    EditText hours, minutes, month, day, year, activityType;
    Button newUnprodActBtn, newProdActBtn, openGraph, openNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Associate EditText fields and Button with layout
        hours = (EditText) findViewById(R.id.editDurationHours);
        minutes = (EditText) findViewById(R.id.editDurationMin);
        month = (EditText) findViewById(R.id.editMonth);
        day = (EditText) findViewById(R.id.editDay);
        year = (EditText) findViewById(R.id.editYear);
        activityType = (EditText) findViewById(R.id.editActivityType);
        newProdActBtn =  (Button) findViewById(R.id.addProductiveBtn);
        newUnprodActBtn =  (Button) findViewById(R.id.addUnproductiveBtn);
        openGraph =  (Button) findViewById(R.id.GraphBtn);
        openNavigation = (Button) findViewById(R.id.NavTestBtn);


        //TODO Make code more elegant, I know remaking OnClickListener is not ideal, maybe do same thing switch case in fragments?
        newProdActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Link AddModuleFragment
                isProductive = true;   //set global productivity status as productive each time productive button is clicked
                AddModuleFragment addModFrag = new AddModuleFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                //TODO replace the fragment so it does not overlay
                transaction.add((R.id.main_act), addModFrag, "AddProdModFragID");
                transaction.commit();
            }
        });

        newUnprodActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Link AddModuleFragment
                isProductive = false;   //set global productivity status as productive each time unproductive button is clicked
                AddModuleFragment addModFrag = new AddModuleFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                //TODO replace the fragment so it does not overlay
                transaction.add((R.id.main_act), addModFrag, "AddUnprodModFragID");
                transaction.commit();
            }
        });

        openGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an object of intent class to help us open second activity
                Intent intent = new Intent("org.example.productivity.productvitytracker.GraphActivity"); //this is the package of the new activity
                startActivity(intent);
            }
        });

        openGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an object of intent class to help us open second activity
                Intent intent = new Intent("org.example.productivity.productvitytracker.MainNavigationActivity"); //this is the package of the new activity
                startActivity(intent);
            }
        });
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

    // isProductive() is used to access global isProductive boolean variable to be store a TimeModule as
    // productive or unproductive in AddModuleFragment
    public static boolean isProductive() {
        return isProductive;
    }
}
