package org.example.productivity.productvitytracker;


import android.app.Fragment;
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
public class ViewHistoryNavigationFragment extends Fragment {

    View view;

    //Needed UI components
    TextView viewHistoryText;
    Button viewProdbtn, viewUNprodbtn;

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

        //TODO set onClickListeners for this, follow either mainactivity way or addModuleFragment way

        return view;
    }



}
