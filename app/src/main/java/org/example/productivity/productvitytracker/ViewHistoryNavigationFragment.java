package org.example.productivity.productvitytracker;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Fragment that lets users choose whether they want to view thier productive or unproductive
 * activity history.
 */
public class ViewHistoryNavigationFragment extends Fragment implements View.OnClickListener {

    View view;

    //Needed UI components
    TextView viewHistoryText;
    Button viewProdbtn, viewUNprodbtn;
    public static boolean isProductiveGraph;   //graph type for GraphActivity

    public ViewHistoryNavigationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_history_navigation, container, false);

        //Associate
        viewHistoryText = (TextView) view.findViewById(R.id.viewHistoryText);
        viewProdbtn = (Button) view.findViewById(R.id.prodViewHistoryBtn);
        viewUNprodbtn = (Button) view.findViewById(R.id.UNprodViewHistoryBtn);

        //Set needed onClickListeners
        viewProdbtn.setOnClickListener(this);
        viewUNprodbtn.setOnClickListener(this);
        return view;
    }

    @Override
    // Functionality for adding a TimeModule. Need to add functionality via implementing OnClickListener in this class to resolve scope errors
    public void onClick(View v) {

        //TODO differentiate between productive and UNproductive
        switch (v.getId()) {
            // User wants to view productive history
            case R.id.prodViewHistoryBtn:
                // Use object of intent class to open GraphActivity since it is of type activity
                Intent prodGraphIntent = new Intent("org.example.productivity.productvitytracker.GraphActivity"); //this is the package of the new activity
                startActivity(prodGraphIntent);
                isProductiveGraph = true;   //set graph type
                break;

            //User wants to view UNproductive history
            case R.id.UNprodViewHistoryBtn:
                // Use object of intent class to open GraphActivity since it is of type activity
                Intent UNprodGraphIntent = new Intent("org.example.productivity.productvitytracker.GraphActivity"); //this is the package of the new activity
                startActivity(UNprodGraphIntent);
                isProductiveGraph = false;  //set graph type
                break;
        }
    }

    // getGraphProdStatus() is used to access global isProductiveGraph boolean variable to communicate to GraphActivity whether user wants
    // ro see a productive or unproductive graph
    //public static boolean getGraphProdStatus() {
       // return isProductiveGraph;
    //}
}
