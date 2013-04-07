package io.ehdev.android.drivingtime.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import io.ehdev.android.drivingtime.database.model.DrivingRecord;
import io.ehdev.android.drivingtime.view.entry.DisplayRecordRow;

import java.util.Date;
import java.util.List;

public class DrivingRecordAdapter extends BaseAdapter{

    private Context context;
    private List<DrivingRecord> drivingRecordList;

    public DrivingRecordAdapter(Context context, List<DrivingRecord> drivingRecordList){
        this.context = context;

        this.drivingRecordList = drivingRecordList;
    }

    @Override
    public int getCount() {
        return drivingRecordList.size();
    }

    @Override
    public DrivingRecord getItem(int position) {
        return drivingRecordList.get(position);
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

        return displayRecordRow;
    }
}
