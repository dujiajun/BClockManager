package com.dujiajun.bclockmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.dujiajun.bclockmanager.R;
import com.dujiajun.bclockmanager.model.Clock;

import java.util.List;

/**
 * Created by cqduj on 2017/05/04.
 */

public class ClockAdapter extends RecyclerView.Adapter<ClockAdapter.ViewHolder> {

    private List<Clock> clockList;

    public ClockAdapter(List<Clock> clockList) {
        this.clockList = clockList;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clock, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Clock clock = clockList.get(position);
        holder.clockTime.setText(clock.getTimeText());
        holder.openSwitch.setChecked(clock.getOpen());
    }

    @Override
    public int getItemCount() {
        return clockList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView clockTime;
        Switch openSwitch;

        public ViewHolder(View itemView) {
            super(itemView);
            clockTime = (TextView) itemView.findViewById(R.id.tv_clock_time);
            openSwitch = (Switch) itemView.findViewById(R.id.clock_switch);
        }
    }
}
