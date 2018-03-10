package com.werelit.neurolls.neurolls;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    //private TimePickerFragmentListener listener;
    private TextView tv;

    //sets local textView time
    public void timeView(TextView tv) {
        this.tv = tv;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create a new instance of TimePickerDialog
        TimePickerDialog tmd = new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

        return tmd;
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget

        //Set a message for user

        //Display the user changed time on TextView
        String hr = String.valueOf(hourOfDay);

        //if chose hour is 0 or midnight
        /*if (hourOfDay == 0) {
            hr = "24";
        }*/

        //formats minutes with leading zero
        String min = String.format(Locale.getDefault(), "%02d", minute);

        String time = hr + ":" + min;
        tv.setText(time);
        Log.d("ONTIMESET", "WORKING");
    }
}
