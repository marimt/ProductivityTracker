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

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);

        GraphView lineGraph = (GraphView) findViewById(R.id.linegraph);

        //Get isProductiveGraph boolean status to use either productive or UNproductive methods
        ViewHistoryNavigationFragment viewHNFstatus = new ViewHistoryNavigationFragment();
        boolean isItProductiveGraph = viewHNFstatus.getGraphProdStatus();


        //Customize
        Viewport viewport = lineGraph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinY(0);    //assume no negative times
        viewport.setMaxY(24);   //24 hours is max time. Reasoning: 24 hrs/day
        viewport.setMinX(0);    //assume no negative dates
        viewport.setMaxX(10);   //for now display max 10 points
        viewport.setScrollable(true);

        if (isItProductiveGraph) {
            //Get user data
            ArrayList<Double> extractedProdDurationData = ProcessProductiveDurationData();
            Log.v("CONTENTS", extractedProdDurationData.toString());
            ArrayList<Integer> extractedProdDateData = ProcessProductiveDateData();

            int testXPos = 0;   //Will eventually move to ^ extractedProdDateData through adding a enum and/or insertion sort @ addModule
            int amntOfPoints = extractedProdDurationData.size();
            int yCoordArrayPosition = 0;

            //Go though data
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
            while (amntOfPoints != 0) {
                //Add point to graph
                DataPoint point = new DataPoint(testXPos, extractedProdDurationData.get(yCoordArrayPosition));
                series.appendData(point, true, 10);

                Log.v("PRODPOINT", "added" + extractedProdDurationData.get(yCoordArrayPosition));
                yCoordArrayPosition++;
                testXPos++;
                amntOfPoints--;
            }

            //draw graph when all points are added
            lineGraph.addSeries(series);


            Log.v("INFO", "read productive information");
        }

        if (!isItProductiveGraph) {
            //Get user data
            ArrayList<Double> extractedUNProdDurationData = ProcessUNproductiveDurationData();
            Log.v("CONTENTS", extractedUNProdDurationData.toString());
            ArrayList<Integer> extractedUNProdDateData = ProcessUNproductiveDateData();

            int testXPos = 0;   //Will eventually move to ^ extractedProdDateData through adding a enum and/or insertion sort @ addModule
            int amntOfPoints = extractedUNProdDurationData.size();
            int yCoordArrayPosition = 0;

            //Go though data
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
            while (amntOfPoints != 0) {
                //Add point to graph
                DataPoint point = new DataPoint(testXPos, extractedUNProdDurationData.get(yCoordArrayPosition));
                series.appendData(point, true, 20);

                Log.v("UNPRODPOINT", "added" + extractedUNProdDurationData.get(yCoordArrayPosition));
                yCoordArrayPosition++;
                testXPos++;
                amntOfPoints--;
            }

            //draw graph when all points are added
            lineGraph.addSeries(series);


            Log.v("INFO", "read unproductive information");
        }
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
    public String ReadProductiveModules() {
        String fileContents;
        String noFile = "no file was processed";

        StringBuffer stringBuffer = new StringBuffer(); // used to read strings

        try {
            // Read productive modules
            FileInputStream prodFileInputStream = openFileInput("productive_time_module_file");
            InputStreamReader prodInputStreamReader = new InputStreamReader(prodFileInputStream);
            BufferedReader prodBufferedReader = new BufferedReader(prodInputStreamReader);

            // First read file with productive time modules
            while ((fileContents = prodBufferedReader.readLine()) != null) {
                stringBuffer.append(fileContents + "\n");
                Log.v("READ", "opened and read productive file");
            }

            Log.v("CHECKPRODBUFFREAD", stringBuffer.toString());
            return stringBuffer.toString(); //displays as [TimeModule, TimeModule, ... , TimeModule]

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return noFile;
    }

    // Method to access and read UNproductive time modules from the device's internal storage
    public String ReadUNproductiveModules() {
        String fileContents;
        String noFile = "no file was processed";

        StringBuffer stringBuffer = new StringBuffer(); // used to read strings

        try {
            // Read UNproductive modules
            FileInputStream UNprodFileInputStream = openFileInput("UNproductive_time_module_file");
            InputStreamReader UNprodInputStreamReader = new InputStreamReader(UNprodFileInputStream);
            BufferedReader UNprodBufferedReader = new BufferedReader(UNprodInputStreamReader);

            // Then read file with UNproductive time modules
            while ((fileContents = UNprodBufferedReader.readLine()) != null) {
                stringBuffer.append(fileContents + "\n");
                Log.v("READ", "opened and read UNproductive file");
            }

            Log.v("CHECKUNPRODBUFFREAD", stringBuffer.toString());
            return stringBuffer.toString(); //displays as [TimeModule, TimeModule, ... , TimeModule]

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return noFile;
    }

    // Method to process raw data obtained from ReadProductiveModules() and retrieve the productive durations
    public ArrayList<Double> ProcessProductiveDurationData() {
        //State of Data at this point is [TimeModule{duration=DOUBLE, date='STRING', typeOfActivity='STRING', isProductive=BOOLEAN}]
        //In order to graph data we need to extract duration as duration will be the Y-Axis.

        String processedData;
        processedData = ReadProductiveModules();
        ArrayList<Double> productiveDurations = new ArrayList<>();

        for (int i = 0; i < processedData.length(); i++) {  //iterate through characters in stored time modules file
            // Extract duration. To do this look for the sequence of characters [i,o,n,=] that point towards a duration coming up.
            if (processedData.charAt(i) == 'i' && processedData.charAt(i + 1) == 'o' && processedData.charAt(i + 2) == 'n' && processedData.charAt(i + 3) == '=') { //TODO make this a more rigorous check
                //A duration can is at least X.XX and at most XX.XX
                String durationEntryString = "" + processedData.charAt(i + 4) + processedData.charAt(i + 5) + processedData.charAt(i + 6);  // The last position the decimal can appear is the 3rd position
                if (processedData.charAt(i + 7) != ',' && processedData.charAt(i + 7) != ' ' && processedData.charAt(i + 7) != 'd') //check for [ \, , d, a ] since they are next possible characters
                    durationEntryString += processedData.charAt(i + 7);
                if (processedData.charAt(i + 8) != ',' && processedData.charAt(i + 8) != ' ' && processedData.charAt(i + 8) != 'd')
                    durationEntryString += processedData.charAt(i + 8); // XX.XX/24.00 is limit.

                productiveDurations.add(Double.parseDouble(durationEntryString));

                Log.v("PRODDURATIONCHECK", durationEntryString);
                Log.v("PRODDURATIONARRAYLIST", productiveDurations.toString());
            }
        }
        return productiveDurations;
    }

    // Method to process raw data obtained from ReadProductiveModules() and retrieve the productive dates
    public ArrayList<Integer> ProcessProductiveDateData() {
        //State of Data at this point is [TimeModule{duration=DOUBLE, date='STRING', typeOfActivity='STRING', isProductive=BOOLEAN}]
        //In order to graph data we need to extract date as date will be the X-Axis.

        String processedData;
        processedData = ReadProductiveModules();
        ArrayList<Integer> productiveDates = new ArrayList<>();

        for (int i = 0; i < processedData.length(); i++) {  //iterate through characters in stored time modules file
            // Extract date. To do this look for the sequence of characters [t,e,=, '] that point towards a date coming up.
            if (processedData.charAt(i) == 't' && processedData.charAt(i + 1) == 'e' && processedData.charAt(i + 2) == '=' && processedData.charAt(i + 3) == '\'') { //TODO make this a more rigorous check
                // The format for a date is constrained to MMDDYYYY
                String dateEntryString = "";

                for (int dateIndex = (i + 4); dateIndex <= (i + 11); dateIndex++) {
                    if (processedData.charAt(dateIndex) != ',' && processedData.charAt(dateIndex) != ' ' && processedData.charAt(dateIndex) != '\'')
                        dateEntryString += processedData.charAt(dateIndex);
                }

                //productiveDates.add(Integer.parseInt(dateEntryString));

                //Log.v("PRODDATECHECK", dateEntryString);
                Log.v("PRODDATEARRAYLIST", productiveDates.toString());
            }
        }
        return productiveDates;
    }

    // Method to process raw data obtained from ReadProductiveModules() and retrieve the UNproductive durations
    public ArrayList<Double> ProcessUNproductiveDurationData() {
        //State of Data at this point is [TimeModule{duration=DOUBLE, date='STRING', typeOfActivity='STRING', isProductive=BOOLEAN}]
        //In order to graph data we need to extract duration as duration will be the Y-Axis.

        String processedData;
        processedData = ReadUNproductiveModules();
        ArrayList<Double> UNproductiveDurations = new ArrayList<>();

        for (int i = 0; i < processedData.length(); i++) {  //iterate through characters in stored time modules file
            // Extract duration. To do this look for the sequence of characters [i,o,n,=] that point towards a duration coming up.
            if (processedData.charAt(i) == 'i' && processedData.charAt(i + 1) == 'o' && processedData.charAt(i + 2) == 'n' && processedData.charAt(i + 3) == '=') { //TODO make this a more rigorous check
                //A duration can is at least X.XX and at most XX.XX
                String durationEntryString = "" + processedData.charAt(i + 4) + processedData.charAt(i + 5) + processedData.charAt(i + 6);  // The last position the decimal can appear is the 3rd position
                if (processedData.charAt(i + 7) != ',' && processedData.charAt(i + 7) != ' ' && processedData.charAt(i + 7) != 'd') //check for [ \, , d, a ] since they are next possible characters
                    durationEntryString += processedData.charAt(i + 7);
                if (processedData.charAt(i + 8) != ',' && processedData.charAt(i + 8) != ' ' && processedData.charAt(i + 8) != 'd')
                    durationEntryString += processedData.charAt(i + 8); // XX.XX is limit. This is in hours and seems reasonable to limit at 99.99 hours for an activity.

                UNproductiveDurations.add(Double.parseDouble(durationEntryString));

                Log.v("PRODDURATIONCHECK", durationEntryString);
                Log.v("PRODDURATIONARRAYLIST", UNproductiveDurations.toString());
            }
        }
        return UNproductiveDurations;
    }

    // Method to process raw data obtained from ReadProductiveModules() and retrieve the UNproductive dates
    public ArrayList<Integer> ProcessUNproductiveDateData() {
        //State of Data at this point is [TimeModule{duration=DOUBLE, date='STRING', typeOfActivity='STRING', isProductive=BOOLEAN}]
        //In order to graph data we need to extract date as date will be the X-Axis.

        String processedData;
        processedData = ReadUNproductiveModules();
        ArrayList<Integer> UNproductiveDates = new ArrayList<>();

        for (int i = 0; i < processedData.length(); i++) {  //iterate through characters in stored time modules file
            // Extract date. To do this look for the sequence of characters [t,e,=, '] that point towards a date coming up.
            if (processedData.charAt(i) == 't' && processedData.charAt(i + 1) == 'e' && processedData.charAt(i + 2) == '=' && processedData.charAt(i + 3) == '\'') { //TODO make this a more rigorous check
                // The format for a date is constrained to MMDDYYYY
                String dateEntryString = "";

                for (int dateIndex = (i + 4); dateIndex <= (i + 11); dateIndex++) {
                    dateEntryString += processedData.charAt(dateIndex);
                }

                //UNproductiveDates.add(Integer.parseInt(dateEntryString));

                Log.v("UNPRODDATECHECK", dateEntryString);
                //Log.v("UNPRODDATEARRAYLIST", UNproductiveDates.toString());
            }
        }
        return UNproductiveDates;
    }
}
