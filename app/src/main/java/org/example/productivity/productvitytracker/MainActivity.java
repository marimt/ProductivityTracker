package org.example.productivity.productvitytracker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    //Toolbar toolbar;

    //Needed UI components
    EditText hours, minutes, month, day, year, activityType;    //for AddModuleFragment

    //=========================================================================================================================================
    //=========================================================================================================================================
    /**
     * 7) prettify
     * 9) fix bugs in to dos
     * 8) test and port out yo
     * Note: bugs not critical for demo purposes and additional features on Trello board.
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

        //TODO fix the toolbar
        //toolbar = (Toolbar) findViewById(R.id.toolbar);


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
        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonAddActivity)   //sub-button
                .addSubActionView(buttonViewHistory) //sub-button
                .attachTo(floatingActionButton) //main button
                .build();
        //TODO the sub buttons don't close when user presses one. fix that.

        final AddActivityNavigationFragment addActivityNavigationFragment = new AddActivityNavigationFragment();
        //Set listeners for FAB sub-buttons
        //--------buttonAddActivity FAB sub-button------------------------------------------------------------
        buttonAddActivity.setOnClickListener(new View.OnClickListener() {

            FragmentTransaction transaction = manager.beginTransaction();
            FragmentTransaction transactionChange = manager.beginTransaction();
            FragmentTransaction transactionChangeBack = manager.beginTransaction();
            FragmentTransaction transactionChange2 = manager.beginTransaction();

            ViewHistoryNavigationFragment viewHist = new ViewHistoryNavigationFragment();

            @Override
            public void onClick(View v) {
                //CASE 1 of buttonAddActivity button press - it has not been pressed before.
                //Show AddActivityNavigationFragment
                if (welcomeScreen.isVisible()) {   //setting the fragments only to be replaced under this condition stops it from mult overlay since add its only on startup
                    transaction.replace(R.id.main_act, addActivityNavigationFragment);  //use replace to prevent multiple overlay
                    transaction.commit();
                }

                //CASE 2 of buttonAddActivity button pressed while addActNav fragment is already visible
                if (v == buttonAddActivity && addActivityNavigationFragment.isVisible()) {
                    Toast.makeText(getApplicationContext(), "Please finish adding the current activity", Toast.LENGTH_SHORT).show();
                }

                //CASE 3 - buttonViewHistory is pressed and addActNav is visible
                buttonViewHistory.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v2) {
                        if (addActivityNavigationFragment.isVisible()) {
                            transactionChange.replace(R.id.main_act, viewHist);
                            transactionChange.commit();
                        }
                        //TODO known bug, having this fragment called twice will cause crash...maybe something about putting it back to stack?
//                        // CASE 4 - of buttonViewHistory press - it is pressed while addActivityNavigationFragment is visible
//                        if (v2 == buttonViewHistory && addActivityNavigationFragment.isVisible()){
//                            Toast.makeText(getApplicationContext(), "testing back twice", Toast.LENGTH_SHORT).show();
//                            transactionChange2.replace(R.id.main_act, viewHist);
//                           transactionChange2.commit();
//                        }
                    }
                });

                // CASE 5 - buttonAddActivity is pressed while viewHist fragment is visible
                if (v == buttonAddActivity && viewHist.isVisible()) {
                    transactionChangeBack.replace(R.id.main_act, addActivityNavigationFragment);
                    transactionChangeBack.commit();
                }
            }
        });
        //--------------------------------------------------------------------------------end buttonAddActivity FAB sub-button----


        //--------buttonViewHistory FAB sub-button------------------------------------------------------------
        buttonViewHistory.setOnClickListener(new View.OnClickListener() {
            FragmentTransaction transaction = manager.beginTransaction();
            FragmentTransaction transactionChange = manager.beginTransaction();
            FragmentTransaction transactionChangeBack = manager.beginTransaction();
//            FragmentTransaction transactionChange2 = manager.beginTransaction();
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

                //CASE 2 - buttonAddActivity is pressed while viewHist is up
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
                        //TODO final case where user has already switched framenets and wants to switch back - known bug
//                        // CASE 4 - buttonAddActivity is pressed while viewHist is up
//                        if (v2 == buttonAddActivity && viewHist.isVisible()) {
//                            Toast.makeText(getApplicationContext(), "changeeee", Toast.LENGTH_SHORT).show();
//                            //transactionChange2.replace(R.id.main_act, addActivityNavigationFragment);
//                            //transactionChange2.commit();
//                        }
                    }
                });

                // CASE 3 - of buttonViewHistory press - it is pressed while addActivityNavigationFragment is up
                if (v == buttonViewHistory && addActivityNavigationFragment.isVisible()){
                    transactionChangeBack.replace(R.id.main_act, viewHist);
                    transactionChangeBack.commit();
                }
            }
        });
        //--------------------------------------------------------------------------------end buttonViewActivity FAB sub-button----
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

        return super.onOptionsItemSelected(item);
    }
}
