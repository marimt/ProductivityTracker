package org.example.productivity.productvitytracker;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Default welcome screen that shows on app startup. This fragments gets replaced with
 * navigation fragments for adding an activity or viewing user history depending on what
 * FAB button the user selects on home screen.
 */
public class WelcomeScreenFragment extends Fragment {


    View view;
    TextView homeWelcomeMssg;

    public WelcomeScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);

        //Associate
        homeWelcomeMssg = (TextView) view.findViewById(R.id.welcomeMssg);

        return view;
    }


}
