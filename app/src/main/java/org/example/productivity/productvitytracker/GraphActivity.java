package org.example.productivity.productvitytracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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
                for(int i = 0; i < 100; i++) {
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
}
