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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * The fragment user goes to after clicking Productive or Unproductive button to add an activity.
 */

public class AddModuleFragment extends Fragment implements View.OnClickListener{

    View view;
    public final String prodfileName = "productive_time_module_file";
    public final String UNprodfileName = "UNproductive_time_module_file";

    //Array list to store all TimeModules
    ArrayList<TimeModule> productiveTimeModulesArrayList = new ArrayList<>();
    ArrayList<TimeModule> UNproductiveTimeModulesArrayList = new ArrayList<>();

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
                //TODO clear fields after the button is pressed
                break;
        }
    }


    // askForInput() takes in user input and stores it in TimeModule array list and saves it via StoreModules() method
    public void askForInput() {

        final String LOG_TAG2 = "DISPLAY";
        final int hoursToMinutesConversion = 60;


        // Get input from editText fields.
        int hrInput, minInput, monthInput, dayInput, yearInput;
        String actTypeInput;
        // Don't parse input if field is empty as it will cause app to crash. Note they are in the ordered to field
        // positions so that the toast messages show up in a good order.

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

        //If all input is okay then notify user activity was added
        Toast.makeText(getActivity(), "Activity was added", Toast.LENGTH_SHORT).show();

        //TODO clear the fields after each new entry i.e button click --> do this in group method by editText.setText("") for each field

        //To get whether a module is productive or unproductive access global productivity status
        AddActivityNavigationFragment productivityStatus = new AddActivityNavigationFragment();
        boolean isItProductive = productivityStatus.isProductive();


        //Process needed values for storing in a TimeModule
        int duration = hrInput * hoursToMinutesConversion + minInput;
        String date = Integer.toString(monthInput) + "/" + Integer.toString(dayInput) + "/" + Integer.toString(yearInput);
        //TODO EXTRA GOAL: change to use android specific process to pick from calender and process as a date, same for duration


        TimeModule newTimeModule = new TimeModule(duration, date, actTypeInput, isItProductive);

        if (isItProductive) {
            productiveTimeModulesArrayList.add(newTimeModule);
            Log.v("STATUS", "added to productive list");
        }
        else if (!isItProductive) {
            UNproductiveTimeModulesArrayList.add(newTimeModule);
            Log.v("STATUS", "added to UNproductive list");
        }

        Log.v(LOG_TAG2, newTimeModule.toString());
        Log.v("TEST", "finished adding to a array list");

        StoreModules();
        ProcessModules();
    }

    // Method to store the data to internal device storage obtained from AddModuleFragment
    public void StoreModules() {

        //To get whether a module is productive or unproductive access global productivity status
        AddActivityNavigationFragment productivityStatus = new AddActivityNavigationFragment();
        boolean isItProductive = productivityStatus.isProductive();

        // Write to file
        try {
            // If it is a productive time module then save to file for productive time modules
            if(isItProductive) {
                FileOutputStream fileOutputStream = getActivity().openFileOutput(prodfileName, getActivity().MODE_PRIVATE);
                fileOutputStream.write(productiveTimeModulesArrayList.toString().getBytes());
                fileOutputStream.close();
                Log.v("SAVE", "saved productive activity");
            }

            // If it is a UNproductive time module then save to file for UNproductive time modules
            if(!isItProductive) {
                FileOutputStream fileOutputStream = getActivity().openFileOutput(UNprodfileName, getActivity().MODE_PRIVATE);
                fileOutputStream.write(UNproductiveTimeModulesArrayList.toString().getBytes());
                fileOutputStream.close();
                Log.v("SAVE", "saved UNproductive activity");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.v("SAVE", "did not save, error file was not found");

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("SAVE", "error did not save");
        }
    }

    // Method to access and read time modules from the device's internal storage
    public String ProcessModules(){
        String fileContents;
        String noFile = "no file was processed";

        StringBuffer stringBuffer = new StringBuffer(); // used to read strings

        try {
            //THIS READING TYPE RETURNS ALL MODULES (BOTH PROD AND UNPROD)
            // Read regardless of productive or UNproductive
            FileInputStream prodFileInputStream = getActivity().openFileInput("productive_time_module_file");
            FileInputStream UNprodFileInputStream = getActivity().openFileInput("UNproductive_time_module_file");
            InputStreamReader prodInputStreamReader = new InputStreamReader(prodFileInputStream);
            BufferedReader prodBufferedReader = new BufferedReader(prodInputStreamReader);
            InputStreamReader UNprodInputStreamReader = new InputStreamReader(UNprodFileInputStream);
            BufferedReader UNprodBufferedReader = new BufferedReader(UNprodInputStreamReader);

            // First read file with productive time modules
            while ((fileContents = prodBufferedReader.readLine()) != null) {
                stringBuffer.append(fileContents + "\n");
                Log.v("READ", "opened and read productive file");
            }

            // Then read file with UNproductive time modules
            while ((fileContents = UNprodBufferedReader.readLine()) != null) {
                stringBuffer.append(fileContents + "\n");
                Log.v("READ", "opened and read UNproductive file");
            }

            Log.v("READ", stringBuffer.toString());
            return stringBuffer.toString(); //displays as [TimeModule, TimeModule, ... , TimeModule]

            /**
             * Note that this method  returns 2 strings, one only with productive and one only with unproductive
             * need to investigate whether it is the method itself (don't think so) or the way it is being called.
             * It is what I want though...just unexpected
             * To test again view tag in LogCat and notice how the tag displays twice excluding opened notifications (expected once)
             */

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return noFile;
    }
}