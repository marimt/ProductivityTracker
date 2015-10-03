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
import java.util.Random;

public class GraphActivity extends AppCompatActivity {


    private static final Random RANDOM = new Random();
    private int lastX = 0;
    private LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);

        GraphView prodLineGraph = (GraphView) findViewById(R.id.lineprodgraph);

        //Data
        series = new LineGraphSeries<DataPoint>();
        prodLineGraph.addSeries(series);

        //TODO learn and customize graph more
        //Customize
        Viewport viewport = prodLineGraph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMaxY(0);
        viewport.setMaxY(10);
        viewport.setScrollable(true);

        //Crashes application...another scope resolution error...
        Log.v("GRAPH", ReadProductiveModules());
        ProcessProductiveData();
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

    //TODO go over this and see if is neccessary
    @Override
    protected void onResume() {
        super.onResume();
        //simulate real time with thread that appends data to the the graph
        new Thread(new Runnable() {
            @Override
            public void run() {
                //add 100 new entries
                for (int i = 0; i < 100; i++) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addEntry();
                        }
                    });
                    //sleep to slow down the addition of entries
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start(); //before this it was not showing any points on viewport
    }

    //TODO maybe put all this in a seperate class...
    private void addEntry() {
        // Display max 10 points on the viewport and we scroll to end
        series.appendData(new DataPoint(lastX++, RANDOM.nextDouble() * 10d), true, 10);
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

            //Log.v("READ", stringBuffer.toString());
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

            //Log.v("READ", stringBuffer.toString());
            return stringBuffer.toString(); //displays as [TimeModule, TimeModule, ... , TimeModule]

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return noFile;
    }

    // Method to process raw data obtained from ReadProductiveModules()
    public void ProcessProductiveData() {
        //State of Data at this point is [TimeModule{duration=DOUBLE, date='STRING', typeOfActivity='STRING', isProductive=BOOLEAN}]
        //In order to graph data we need to extract date and duration. Date will be Y-Axis and Duration will be X-Axis.


        //TODO currently does not return anything...maybe have it return a 2D array that holds coordinates of graph. Single arrays will be combined to form 2D array with coords

        String processedData;
        processedData = ReadProductiveModules();
        ArrayList<Double> productiveDurations = new ArrayList<>();
        ArrayList<Integer> productiveDates = new ArrayList<>();

        for (int i = 0; i < processedData.length(); i++) {  //iterate through characters in stored time modules file
            // Extract duration. To do this look for the sequence of characters [i,o,n,=] that point towards a duration coming up.
            if (processedData.charAt(i) == 'i' && processedData.charAt(i + 1) == 'o' && processedData.charAt(i + 2) == 'n' && processedData.charAt(i + 3) == '=') { //TODO make this a more rigorous check
                //A duration can is at least X.XX and at most XX.XX
                String durationEntryString = "" + processedData.charAt(i + 4) + processedData.charAt(i + 5) + processedData.charAt(i + 6);  // The last position the decimal can appear is the 3rd position
                if (processedData.charAt(i + 7) != ',' && processedData.charAt(i + 7) != ' ' && processedData.charAt(i + 7) != 'd') //check for [ \, , d, a ] since they are next possible characters
                    durationEntryString += processedData.charAt(i + 7);
                if (processedData.charAt(i + 8) != ',' && processedData.charAt(i + 8) != ' ' && processedData.charAt(i + 8) != 'd')
                    durationEntryString += processedData.charAt(i + 8); // XX.XX is limit. This is in hours and seems reasonable to limit at 99.99 hours for an activity.


                productiveDurations.add(Double.parseDouble(durationEntryString));

                Log.v("PRODCHECK", durationEntryString);
                Log.v("PRODARRAYLIST", productiveDurations.toString());
            }

            // Extract date. To do this look for the sequence of characters [t,e,=, '] that point towards a date coming up.
            if (processedData.charAt(i) == 't' && processedData.charAt(i + 1) == 'e' && processedData.charAt(i + 2) == '=' && processedData.charAt(i + 3) == '\'') { //TODO make this a more rigorous check
                // The format for a date is constrained to MMDDYYYY
                String dateEntryString = "";
                for (int dateIndex = (i + 4); dateIndex <= (i + 11); dateIndex++) {
                    dateEntryString += processedData.charAt(dateIndex);
                }

                productiveDates.add(Integer.parseInt(dateEntryString));

                Log.v("PRODCHECK", dateEntryString);
                Log.v("PRODARRAYLIST", productiveDurations.toString());
            }
        }
    }

    // Method to process raw data obtained from ReadUNproductiveModules()
    public void ProcessUNproductiveData() {
        //State of Data at this point is [TimeModule{duration=DOUBLE, date='STRING', typeOfActivity='STRING', isProductive=BOOLEAN}]
        //In order to graph data we need to extract date and duration. Date will be Y-Axis and Duration will be X-Axis.


        //TODO currently does not return anything...maybe have it return a 2D array that holds coordinates of graph. Single arrays will be combined to form 2D array with coords

        String processedData;
        processedData = ReadUNproductiveModules();
        ArrayList<Double> UNproductiveDurations = new ArrayList<>();
        ArrayList<Integer> UNproductiveDates = new ArrayList<>();

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

                Log.v("UNPRODCHECK", durationEntryString);
                Log.v("UNPRODARRAYLIST", UNproductiveDurations.toString());
            }

            // Extract date. To do this look for the sequence of characters [t,e,=, '] that point towards a date coming up.
            if (processedData.charAt(i) == 't' && processedData.charAt(i + 1) == 'e' && processedData.charAt(i + 2) == '=' && processedData.charAt(i + 3) == '\'') { //TODO make this a more rigorous check
                // The format for a date is constrained to MMDDYYYY
                String dateEntryString = "";
                for (int dateIndex = (i + 4); dateIndex <= (i + 11); dateIndex++) {
                    dateEntryString += processedData.charAt(dateIndex);
                }

                UNproductiveDates.add(Integer.parseInt(dateEntryString));

                Log.v("UNPRODCHECK", dateEntryString);
                Log.v("UNPRODARRAYLIST", UNproductiveDurations.toString());
            }
        }
    }
}
