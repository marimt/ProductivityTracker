package org.example.productivity.productvitytracker;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The fragment user goes to after clicking Productive or Unproductive button to add an activity.
 */

public class AddModuleFragment extends Fragment implements View.OnClickListener{

    View view;
    //Array list to store all TimeModules
    ArrayList<TimeModule> productiveTimeModulesArrayList = new ArrayList<>();
    ArrayList<TimeModule> unproductiveTimeModulesArrayList = new ArrayList<>();

    //Needed UI components
    Button finishedInputBtn;
    EditText hours, minutes, month, day, year, activityType;

    public AddModuleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.add_module_fragment, container, false);

        //Associate EditText fields and Buttons with layout


        hours = (EditText) view.findViewById(R.id.editDurationHours);
        minutes = (EditText) view.findViewById(R.id.editDurationMin);
        month = (EditText) view.findViewById(R.id.editMonth);
        day = (EditText) view.findViewById(R.id.editDay);
        year = (EditText) view.findViewById(R.id.editYear);
        activityType = (EditText) view.findViewById(R.id.editActivityType);
        finishedInputBtn =  (Button) view.findViewById(R.id.finishedInputBtn);

        //Set needed OnClickListeners
        finishedInputBtn.setOnClickListener(this);

        return view;
    }


    @Override
    //Only using 1 case now which makes this switch pointless but set up just in case new buttons
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.finishedInputBtn:
                askForInput();
                //Log.v("TEST", "switch1 worked"); //test
                break;
        }
    }


    // askForInput() takes in user input and stores it in TimeModule array list.
    public void askForInput() {

        final String LOG_TAG2 = "DISPLAY";
        final int hoursToMinutesConversion = 60;


        // Get input from editText fields.
        int hrInput, minInput, monthInput, dayInput, yearInput;
        String actTypeInput;
        // Don't parse input if field is empty as it will cause app to crash. Note they are in the ordered to field
        // positions so that the toast messages show up in a good order.
        // TODO maybe make a class to check parse but not really worth since message changes
        //ACTIVITY TYPE
        if (activityType == null || activityType.getText().toString().matches("")) {
            Toast.makeText(getActivity(), "You forgot to add the activity", Toast.LENGTH_SHORT).show();
            return;
        }
        else { actTypeInput = activityType.getText().toString(); }
        //HOURS
        if (hours == null || hours.getText().toString().matches("")) {
            Toast.makeText(getActivity(), "You forgot to add how many hours", Toast.LENGTH_SHORT).show();
            return;
        }
        else { hrInput = Integer.parseInt(hours.getText().toString()); }
        //MINUTES
        if (minutes == null || minutes.getText().toString().matches("")) {
            Toast.makeText(getActivity(), "You forgot to add how many minutes", Toast.LENGTH_SHORT).show();
            return;
        }
        else { minInput = Integer.parseInt(minutes.getText().toString()); }
        //MONTHS
        if (month == null || month.getText().toString().matches("")) {
            Toast.makeText(getActivity(), "You forgot to the month", Toast.LENGTH_SHORT).show();
            return;
        }
        else { monthInput = Integer.parseInt(month.getText().toString()); }
        //DAYS
        if (day == null || day.getText().toString().matches("")) {
            Toast.makeText(getActivity(), "You forgot to add what day", Toast.LENGTH_SHORT).show();
            return;
        }
        else { dayInput = Integer.parseInt(day.getText().toString()); }
        //YEAR
        if (year == null || year.getText().toString().matches("")) {
            Toast.makeText(getActivity(), "You forgot to add the year", Toast.LENGTH_SHORT).show();
            return;
        }
        else { yearInput = Integer.parseInt(year.getText().toString()); }

        //TODO clear the fields after each new entry i.e button click

        //To get whether a module is productive or unproductive access global productivity status
        //TODO change this implementation since going through a global variable is not ideal
        MainActivity productivityStatus = new MainActivity();
        boolean isItProductive = productivityStatus.isProductive();


        //Process needed values for storing in a TimeModule
        int duration = hrInput * hoursToMinutesConversion + minInput;
        String date = Integer.toString(monthInput) + "/" + Integer.toString(dayInput) + "/" + Integer.toString(yearInput);
        //TODO change to use android specific process to pick from calender and process as a date, same for duration


        TimeModule newTimeModule = new TimeModule(duration, date, actTypeInput, isItProductive);

        if (isItProductive) {
            productiveTimeModulesArrayList.add(newTimeModule);
            Log.v("STATUS", "added to productive list");
        }
        else if (!isItProductive) {
            unproductiveTimeModulesArrayList.add(newTimeModule);
            Log.v("STATUS", "added to UNproductive list");
        }

        Log.v(LOG_TAG2, newTimeModule.toString());
        Log.v("TEST", "finished adding to a array list");

    }
}
