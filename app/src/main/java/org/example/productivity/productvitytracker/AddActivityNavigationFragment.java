package org.example.productivity.productvitytracker;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Fragment that lets users choose whether they want to add a productive or unproductive activity.
 */
public class AddActivityNavigationFragment extends Fragment implements View.OnClickListener {

    View view;
    public static boolean isProductive;

    //Needed UI components
    TextView addActivityText;
    Button addProdbutton, addUNprodbutton;

    public AddActivityNavigationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_activity_navigation, container, false);

        //Associate
        addActivityText = (TextView) view.findViewById(R.id.addActText);
        addProdbutton = (Button) view.findViewById(R.id.prodAddBtn);
        addUNprodbutton = (Button) view.findViewById(R.id.UNprodAddBtn);

        //Set needed onClickListeners
        addProdbutton.setOnClickListener(this);
        addUNprodbutton.setOnClickListener(this);
        return view;
    }

    @Override
    // Functionality for adding a TimeModule. Need to add functionality via implementing OnClickListener in this class to resolve scope errors
    public void onClick(View v) {
        //Components to perform fragment activities and needed methods
        FragmentManager manager = getFragmentManager();
        AddModuleFragment addModFrag = new AddModuleFragment();
        FragmentTransaction addModTrans = manager.beginTransaction();

        switch (v.getId()) {
            case R.id.prodAddBtn:
                isProductive = true;    //set global productivity status as true each time productive button is clicked
//                addModTrans.replace(R.id.main_act, addModFrag);
//                addModTrans.commit();
                manager.beginTransaction().replace(R.id.main_act, addModFrag).addToBackStack("tag").commit();
                break;

            case R.id.UNprodAddBtn:
                isProductive = false;    //set global productivity status as false each time unproductive button is clicked
//                addModTrans.replace(R.id.main_act, addModFrag);
//                addModTrans.commit();
                manager.beginTransaction().replace(R.id.main_act, addModFrag).addToBackStack("tag").commit();
                break;
        }
    }

    // isProductive() is used to access global isProductive boolean variable to be store a TimeModule as
    // productive or unproductive in AddModuleFragment
    public static boolean isProductive() {
        return isProductive;
    }

}

