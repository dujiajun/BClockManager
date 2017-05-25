package com.dujiajun.bclockmanager.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dujiajun.bclockmanager.MyDatabaseHelper;
import com.dujiajun.bclockmanager.R;
import com.dujiajun.bclockmanager.model.Clock;
import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;

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

        //timePicker.setEnabled(true);


        MyDatabaseHelper dbHelper = new MyDatabaseHelper(ClockActivity.this, "clocks.db", null, 1);
        db = dbHelper.getReadableDatabase();

        init();


    }

    private int id = -1;
    private boolean isAdd = true;

    private void init() {
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        if ("add".equals(mode)) {
            isAdd = true;
            if (Build.VERSION.SDK_INT >= 23) {
                timePicker.setHour(6);
                timePicker.setMinute(0);
            } else {
                timePicker.setCurrentHour(6);
                timePicker.setCurrentMinute(0);
            }
        } else {
            isAdd = false;
            id = intent.getIntExtra("id", 0);
            if (Build.VERSION.SDK_INT >= 23) {
                timePicker.setHour(intent.getIntExtra("hour", 6));
                timePicker.setMinute(intent.getIntExtra("minute", 0));
            } else {
                timePicker.setCurrentHour(intent.getIntExtra("hour", 6));
                timePicker.setCurrentMinute(intent.getIntExtra("minute", 0));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isAdd) {
            getMenuInflater().inflate(R.menu.menu_clock_add, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_clock_modify, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add: {
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
                Cursor cursor = db.rawQuery("select * from Clocks order by id DESC", null);

                if (cursor.moveToFirst()) {

                    id = cursor.getInt(cursor.getColumnIndex("id"));
                    AlarmManagerUtil.setAlarm(getApplicationContext(), 0, clock.getHour(), clock.getMinute(), id, 0, null, 0);
                    Toast.makeText(ClockActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                }

                cursor.close();

                finish();
            }

            break;
            case R.id.menu_modify: {
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
                db.update("Clocks", values, "id = " + String.valueOf(id), null);
                AlarmManagerUtil.cancelAlarm(getApplicationContext(), AlarmManagerUtil.ALARM_ACTION, id);
                AlarmManagerUtil.setAlarm(getApplicationContext(), 0, clock.getHour(), clock.getMinute(), id, 0, null, 0);
                Toast.makeText(ClockActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            case R.id.menu_delete:
                new AlertDialog.Builder(this).setTitle("确认删除？").setMessage("删除之后将不可恢复？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlarmManagerUtil.cancelAlarm(getApplicationContext(), AlarmManagerUtil.ALARM_ACTION, id);
                                db.delete("Clocks", "id = " + String.valueOf(id), null);
                                finish();
                            }
                        }).setNegativeButton("取消", null).show();

                break;
        }
        return true;
    }
}
