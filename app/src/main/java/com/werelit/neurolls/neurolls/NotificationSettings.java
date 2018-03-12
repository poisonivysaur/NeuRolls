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
import java.util.concurrent.TimeUnit;

public class NotificationSettings extends DialogFragment {
    private TextView tv;
    private Spinner days;
    private NotificationSettingsListener listener;
    private Spinner spinner;
    public static final String PREFS_NAME = "MyPrefsFile";
    private int count;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);
        this.count = settings.getInt("count", 0);

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
                        String days = spinner.getSelectedItem().toString();
                        String time = tv.getText().toString();

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
                        TextView etDate = ((ViewMediaDetailsActivity) getContext()).findViewById(R.id.date_text_view);
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

                        long total = yearsToMillis + monthsToMillis + daysToMillis + minsToMillis + hrsToMillis + secsToMillis;

                        Log.d("TOTAL", total + "");
                        scheduleNotification(getNotification(total + ""), total);
                        Toast.makeText(getContext(), total + " ms delay", Toast.LENGTH_SHORT).show();

                    }
                });

        tv = view.findViewById(R.id.time);
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

    private void scheduleNotification(Notification notification, long delay) {

        //initiates new notification intent
        Intent notificationIntent = new Intent(getContext(), NotificationPublisher.class);

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1); //adds notificationpublisher id constant 1
        count ++;
        notificationIntent.setAction("" + count);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification); //adds the created notification
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //sets notification runtime in milliseconds
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        //gets system alarm service
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        //sets notification runtime in alarm service when it will notify
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        //initiates notification builder
        Notification.Builder builder = new Notification.Builder(getContext());

        //sets notificationbuilder properties
        builder.setContentTitle("Time to check off that movie ;)");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_movie_filter);
        builder.setDefaults(Notification.DEFAULT_SOUND);

        return builder.build();
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("count", count);

        editor.apply();
        Log.d("onStop", "count saved" + count);
    }
}
