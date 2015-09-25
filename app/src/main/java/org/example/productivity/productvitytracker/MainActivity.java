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


public class MainActivity extends AppCompatActivity{

    FragmentManager manager = getFragmentManager();
    Toolbar toolbar;
    private static final String LOG_TAG = "TestInput";
    public static boolean isProductive;

    View view;  //create view as global to set up onClickListener switch cases

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
        final SubActionButton buttonAddActivity = itemBuilder.setContentView(addActFAB).build();
        final SubActionButton buttonViewHistory = itemBuilder.setContentView(viewHistoryFAB).build();

        //Create submenu for FAB sub-buttons
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonAddActivity)   //sub-button
                .addSubActionView(buttonViewHistory) //sub-button
                .attachTo(floatingActionButton) //main button
                .build();
        //TODO the sub buttons don't close when user presses one. fix that.

        //Set listeners for FAB sub-buttons
        //--------buttonAddActivuty FAB sub-button------------------------------------------------------------
        buttonAddActivity.setOnClickListener(new View.OnClickListener() {

            FragmentTransaction transaction = manager.beginTransaction();   //transaction for first time button is pressed
            FragmentTransaction transactionChange = manager.beginTransaction();     //transaction for case if buttonViewHistory is pressed on AddActivity fragment
            FragmentTransaction transactionChange2 = manager.beginTransaction();     //transaction for case if buttonViewHistory is pressed on AddActivity fragment
            AddActivityNavigationFragment addActivityNavigationFragment = new AddActivityNavigationFragment();
            ViewHistoryNavigationFragment viewHist = new ViewHistoryNavigationFragment();

            @Override
            public void onClick(View v) {
                //CASE 1 of buttonAddActivity button press - it has not been pressed before.
                //Show AddActivityNavigationFragment
                if (welcomeScreen.isVisible()) {   //setting the fragments only to be replaced under this condition stops it from mult overlay since add its only on startup
                    transaction.replace(R.id.main_act, addActivityNavigationFragment);  //use replace to prevent multiple overlay
                    transaction.commit();
                }

                //CASE 2 of buttonAddActivity button pressed while addActNav fragment is already up
                if (v == buttonAddActivity && addActivityNavigationFragment.isVisible()) {
                    Toast.makeText(getApplicationContext(), "Please finish adding the current activity", Toast.LENGTH_SHORT).show();
                }

                //CASE 3 - buttonViewHistory is pressed while addActNav is up
                buttonViewHistory.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v2) {
                        if (addActivityNavigationFragment.isVisible()) {
                            transactionChange.replace(R.id.main_act, viewHist);
                            transactionChange.commit();
                        }
                        //TODO add case for if add activity is pressed after switching to viewHist
                    }
                });

            }
        });
        //--------------------------------------------------------------------------------end buttonAddActivuty FAB sub-button----

        //--------buttonViewHistory FAB sub-button------------------------------------------------------------
        buttonViewHistory.setOnClickListener(new View.OnClickListener() {
            FragmentTransaction transaction = manager.beginTransaction();   //transaction for first time button is pressed
            FragmentTransaction transactionChange = manager.beginTransaction();     //transaction for case if buttonViewHistory is pressed on AddActivity fragment
            AddActivityNavigationFragment addActivityNavigationFragment = new AddActivityNavigationFragment();
            ViewHistoryNavigationFragment viewHist = new ViewHistoryNavigationFragment();

            @Override
            public void onClick(View v) {
                //CASE 1 of buttonViewHistory button press - it has not been pressed before.
                //Show ViewHistoryNavigationFragment
                if (welcomeScreen.isVisible()) {   //setting the fragments only to be replaced under this condition stops it from mult overlay since add its only on startup
                    transaction.replace(R.id.main_act, viewHist);  //use replace to prevent multiple overlay
                    transaction.commit();
                }



                //CASE 3 - buttonAddActivity is pressed while viewHist is up
                buttonAddActivity.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v2) {
                        if (viewHist.isVisible()) {
                            transactionChange.replace(R.id.main_act, addActivityNavigationFragment);
                            transactionChange.commit();
                        }
                        // SIDE CASE - if addActNav button pressed while addActNav fragment is already up
                        if (v2 == buttonAddActivity && addActivityNavigationFragment.isVisible()) {
                                Toast.makeText(getApplicationContext(), "Please finish adding the current activity", Toast.LENGTH_SHORT).show();
                        }
                        //TODO add case for if viewhist is pressed after switching to addactivity
                    }
                });

            }
        });
        //TODO EXTRA: this FAB button code for cases is redundant...ideally  would want to make a class for it...or do a case-switch by implementing  onclicklistener
        //--------------------------------------------------------------------------------end buttonViewActivity FAB sub-button----

            //below is format for adding an activity. pretty much just note setting isProductive boolean and that there is a function for it at bottom @isProductive()
            //oh and the global variable
            /*
            //Link AddModuleNavigationFragment
            isProductive=true;   //set global productivity status as true so it's added into productive ArrayList

            AddModuleFragment addModFrag = new AddModuleFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add((R.id.main_act),mainNavFrag,"AddProdModFragID");
            transaction.commit();
            */


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
