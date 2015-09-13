package org.example.productivity.productvitytracker;

/** TimeModule class stores each time input with needed values: length, date, and type of activity.
 *  A TimeModule can either be productive or unproductive depending on the button and respective
 *  fragment for input.
 **/

public class TimeModule {

    //Needed Variables
    float duration;
    String date, typeOfActivity;    //typeOfActivity e.g. homework, facebook, etc
    boolean isProductive;


    public TimeModule(int duration, String date, String typeOfActivity, boolean isProductive) {
        this.duration = duration;
        this.date = date;
        this.typeOfActivity = typeOfActivity;
        this.isProductive = isProductive;
    }

    public float getDuration() {
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
        return "TimeModule{" +
                "duration=" + duration +
                ", date='" + date + '\'' +
                ", typeOfActivity='" + typeOfActivity + '\'' +
                ", isProductive=" + isProductive +
                '}';
    }
}

