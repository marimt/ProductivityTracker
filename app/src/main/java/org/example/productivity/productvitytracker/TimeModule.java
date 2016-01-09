package org.example.productivity.productvitytracker;

/** TimeModule class stores each time input with needed values: length, date, and type of activity.
 *  A TimeModule can either be productive or unproductive depending on the button and respective
 *  fragment for input.
 **/

public class TimeModule {

    //Needed Variables
    private double duration;
    private String date, typeOfActivity;    //typeOfActivity e.g. homework, facebook, etc
    private boolean isProductive;


    public TimeModule(double duration, String date, String typeOfActivity, boolean isProductive) {
        this.duration = duration;
        this.date = date;
        this.typeOfActivity = typeOfActivity;
        this.isProductive = isProductive;
    }

    public double getDuration() {
        return duration;
    }

    public boolean isProductive() {
        return isProductive;
    }

    public String getDate() {
        return date;
    }

    public String getTypeOfActivity() {
        return typeOfActivity;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public void setTypeOfActivity(String typeOfActivity) {
        this.typeOfActivity = typeOfActivity;
    }

    public void setIsProductive(boolean isProductive) {
        this.isProductive = isProductive;
    }

    @Override
    public String toString() {
        //TODO EXTRA: This is not a good way to seperate the variables. GraphActivity ProcessData() depends on this format --> when fix make sure still works.
        return "TimeModule{" +
                "duration=" + duration +
                ", date='" + date + '\'' +
                ", typeOfActivity='" + typeOfActivity + '\'' +
                ", isProductive=" + isProductive +
                '}';
    }
}

