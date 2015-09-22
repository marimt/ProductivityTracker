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

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;


public class MainActivity extends AppCompatActivity {

    FragmentManager manager = getFragmentManager();
    Toolbar toolbar;
    private static final String LOG_TAG = "TestInput";
    public static boolean isProductive;

    //Needed UI components
    EditText hours, minutes, month, day, year, activityType;

    //=========================================================================================================================================
    //=========================================================================================================================================
    /**
     * ^^^^ is goal for next time.
     * 3b) Set up onClickListeners for view and add NAVIGATION framents
     * 4) link nav layouts to proper sub layouts, add to addMod and view to GraphActivity
     * 5) set up next goals which is probably customizing the graph
     * 6) storage
     * 7) prettify
     * 9) fix bugs in to dos
     * 8) test and port out yo
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

        //TODO fix the toolbar...looks bad without it but need to fix fragments first
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        //Add default welcome layout. Will be switched for other layouts (add activity, history) depending on user behavior
        final WelcomeScreenFragment welcomeScreen = new WelcomeScreenFragment();
        FragmentTransaction transaction2 = manager.beginTransaction();
        transaction2.add((R.id.main_act), welcomeScreen, "defaultWelcomeScrn");
        transaction2.commit();


        //Makes ImageViews for icons
        ImageView mainNavFAB = new ImageView(this);
        ImageView addActFAB = new ImageView(this);
        ImageView viewHistoryFAB = new ImageView(this);

        //TODO get actual icons for these
        //Assign icons for FAB buttons
        mainNavFAB.setImageResource(R.drawable.circle);
        addActFAB.setImageResource(R.drawable.circle);
        viewHistoryFAB.setImageResource(R.drawable.circle);

        //Make main floating action bar button (FAB button)
        FloatingActionButton floatingActionButton = new FloatingActionButton.Builder(this).setContentView(mainNavFAB).build();

        //Add SubAction that opens submenu which displays FAB sub-buttons after clicking mainNavFAB
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        //Create and add sub-buttons to submenu
        SubActionButton buttonAddActivity = itemBuilder.setContentView(addActFAB).build();
        SubActionButton buttonViewHistory = itemBuilder.setContentView(viewHistoryFAB).build();

        //Create submenu for FAB sub-buttons
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonAddActivity)   //sub-button
                .addSubActionView(buttonViewHistory) //sub-button
                .attachTo(floatingActionButton) //main button
                .build();
                //TODO the sub buttons don't close when user presses one. fix that.

        //Set listeners for FAB sub-buttons
        buttonAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Link AddActivityNavigationFragment

                AddActivityNavigationFragment addActivityNavigationFragment = new AddActivityNavigationFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.remove(welcomeScreen);
                //TODO figure out how to check if addNav is open since right now if it's open it will overlay...
                transaction.add((R.id.main_act), addActivityNavigationFragment, "addActNav");
                transaction.commit();


                //below is format for adding an activity. pretty much just note setting isProductive boolean and that there is a function for it at bottom @isProductive()
                //oh and the global variable
                /*
                //Link AddModuleNavigationFragment
                isProductive = true;   //set global productivity status as true so it's added into productive ArrayList

                AddModuleFragment addModFrag = new AddModuleFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                //TODO replace the fragment so it does not overlay when switching adding
                transaction.add((R.id.main_act), mainNavFrag, "AddProdModFragID");
                transaction.commit();
               */

            }
        });

        buttonViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Link ViewHistoryNavigationFragment

                ViewHistoryNavigationFragment viewHistoryNavigationFragment = new ViewHistoryNavigationFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.remove(welcomeScreen);
                //TODO figure out how to check if addNav is open since right now if it's open it will overlay...
                transaction.add((R.id.main_act), viewHistoryNavigationFragment, "viewHistNav");
                transaction.commit();
            }
        });

        //TODO fix the overall navigation. right now back button exits out of the whole app...should go to previous state...
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
