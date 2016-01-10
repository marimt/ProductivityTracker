package org.example.productivity.productvitytracker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{

    FragmentManager manager = getFragmentManager();

    //Needed UI components
    EditText hours, minutes, month, day, year, activityType;    //for AddModuleFragment

    private DrawerLayout drawerLayout;
    private FloatingActionButton mFAB;
    private NavigationView  navigationView;
    private Toolbar toolbar;

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
        mFAB = (FloatingActionButton) findViewById(R.id.FAB);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);

        //Declare toolbar to be the action bar
        setSupportActionBar(toolbar);
        //Set support action bar to be able to set menu icon
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_menu_white);
        if (actionBar  != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Add default welcome layout. Will be switched for other layouts (add activity, history) depending on user behavior
        final WelcomeScreenFragment welcomeScreen = new WelcomeScreenFragment();
        FragmentTransaction transaction2 = manager.beginTransaction();
        transaction2.add((R.id.main_act), welcomeScreen, "defaultWelcomeScrn");
        transaction2.commit();

        final AddActivityNavigationFragment addActivityNavigationFragment = new AddActivityNavigationFragment();
        //--------buttonAddActivity FAB button------------------------------------------------------------
        mFAB.setOnClickListener(new View.OnClickListener() {

            FragmentTransaction transaction = manager.beginTransaction();
            FragmentTransaction transactionChangeBack = manager.beginTransaction();

            ViewHistoryNavigationFragment viewHist = new ViewHistoryNavigationFragment();

            @Override
            public void onClick(View v) {
                //CASE 1 of buttonAddActivity button press - it has not been pressed before.
                //Show AddActivityNavigationFragment
                if (welcomeScreen.isVisible()) {   //setting the fragments only to be replaced under this condition stops it from mult overlay since add its only on startup
                    transaction.replace(R.id.main_act, addActivityNavigationFragment);  //use replace to prevent multiple overlay
                    transaction.commit();
                }
                    //TODO: add cases for  user pressing button after initial startup
                //CASE 2 of buttonAddActivity button pressed while addActNav fragment is already visible
//                if (addActivityNavigationFragment.isVisible()) {
//                    Toast.makeText(getApplicationContext(), "Please finish adding the current activity", Toast.LENGTH_SHORT).show();
//                }
//
//                //CASE 3 - buttonAddActivity is pressed while viewHist fragment is visible
//                if (v == mFAB && viewHist.isVisible()) {
//                    transactionChangeBack.replace(R.id.main_act, addActivityNavigationFragment);
//                    transactionChangeBack.commit();
//                }
            }
        });
        //--------------------------------------------------------------------------------end buttonAddActivity FAB button----


        //--------NaviigationDrawer actions pressed-------------------------------------------------------------------------------
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            FragmentTransaction transaction = manager.beginTransaction();
            AddActivityNavigationFragment addActivityNavigationFragment = new AddActivityNavigationFragment();
            ViewHistoryNavigationFragment viewHist = new ViewHistoryNavigationFragment();


            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // If user selects Add Activity
                if (menuItem.getItemId() == R.id.navAddActivity) {

                    //CASE 1 of Add Activity navigation press - it has not been pressed before.
                    //Show Add Activity fragment
                    if (welcomeScreen.isVisible()) {   //setting the fragments only to be replaced under this condition stops it from mult overlay since add its only on startup
                        menuItem.setChecked(true);
                        transaction.replace(R.id.main_act, addActivityNavigationFragment);  //use replace to prevent multiple overlay
                        transaction.commit();
                    }
                }

                    //If user selects View Graph

                if (menuItem.getItemId() == R.id.navViewGraph) {
                    //CASE 1 of View Graph navigation press - it has not been pressed before.
                    //Show ViewHistoryNavigationFragment
                    if (welcomeScreen.isVisible()) {   //setting the fragments only to be replaced under this condition stops it from mult overlay since add its only on startup
                        menuItem.setChecked(true);
                        transaction.replace(R.id.main_act, viewHist);  //use replace to prevent multiple overlay
                        transaction.commit();
                    }
                }

                //TODO: Add functionality for opening the drawer view outside of on startup
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    // Override this method to implement back button functionality for fragments
    public void onBackPressed() {

        // keep track of count or behavior will be that fragment will go back THEN close out of the app
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            getFragmentManager().popBackStack();
        }
        else {
            getFragmentManager().popBackStack();
        }

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

        //Make menu in support action bar clickable to open navigation drawer
        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}