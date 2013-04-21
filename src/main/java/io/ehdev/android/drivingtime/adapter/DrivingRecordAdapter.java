package io.ehdev.android.drivingtime.adapter;

import android.R;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.view.entry.DisplayRecordRow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DrivingRecordAdapter extends EntryAdapter<Record> {

    public static final String TAG = DrivingRecordAdapter.class.getName();

    private Context context;
    private List<Record> drivingRecordList;
    private int selected = -1;

    public DrivingRecordAdapter(Context context, List<Record> drivingRecordList){
        this.context = context;

        this.drivingRecordList = new ArrayList<Record>(drivingRecordList);
    }

    public void replaceDataSet(List<Record> drivingRecordList){
        this.drivingRecordList.clear();
        this.drivingRecordList.addAll(drivingRecordList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return drivingRecordList.size();
    }

    @Override
    public Record getItem(int position) {
        return drivingRecordList.get(position);
    }

    @Override
    public Class getClassName() {
        return Record.class;
    }

    @Override
    public long getItemId(int position) {
        return drivingRecordList.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(null == view || !(view instanceof DisplayRecordRow))
            view = new DisplayRecordRow(context);

        java.text.DateFormat timeFormatter = DateFormat.getTimeFormat(context);
        Date startTime = getItem(position).getStartTime().toDate();

        java.text.DateFormat dateFormatter= DateFormat.getLongDateFormat(context);
        String timeString = String.format("%s %s", dateFormatter.format(startTime), timeFormatter.format(startTime));

        DisplayRecordRow displayRecordRow = (DisplayRecordRow) view;
        displayRecordRow.setRightText(timeString);
        displayRecordRow.setLeftText(getItem(position).getDrivingTask().getTaskName());
        displayRecordRow.setCenterText(getItem(position).getDurationAsString());
        displayRecordRow.requestLayout();
        if(position == selected)
            displayRecordRow.setBackgroundResource(R.color.holo_orange_dark);
        else
            displayRecordRow.setBackgroundResource(R.color.transparent);

        return displayRecordRow;
    }

}
