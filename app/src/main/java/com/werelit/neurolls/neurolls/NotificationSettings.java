package com.werelit.neurolls.neurolls;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class NotificationSettings extends DialogFragment {
    private TextView tv;
    private NotificationSettingsListener listener;
    private Spinner spinner;
    public static final String PREFS_NAME = "MyPrefsFile";
    //private int count;
    private long delay;
    private boolean isForAdding = true;
    private String notifID;
    private String mediaName;
    private TextView etDate, etTime;
    private String days, time;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);
        //this.count = settings.getInt("count", 0);

        etDate = ((ViewMediaDetailsActivity) getContext()).findViewById(R.id.date_text_view);
        etTime = ((ViewMediaDetailsActivity) getContext()).findViewById(R.id.notif_time_text_view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_notification_settings, null);

        builder.setView(view)
                .setTitle("Notification Settings")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { //as of now, the dialog closes when pressed cancel

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { //this is where notif scheduling happens

                        days = spinner.getSelectedItem().toString();
                        time = tv.getText().toString();

                        String hrs = time.substring(0, time.indexOf(":"));
                        String mins = time.substring(time.indexOf(":") + 1);

                        //gets the current date and time
                        Calendar c = Calendar.getInstance();
                        int iYear = c.get(Calendar.YEAR);
                        int iMonths = c.get(Calendar.MONTH);
                        int iDays = c.get(Calendar.DAY_OF_MONTH);
                        int iHrs = c.get(Calendar.HOUR_OF_DAY);
                        int iMins = c.get(Calendar.MINUTE);
                        int iSecs = c.get(Calendar.SECOND);

                        //gets the sched date
                        String date = etDate.getText().toString();
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

                        try {
                            cal.setTime(sdf.parse(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        long yearsToMillis = TimeUnit.DAYS.toMillis((cal.get(Calendar.YEAR) - iYear) * c.getActualMaximum(Calendar.DAY_OF_YEAR));
                        long monthsToMillis = TimeUnit.DAYS.toMillis((cal.get(Calendar.MONTH) - iMonths) * c.getActualMaximum(Calendar.MONTH));
                        long daysToMillis = TimeUnit.DAYS.toMillis((cal.get(Calendar.DAY_OF_MONTH) - Integer.parseInt(days)) - iDays);

                        //converts selected date and time to milliseconds
                        long selectedMins = TimeUnit.MINUTES.toMillis(Integer.parseInt(mins));
                        long selectedHrs = TimeUnit.HOURS.toMillis(Integer.parseInt(hrs));

                        //converts current date and time to milliseconds
                        long currentHrs = TimeUnit.HOURS.toMillis(iHrs);
                        long currentMins = TimeUnit.MINUTES.toMillis(iMins);
                        long currentSecs = TimeUnit.SECONDS.toMillis(iSecs);

                        //computes for the notification delay
                        long minsToMillis = selectedMins - currentMins;
                        long hrsToMillis = selectedHrs - currentHrs;
                        long secsToMillis = 60 - currentSecs;

                        /*Log.d("DAYS", "" + daysToMillis);
                        Log.d("HRS", "" + hrsToMillis);
                        Log.d("MINS", "" + minsToMillis);
                        Log.d("YEARS", "" + yearsToMillis);
                        Log.d("MONTHS", "" + monthsToMillis);
                        Log.d("SECONDS", "" + secsToMillis);*/

                        delay = yearsToMillis + monthsToMillis + daysToMillis + minsToMillis + hrsToMillis + secsToMillis;
                        //Log.d("TOTAL", delay + "");

                        if (!isForAdding) {///////////////////////////////////////////////////////////////////////////////////
                            scheduleNotification(getNotification(mediaName, getContext()), delay, getContext());
                            ((ViewMediaDetailsActivity) getContext()).saveDateTime(date, time);
                            //Toast.makeText(getContext(), mediaName + " in " + delay, Toast.LENGTH_SHORT).show();
                        }

                        etTime.setText(time);

                        /*SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("count", count);
                        editor.apply();
                        Log.d("WORKING?", "count saved" + count);*/
                    }
                });

        tv = view.findViewById(R.id.time);
        tv.setText(etTime.getText().toString());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment newFragment = new TimePickerFragment();

                newFragment.timeView(tv);
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });

        spinner = (Spinner) view.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.days_before, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerActivity());

        return builder.create();
    }

    //spinner activity class
    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)

            Log.d("SPINNER", "SELECTED " + parent.getItemAtPosition(pos));
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
            Log.d("SPINNER", "NOTHING SELECTED");
        }
    }

    //interface used for sending data to activity
    public interface NotificationSettingsListener {
        void setTime(String time);
        void setDays(String days);
    }

