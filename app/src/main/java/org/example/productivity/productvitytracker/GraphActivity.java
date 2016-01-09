package org.example.productivity.productvitytracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
    This class works by using the Graph View library to plot the different activities on the X axis chronologically
     and the amount of time (in hours) each activity took on the Y axis. E.g. activity 1 was completed 8/05/2015
     for 3 hours and activity 2 was completed 8/06/2015 for 1 hour so the points plotted would be (1, 3) and (2, 1).
     The functions with viewport and series here are library functions of Graph View. Viewport allows us to modify
     the way the graph is displayed and series is used for plotting points.

     ProcessDateData and ProcessDurationData are functions made to extract information from a text file (which is read by ReadModules)
     of activity input from the phone's device. This information is written without special formatting so we look for key words that
     will signal the information we want. I.e. for ProcessDateData we look for "ion=" becuase that points to a duration coming up
     and activities are stored as time module arrays with ativity time being 'duration=[TIME]' These are put into arrays and plotted
     as explained in the above paragraph.
 */

public class GraphActivity extends AppCompatActivity {

    final String LOG_TAG = "ERRORS";

    //Set max X and Y points to display. These can be changed according to how much data needs to be displayed.
    public final int maxXpoints = 10;  //for now display max 10 points for the X axis
    public final int maxYpoints =  24; //24 hours is max time and time is y axis. Reasoning: 24 hrs/day

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);

        GraphView lineGraph = (GraphView) findViewById(R.id.linegraph);

        //Get isProductiveGraph boolean status to use either productive or UNproductive methods
        ViewHistoryNavigationFragment viewHNFstatus = new ViewHistoryNavigationFragment();
        boolean productiveStatus = viewHNFstatus.isProductiveGraph;


        //Customize
        Viewport viewport = lineGraph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinY(0);    //assume no negative times
        viewport.setMaxY(maxYpoints);
        viewport.setMinX(0);    //assume no negative dates
        viewport.setMaxX(maxXpoints);
        viewport.setScrollable(true);

        ArrayList<Double> extractedDurationData;
        ArrayList<Integer> extractedDateData;

        extractedDurationData = ProcessDurationData();
        extractedDateData = ProcessDateData();

        Log.v("CONTENTS", extractedDurationData.toString());
        int testXPos = 0;   //Will eventually move to ^ extractedProdDateData through adding a enum and/or insertion sort @ addModule
        int amntOfPoints = extractedDurationData.size();
        int yCoordArrayPosition = 0;

        //Go though data
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        while (amntOfPoints != 0) {
            //Add point to graph
            DataPoint point = new DataPoint(testXPos, extractedDurationData.get(yCoordArrayPosition));
            series.appendData(point, true, maxXpoints);

            Log.v("GRAPHPOINT", "added" + extractedDurationData.get(yCoordArrayPosition));
            yCoordArrayPosition++;
            testXPos++;
            amntOfPoints--;
        }

        //draw graph when all points are added
        lineGraph.addSeries(series);
        Log.v("INFO", "read unproductive information");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
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

    // Method to access and read productive time modules from the device's internal storage
    public String ReadModules(boolean productiveStatus) {
        String fileContents;
        String noFile = "no file was processed";

        StringBuilder stringBuilder = new StringBuilder(); // used to read strings

        try {
            // Read productive modules
            FileInputStream fileInputStream;

            if (productiveStatus)
                fileInputStream = openFileInput("productive_time_module_file");
            else
                fileInputStream = openFileInput("UNproductive_time_module_file");

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // First read file with productive time modules
            while ((fileContents = bufferedReader.readLine()) != null) {
                stringBuilder.append(fileContents + "\n");
                Log.v("READ", "opened and read file");
            }

            Log.v("CHECKBUFFREAD", stringBuilder.toString());
            return stringBuilder.toString(); //displays as [TimeModule, TimeModule, ... , TimeModule]

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "File was not found");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "There was an error reading the file");
        }
        return noFile;
    }

    // Method to process raw data obtained from ReadProductiveModules() and retrieve the UNproductive durations
    public ArrayList<Double> ProcessDurationData() {
        //State of Data at this point is [TimeModule{duration=DOUBLE, date='STRING', typeOfActivity='STRING', isProductive=BOOLEAN}]
        //In order to graph data we need to extract duration as duration will be the Y-Axis.

        String processedData;
        ArrayList<Double> activityDurations = new ArrayList<>();

        //Get whether it was a productive activity or not
        ViewHistoryNavigationFragment viewHNFstatus = new ViewHistoryNavigationFragment();
        boolean productiveStatus = viewHNFstatus.isProductiveGraph;

        if (productiveStatus)
            processedData = ReadModules(true);
        else
            processedData = ReadModules(false);

        for (int i = 0; i < processedData.length(); i++) {  //iterate through characters in stored time modules file
            // Extract duration. To do this look for the sequence of characters [i,o,n,=] that point towards a duration coming up.
            if (processedData.charAt(i) == 'i' && processedData.charAt(i + 1) == 'o' && processedData.charAt(i + 2) == 'n' && processedData.charAt(i + 3) == '=') { //TODO make this a more rigorous check
                //A duration can is at least X.XX and at most XX.XX
                String durationEntryString = "" + processedData.charAt(i + 4) + processedData.charAt(i + 5) + processedData.charAt(i + 6);  // The last position the decimal can appear is the 3rd position

                if (processedData.charAt(i + 7) != ',' && processedData.charAt(i + 7) != ' ' && processedData.charAt(i + 7) != 'd') //check for [ \, , d, a ] since they are next possible characters
                    durationEntryString += processedData.charAt(i + 7);
                if (processedData.charAt(i + 8) != ',' && processedData.charAt(i + 8) != ' ' && processedData.charAt(i + 8) != 'd')
                    durationEntryString += processedData.charAt(i + 8); // XX.XX is limit. This is in hours and seems reasonable to limit at 99.99 hours for an activity.

                activityDurations.add(Double.parseDouble(durationEntryString));

                Log.v("DURATIONCHECK", durationEntryString);
                Log.v("DURATIONARRAYLIST", activityDurations.toString());
            }
        }
        return activityDurations;
    }

    // Method to process raw data obtained from ReadProductiveModules() and retrieve the UNproductive dates
    public ArrayList<Integer> ProcessDateData() {
        //State of Data at this point is [TimeModule{duration=DOUBLE, date='STRING', typeOfActivity='STRING', isProductive=BOOLEAN}]
        //In order to graph data we need to extract date as date will be the X-Axis.

        String processedData;

        //Get whether it was a productive activity or not
        ViewHistoryNavigationFragment viewHNFstatus = new ViewHistoryNavigationFragment();
        boolean productiveStatus = viewHNFstatus.isProductiveGraph;

        if (productiveStatus)
            processedData = ReadModules(true);
        else
            processedData = ReadModules(false);

        ArrayList<Integer> activityDates = new ArrayList<>();

        for (int i = 0; i < processedData.length(); i++) {  //iterate through characters in stored time modules file
            // Extract date. To do this look for the sequence of characters [t,e,=, '] that point towards a date coming up.
            if (processedData.charAt(i) == 't' && processedData.charAt(i + 1) == 'e' && processedData.charAt(i + 2) == '=' && processedData.charAt(i + 3) == '\'') { //TODO make this a more rigorous check
                // The format for a date is constrained to MMDDYYYY
                String dateEntryString = "";

                for (int dateIndex = (i + 4); dateIndex <= (i + 11); dateIndex++) {
                    dateEntryString += processedData.charAt(dateIndex);
                }

                //activityDates.add(Integer.parseInt(dateEntryString));

                Log.v("DATECHECK", dateEntryString);
                //Log.v("DATEARRAYLIST", UNproductiveDates.toString());
            }
        }
        return activityDates;
    }

}
