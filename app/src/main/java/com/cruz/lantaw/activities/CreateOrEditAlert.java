package com.cruz.lantaw.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cruz.lantaw.R;
import com.cruz.lantaw.models.Movie;
import com.cruz.lantaw.service.AlarmService;
import com.cruz.lantaw.service.ReminderContract;
import com.cruz.lantaw.service.ReminderDataHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateOrEditAlert extends AppCompatActivity {

    private SimpleAdapter mAdapter;
    private EditText mContent, mTitle;
    private String mTime, mDate;
    private int mID, mRepeatMode;
    private Map<String, String> mAlarmTime, mAlarmDate, mAlarmRepeat, mSave;
    private Calendar mAlertTime;
    private ContentResolver mContentResolver;

    private static final String NONE = "None";
    private static final String HOURLY = "Hourly";
    private static final String DAILY = "Daily";
    private static final String WEEKLY = "Weekly";
    private static final String MONTHLY = "Monthly";
    private static final String YEARLY = "Yearly";

    private static final String[] REPEAT_MODES =
            new String[]{NONE, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY};

    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm aa");
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");

    public static final String ID_KEY = "id";
    private String thumbnail;
    private String movieID;

    private DatabaseReference myRef;
    private FirebaseUser user;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_edit_alert);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("saved").child("user");
        thumbnail = getIntent().getStringExtra("thumbnail");
        movieID = getIntent().getStringExtra("id");

        mContentResolver = getContentResolver();

        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        mAlarmTime = new HashMap<String, String>();
        mAlarmDate = new HashMap<String, String>();
        mAlarmRepeat = new HashMap<String, String>();
        mSave = new HashMap<String,String>();

        mContent = (EditText) findViewById(R.id.alertContent);
        mTitle = (EditText) findViewById(R.id.alertTitle);

        mRepeatMode = 0;

        Intent intent = getIntent();
        mID = intent.getIntExtra(ID_KEY, 0);
        mAlertTime = Calendar.getInstance();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        this.setSupportActionBar(toolbar);

        // If item exists, then set mTime and mDate list items to the mTime and mDate stored in alert
        if (mID > 0) {
            Uri uri = ContentUris.withAppendedId(ReminderContract.Alerts.CONTENT_URI,
                    mID);
            Cursor cursor = mContentResolver.query(uri, null, null, null, null);
            cursor.moveToFirst();
            String contentString = cursor.getString(cursor.getColumnIndex
                    (ReminderDataHelper.DB_COLUMN_CONTENT));
            String titleString = cursor.getString(cursor.getColumnIndex(ReminderDataHelper.DB_COLUMN_TITLE));
            mContent.setText(contentString);
            mTitle.setText(titleString);

            long timeInMilliseconds = cursor.getLong(cursor.getColumnIndex(ReminderDataHelper.DB_COLUMN_TIME));
            mRepeatMode = cursor.getInt(cursor.getColumnIndex(ReminderDataHelper.DB_COLUMN_FREQUENCY));
            mAlertTime.setTimeInMillis(timeInMilliseconds);
            DateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

            mTime = timeFormat.format(mAlertTime.getTime());
            mDate = dateFormat.format(mAlertTime.getTime());
//            getSupportActionBar().setTitle("Edit Alert");
            cursor.close();

            // Otherwise, set mTime and mDate list items to system mTime
        } else {
            Calendar current = Calendar.getInstance();
            mTime = TIME_FORMAT.format(current.getTime());
            mDate = DATE_FORMAT.format(current.getTime());
            mAlertTime.setTimeInMillis(current.getTimeInMillis());
//            getSupportActionBar().setTitle("Create Alert");

        }

        mAlarmTime.put("mTitle", "Time");
        mAlarmTime.put("subtext", mTime);
        mAlarmDate.put("mTitle", "Date");
        mAlarmDate.put("subtext", mDate);
        mAlarmRepeat.put("mTitle", "Repeat");
        mAlarmRepeat.put("subtext", REPEAT_MODES[mRepeatMode]);
        mSave.put("mTitle","");
        mSave.put("subtext","SAVE");



        mapList.add(mAlarmTime);
        mapList.add(mAlarmDate);
        mapList.add(mAlarmRepeat);
        mapList.add(mSave);
        mAdapter = new SimpleAdapter(this, mapList, android.R.layout.simple_list_item_2,
                new String[]{"mTitle", "subtext"}, new int[]{android.R.id.text1, android.R.id.text2});
        ListView listView = (ListView) findViewById(R.id.alertSettings);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //if first item in list (the mTime), then show timePickerDialog
                if (i == 0) {
                    TimePickerDialog timePicker = timePicker();
                    timePicker.show();
                    //if second item in list (the mDate), then show datePicker dialog
                } else if (i == 1) {
                    DatePickerDialog datePicker = datePicker();
                    datePicker.show();
                } else if (i == 2) {
                    repeatDialog().show();
                } else{
                    saveAlert();
                }

            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog(mID).show();
            }
        });
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("MOVIE REMINDER");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

    }

    @Override
    public void onBackPressed() {
        saveAlert();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_or_edit_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

//            case R.id.action_del_alert:
//                deleteDialog(mID).show();
//                break;

            case android.R.id.home:
//                saveAlert();
                terminateActivity();
                break;

            default:
                break;

        }

        return true;
    }

    // mTime picker
    private TimePickerDialog timePicker() {
        return new TimePickerDialog(CreateOrEditAlert.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        mAlertTime.set(Calendar.HOUR_OF_DAY, hour);
                        mAlertTime.set(Calendar.MINUTE, minute);
                        mAlertTime.set(Calendar.SECOND, 0);
                        mTime = TIME_FORMAT.format(mAlertTime.getTime());
                        mAlarmTime.put("subtext", mTime);
                        mAdapter.notifyDataSetChanged();
                    }
                }, mAlertTime.get(Calendar.HOUR_OF_DAY), mAlertTime.get(Calendar.MINUTE), false);
    }

    // mDate picker
    private DatePickerDialog datePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(CreateOrEditAlert.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        mAlertTime.set(Calendar.YEAR, year);
                        mAlertTime.set(Calendar.MONTH, month);
                        mAlertTime.set(Calendar.DAY_OF_MONTH, day);
                        mDate = DATE_FORMAT.format(mAlertTime.getTime());
                        mAlarmDate.put("subtext", mDate);
                        mAdapter.notifyDataSetChanged();
                    }
                }, mAlertTime.get(Calendar.YEAR), mAlertTime.get(Calendar.MONTH),
                mAlertTime.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePicker;
    }

    private AlertDialog saveDialog(final int id, final String title, final String content, long time) {
        final int saveId = id;
        final long saveTime = time;
        final String saveMessage = content;
        final String saveTitle = title;


        return new AlertDialog.Builder(this)

                .setTitle("Confirm")
                .setMessage("Do you want to save?")

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int i) {

                        // if item exists, cancel previous alarm, update alert, then set new alarm
                        if (saveId > 0) {
                            if(saveTitle.equals("") || saveMessage.equals("")){
                                Toast.makeText(CreateOrEditAlert.this, "Please don't leave a field blank.", Toast.LENGTH_SHORT).show();
                            }else {
                                Intent cancelPrevious = new Intent(CreateOrEditAlert.this,
                                        AlarmService.class);
                                cancelPrevious.putExtra("id", saveId);
                                cancelPrevious.setAction(AlarmService.CANCEL);
                                startService(cancelPrevious);
                                ContentValues values = new ContentValues();
                                values.put(ReminderContract.Alerts.TITLE, saveTitle);
                                values.put(ReminderContract.Alerts.CONTENT, saveMessage);
                                values.put(ReminderContract.Alerts.TIME, saveTime);
                                values.put(ReminderContract.Alerts.FREQUENCY, mRepeatMode);
                                Uri uri = ContentUris.withAppendedId(ReminderContract.Alerts.CONTENT_URI,
                                        saveId);
                                mContentResolver.update(uri, values, null, null);
                                createAlarm(saveId);
                                saveMovie(thumbnail, movieID);
                                terminateActivity();
                                dialog.dismiss();
                            }
                            // creates alarm for new alert
                        } else {
                            if(saveTitle.equals("") || saveMessage.equals("")){
                                Toast.makeText(CreateOrEditAlert.this, "Please don't leave a field blank.", Toast.LENGTH_SHORT).show();
                            }else {
                                ContentValues values = new ContentValues();
                                values.put(ReminderContract.Alerts.TYPE, ReminderContract.PATH_ALERT);
                                values.put(ReminderContract.Alerts.TITLE, saveTitle);
                                values.put(ReminderContract.Alerts.CONTENT, saveMessage);
                                values.put(ReminderContract.Alerts.TIME, saveTime);
                                values.put(ReminderContract.Alerts.FREQUENCY, mRepeatMode);
                                Uri uri = mContentResolver.insert(ReminderContract.Notes.CONTENT_URI,
                                        values);
                                createAlarm(Integer.parseInt(uri.getLastPathSegment()));
                                saveMovie(thumbnail, movieID);
                                terminateActivity();
                                dialog.dismiss();
                            }
                        }

                    }

                })


                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        terminateActivity();
                        dialog.dismiss();

                    }
                })
                .create();
    }

    private AlertDialog deleteDialog(int id) {

        final int deleteId = id;
        return new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {

                        if (deleteId > 0) {
                            // delete the alarm
                            Intent delete = new Intent(CreateOrEditAlert.this, AlarmService.class);
                            delete.putExtra(ID_KEY, deleteId);
                            delete.setAction(AlarmService.DELETE);
                            Uri uri = ContentUris.withAppendedId(ReminderContract.Notes.CONTENT_URI,
                                    deleteId);
                            mContentResolver.delete(uri, null, null);
                            startService(delete);
                            terminateActivity();
                        } else {
                            terminateActivity();
                        }
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .create();

    }

    // set repeat mode from None, Hour, Daily, Monthly, Yearly
    private AlertDialog repeatDialog() {
        final int prevRepeat = mRepeatMode;
        return new AlertDialog.Builder(this)
                .setTitle("Repeat")
                .setSingleChoiceItems(REPEAT_MODES, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mRepeatMode = i;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // set label to selected repeat mode
                        mAlarmRepeat.put("subtext", REPEAT_MODES[mRepeatMode]);
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mRepeatMode = prevRepeat;
                    }
                })
                .create();
    }

    // creates an alarm
    private void createAlarm(int id) {
        Intent alarm = new Intent(this, AlarmService.class);
        alarm.putExtra(ID_KEY, id);
        alarm.setAction(AlarmService.CREATE);
        startService(alarm);
    }

    // go back to main activity
    private void terminateActivity() {
        NavUtils.navigateUpFromSameTask(this);
    }

    // saves the alert (Handles case where if mTime set to before current, just set immediate alert)
    private void saveAlert() {
        String contentString = mContent.getText().toString();
        String titleString = mTitle.getText().toString();
        if (!(mAlertTime.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())) {
            saveDialog(mID, titleString, contentString, mAlertTime.getTimeInMillis()).show();
       } else {
            saveDialog(mID, titleString, contentString, Calendar.getInstance().getTimeInMillis()).show();
        }
    }


    private void saveMovie(String poster_image_thumbnail, String movieId) {
        Movie movie = new Movie(poster_image_thumbnail, movieId);
        user = FirebaseAuth.getInstance().getCurrentUser();
        myRef.child(user.getUid().toString()).child(movieId).setValue(movie);
        Toast.makeText(CreateOrEditAlert.this, "Movie Saved!", Toast.LENGTH_SHORT).show();
        Toast.makeText(CreateOrEditAlert.this, "Go to Saved movies tab to view saved movies", Toast.LENGTH_SHORT).show();

    }

}