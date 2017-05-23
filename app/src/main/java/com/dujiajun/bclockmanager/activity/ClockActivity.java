package com.dujiajun.bclockmanager.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dujiajun.bclockmanager.MyDatabaseHelper;
import com.dujiajun.bclockmanager.R;
import com.dujiajun.bclockmanager.fragment.ClockSettingFragment;
import com.dujiajun.bclockmanager.model.Clock;

public class ClockActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TimePicker timePicker;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clocks);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        timePicker = (TimePicker) findViewById(R.id.timepicker);
        timePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= 23) {
            timePicker.setHour(6);
            timePicker.setMinute(0);
        } else {
            timePicker.setCurrentHour(6);
            timePicker.setCurrentMinute(0);
        }

        timePicker.setEnabled(true);

        /*FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        ClockSettingFragment clockSettingFragment = new ClockSettingFragment();
        transaction.add(R.id.clock_setting_frame,clockSettingFragment);
        transaction.commit();*/

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(ClockActivity.this, "clocks.db", null, 1);
        db = dbHelper.getReadableDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                //Toast.makeText(ClockActivity.this, "Add Clicked" , Toast.LENGTH_SHORT).show();
                Clock clock = new Clock(0, 0, true);
                if (Build.VERSION.SDK_INT >= 23) {
                    clock.setHour(timePicker.getHour());
                    clock.setMinute(timePicker.getMinute());
                } else {
                    clock.setHour(timePicker.getCurrentHour());
                    clock.setMinute(timePicker.getCurrentMinute());
                }

                ContentValues values = new ContentValues();
                values.put("hour", clock.getHour());
                values.put("minute", clock.getMinute());
                values.put("open", true);
                db.insert("Clocks", null, values);
                /*if (clock.save()) {
                    Toast.makeText(ClockActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(ClockActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                }*/
                /*if (clock.save()) {
                    clockList.clear();
                    List<Clock> newList =  DataSupport.findAll(Clock.class);
                    for (Clock cl : newList ) {
                        clockList.add(cl);
                    }

                    clockAdapter.notifyDataSetChanged();
                }*/
                break;
            case R.id.menu_cancel:
                Toast.makeText(ClockActivity.this, "Cancel Clicked", Toast.LENGTH_SHORT).show();
                finish();
        }
        return true;
    }
}
