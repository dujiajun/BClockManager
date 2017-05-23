package com.dujiajun.bclockmanager.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dujiajun.bclockmanager.MyDatabaseHelper;
import com.dujiajun.bclockmanager.R;
import com.dujiajun.bclockmanager.adapter.ClockAdapter;
import com.dujiajun.bclockmanager.model.Clock;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ClockAdapter clockAdapter;
    private List<Clock> clockList = new ArrayList<>();
    private SQLiteDatabase db;

    @Override
    protected void onResume() {
        initClocks();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Connector.getDatabase();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        clockAdapter = new ClockAdapter(clockList);
        recyclerView.setAdapter(clockAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ClockActivity.class);
                intent.putExtra("mode", "add");
                startActivity(intent);
            }
        });

        //initClocks();

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(MainActivity.this, "clocks.db", null, 1);
        db = dbHelper.getReadableDatabase();
    }

    private void initClocks() {

        clockList.clear();
        /*
        if (DataSupport.findAll(Clock.class)!=null)
        {
            List<Clock> newList = DataSupport.findAll(Clock.class);;
            for (Clock cl : newList ) {
                clockList.add(cl);
            }
        }

*/
        Cursor cursor = db.rawQuery("select * from Clocks", null);
        if (cursor.moveToFirst()) {
            do {
                Clock clock = new Clock();
                clock.setId(cursor.getInt(cursor.getColumnIndex("id")));
                clock.setOpen(cursor.getInt(cursor.getColumnIndex("open")) > 0);
                clock.setHour(cursor.getInt(cursor.getColumnIndex("hour")));
                clock.setMinute(cursor.getInt(cursor.getColumnIndex("minute")));
                clockList.add(clock);
            }
            while (cursor.moveToNext());

        }

        cursor.close();
        clockAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Toast.makeText(MainActivity.this, "Setting Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_about:
                Toast.makeText(MainActivity.this, "About Clicked", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
