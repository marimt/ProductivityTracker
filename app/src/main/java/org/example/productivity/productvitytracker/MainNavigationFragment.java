package org.example.productivity.productvitytracker;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainNavigationFragment extends Fragment implements View.OnClickListener {

    View view;

    //Needed UI components
    Button addProdActivity, addUNprodActivity, viewProdHistory, viewUNprodHistory;


    public MainNavigationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_navigation_fragment, container, false);

        //Associate fields with layout
        addProdActivity = (Button) view.findViewById(R.id.prodAddBtn);
        addUNprodActivity = (Button) view.findViewById(R.id.unprodAddBtn);
        viewProdHistory = (Button) view.findViewById(R.id.prodGraphBtn);
        viewUNprodHistory = (Button) view.findViewById(R.id.unprodGraphBtn);

        //Set needed OnClickListeners
        addProdActivity.setOnClickListener(this);
        addUNprodActivity.setOnClickListener(this);
        viewProdHistory.setOnClickListener(this);
        viewUNprodHistory.setOnClickListener(this);

        return view;
    }

    //TODO fill out cases
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.prodAddBtn:
                //
                break;

            case R.id.unprodAddBtn:
                //
                break;

            case R.id.prodGraphBtn:
                //
                break;

            case R.id.unprodGraphBtn:
                //
                break;
        }
    }
}
