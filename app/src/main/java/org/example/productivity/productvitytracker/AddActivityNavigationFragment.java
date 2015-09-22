package org.example.productivity.productvitytracker;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Fragment that lets users choose whether they want to add a productive or unproductive activity.
 */
public class AddActivityNavigationFragment extends Fragment {

    View view;

    //Needed UI components
    TextView addActivityText;
    Button addProdbtn, addUNprodbtn;

    public AddActivityNavigationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_activity_navigation, container, false);

        //Associate
        addActivityText = (TextView) view.findViewById(R.id.addActText);
        addProdbtn = (Button) view.findViewById(R.id.prodAddBtn);
        addUNprodbtn = (Button) view.findViewById(R.id.UNprodAddBtn);

        //TODO set onClickListeners for this, follow either mainactivity way or addModuleFragment way

        return view;
    }



}
