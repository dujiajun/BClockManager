package com.dujiajun.bclockmanager.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dujiajun.bclockmanager.MyDatabaseHelper;
import com.dujiajun.bclockmanager.R;
import com.dujiajun.bclockmanager.model.Clock;

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


    class ClockAdapter extends RecyclerView.Adapter<ClockAdapter.ViewHolder> {

        private List<Clock> clockList;

        public ClockAdapter(List<Clock> clockList) {
            this.clockList = clockList;
        }

        @Override

        public ClockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clock, parent, false);
            ClockAdapter.ViewHolder holder = new ClockAdapter.ViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(ClockAdapter.ViewHolder holder, int position) {
            Clock clock = clockList.get(position);
            holder.clockTime.setText(clock.getTimeText());
            holder.openSwitch.setChecked(clock.getOpen());
        }

        @Override
        public int getItemCount() {
            return clockList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView clockTime;
            Switch openSwitch;

            public ViewHolder(View itemView) {
                super(itemView);
                clockTime = (TextView) itemView.findViewById(R.id.tv_clock_time);
                openSwitch = (Switch) itemView.findViewById(R.id.clock_switch);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ClockActivity.class);
                        intent.putExtra("mode", "modify");
                        intent.putExtra("id", clockList.get(getAdapterPosition()).getId());
                        intent.putExtra("hour", clockList.get(getAdapterPosition()).getHour());
                        intent.putExtra("minute", clockList.get(getAdapterPosition()).getMinute());
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