//    //initiates notificationsettingslistener interface
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            listener = (NotificationSettingsListener) context;
//        }
//        catch (final ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
//        }
//    }

    public void scheduleNotification(Notification notification, long delay, Context context) {
        //delay = computeDelay();

        //initiates new notification intent
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);///////////////////////////////////////////////////////

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1); //adds notificationpublisher id constant 1
        if (notifID != null) {
            notificationIntent.setAction(notifID);
            //Toast.makeText(context, notifID, Toast.LENGTH_SHORT).show();
            //Log.d("NOTIFID", "" + notifID);
        } else {
            notifID = UUID.randomUUID().toString();
            //Log.d("NOTIF notifsettings", "" + notifID);
            notificationIntent.setAction("" + notifID);
            //notificationIntent.setAction("" + count);
            //count ++;
            //Log.d("RANDOM ID", UUID.randomUUID().toString());
        }

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification); //adds the created notification
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //sets notification runtime in milliseconds
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        //gets system alarm service
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //sets notification runtime in alarm service when it will notify
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public Notification getNotification(String content, Context context) {
        //initiates notification builder
        Notification.Builder builder = new Notification.Builder(context);///////////////////////////////////

        //sets notificationbuilder properties
        builder.setContentTitle("Time to check this out!");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_movie_filter);
        builder.setDefaults(Notification.DEFAULT_SOUND);

        return builder.build();
    }

    public long getDelay() {
        return delay;
    }

    public void setForAdding(boolean isForAdding) {
        this.isForAdding = isForAdding;
    }

    public void setNotifID(String notifID) {
        this.notifID = notifID;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getNotifID() { return notifID; }
    /*@Override
    public void onStop() {
        super.onStop();
        SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("count", count);
        editor.apply();
        Log.d("onStop", "count saved" + count);
    }*/

    /*public long computeDelay() {

        String hrs = time.substring(0, time.indexOf(":"));
        String mins = time.substring(time.indexOf(":") + 1);

        //gets the current date and time
        Calendar c = Calendar.getInstance();
        int iYear = c.get(Calendar.YEAR);
        int iMonths = c.get(Calendar.MONTH);
        int iDays = c.get(Calendar.DAY_OF_MONTH);
        int iHrs = c.get(Calendar.HOUR_OF_DAY);
        int iMins = c.get(Calendar.MINUTE);
        int iSecs = c.get(Calendar.SECOND);

        //gets the sched date
        String date = etDate.getText().toString();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long yearsToMillis = TimeUnit.DAYS.toMillis((cal.get(Calendar.YEAR) - iYear) * c.getActualMaximum(Calendar.DAY_OF_YEAR));
        long monthsToMillis = TimeUnit.DAYS.toMillis((cal.get(Calendar.MONTH) - iMonths) * c.getActualMaximum(Calendar.MONTH));
        long daysToMillis = TimeUnit.DAYS.toMillis((cal.get(Calendar.DAY_OF_MONTH) - Integer.parseInt(days)) - iDays);

        //converts selected date and time to milliseconds
        long selectedMins = TimeUnit.MINUTES.toMillis(Integer.parseInt(mins));
        long selectedHrs = TimeUnit.HOURS.toMillis(Integer.parseInt(hrs));

        //converts current date and time to milliseconds
        long currentHrs = TimeUnit.HOURS.toMillis(iHrs);
        long currentMins = TimeUnit.MINUTES.toMillis(iMins);
        long currentSecs = TimeUnit.SECONDS.toMillis(iSecs);

        //computes for the notification delay
        long minsToMillis = selectedMins - currentMins;
        long hrsToMillis = selectedHrs - currentHrs;
        long secsToMillis = 60 - currentSecs;

        Log.d("DAYS", "" + daysToMillis);
        Log.d("HRS", "" + hrsToMillis);
        Log.d("MINS", "" + minsToMillis);
        Log.d("YEARS", "" + yearsToMillis);
        Log.d("MONTHS", "" + monthsToMillis);
        Log.d("SECONDS", "" + secsToMillis);

        delay = yearsToMillis + monthsToMillis + daysToMillis + minsToMillis + hrsToMillis + secsToMillis;
        Log.d("TOTAL", delay + "");
        return delay;
    }*/
}
