package com.appmagazine.nardoon;

/**
 * Created by nadia on 5/4/2017.
 */

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.TimePicker;

import com.appmagazine.nardoon.activities.EditAgahi;
import com.appmagazine.nardoon.activities.NewAgahi;


/**
 * A simple {@link Fragment} subclass.
 */
public class BeginTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget
        TextView tv = (TextView) getActivity().findViewById(R.id.txt_begin_time);
        //Set a message for user
        //Display the user changed time on TextView
        tv.setText( "شروع سفارش گیری از " + String.valueOf(hourOfDay) + ":"+ String.valueOf(minute) );
        NewAgahi.startTime = String.valueOf(hourOfDay) + ":"+ String.valueOf(minute) + ":"+ "00";
        EditAgahi.startTime = String.valueOf(hourOfDay) + ":"+ String.valueOf(minute) + ":"+ "00";
    }
}
