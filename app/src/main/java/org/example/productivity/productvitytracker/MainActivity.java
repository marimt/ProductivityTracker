package org.example.productivity.productvitytracker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;


public class MainActivity extends AppCompatActivity {

    FragmentManager manager = getFragmentManager();
    Toolbar toolbar;
    private static final String LOG_TAG = "TestInput";
    public static boolean isProductive;     //TODO change this so somehow status is set with button click, global is not ideal

    //Needed UI components
    EditText hours, minutes, month, day, year, activityType;
    //Button newUnprodActBtn, newProdActBtn, openGraph, openNavigation;

    //=========================================================================================================================================
    //=========================================================================================================================================

    //TODO remove and update buttons since some are depreciated. still need graph buttons but not in main, do in frag...look at sketches but for now just setting up original way
    /**
     * ^^^^ is goal for next time.
     * 1) add welcome default frag
     * 2) break up main nav frag into two frags, one for add activity & 1 for view history.
     * 3) Link those NAV LAYOUTS to sub FAB bttn (currently set directly to addmodfrag)
     * 4) link nav layouts to proper sub layouts
     * 5) set up next goal which is probably customizing the graph
     */
    //=========================================================================================================================================
    //=========================================================================================================================================




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Associate fields with layout
        hours = (EditText) findViewById(R.id.editDurationHours);
        minutes = (EditText) findViewById(R.id.editDurationMin);
        month = (EditText) findViewById(R.id.editMonth);
        day = (EditText) findViewById(R.id.editDay);
        year = (EditText) findViewById(R.id.editYear);
        activityType = (EditText) findViewById(R.id.editActivityType);

       /* newProdActBtn = (Button) findViewById(R.id.addProductiveBtn);
        newUnprodActBtn = (Button) findViewById(R.id.addUnproductiveBtn);
        openGraph = (Button) findViewById(R.id.GraphBtn);
        openNavigation = (Button) findViewById(R.id.NavTestBtn);
        */
        //TODO fix the toolbar...looks bad without it but need to fix fragments first
        //toolbar = (Toolbar) findViewById(R.id.toolbar);


        //Makes ImageViews for icons
        ImageView addTimeModFAB = new ImageView(this);
        ImageView addProdFAB = new ImageView(this);
        ImageView addUNprodFAB = new ImageView(this);

        //TODO get actual icons for these
        addTimeModFAB.setImageResource(R.drawable.circle);
        addProdFAB.setImageResource(R.drawable.circle);
        addUNprodFAB.setImageResource(R.drawable.circle);

        //Make main floating action bar button (FAB button)
        FloatingActionButton floatingActionButton = new FloatingActionButton.Builder(this).setContentView(addTimeModFAB).build();

        //Add SubAction that opens submenu which displays FAB sub-buttons after clicking addTimeModFAB
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        //Create and add sub-buttons to submenu
        SubActionButton buttonProdActivity = itemBuilder.setContentView(addProdFAB).build();
        SubActionButton buttonUNprodActivity = itemBuilder.setContentView(addUNprodFAB).build();

        //Create submenu for FAB sub-buttons
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonProdActivity)   //sub-button
                .addSubActionView(buttonUNprodActivity) //sub-button
                .attachTo(floatingActionButton) //main button
                .build();
                //TODO the sub buttons don't close when user presses one. fix that.

        //Set listeners for FAB sub-buttons
        //TODO Make code more elegant, I know remaking OnClickListener is not ideal, maybe do same thing switch case in fragments?
        buttonProdActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Link AddModuleFragment
                isProductive = true;   //set global productivity status as true so it's added into productive ArrayList

                MainNavigationFragment mainNavFrag = new MainNavigationFragment();
                AddModuleFragment addModFrag = new AddModuleFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                //TODO replace the fragment so it does not overlay when switching adding
                transaction.add((R.id.main_act), mainNavFrag, "AddProdModFragID");
                transaction.commit();

            }
        });

        buttonUNprodActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProductive = false;   //set global productivity status as false so it's added into UNproductive ArrayList

                Toast.makeText(getApplicationContext(), "omg what now",
                        Toast.LENGTH_SHORT).show();

            }
        });

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

    // isProductive() is used to access global isProductive boolean variable to be store a TimeModule as
    // productive or unproductive in AddModuleFragment
    public static boolean isProductive() {
        return isProductive;
    }
}
