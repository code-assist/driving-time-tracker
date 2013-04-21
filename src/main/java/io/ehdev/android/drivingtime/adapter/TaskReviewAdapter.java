package io.ehdev.android.drivingtime.adapter;

import android.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.view.entry.DisplayRecordRow;

import java.util.ArrayList;
import java.util.List;

public class TaskReviewAdapter extends EntryAdapter<Task>{

    public static final String TAG = TaskReviewAdapter.class.getName();

    public static final int NO_VALUE_SELECTED = -1;

    private Context context;
    private List<Task> drivingRecordList;
    private int selected = -1;

    public TaskReviewAdapter(Context context, List<Task> drivingRecordList){
        this.context = context;

        this.drivingRecordList = new ArrayList<Task>(drivingRecordList);
    }

    public void replaceDataSet(List<Task> drivingRecordList){
        this.drivingRecordList.clear();
        this.drivingRecordList.addAll(drivingRecordList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return drivingRecordList.size();
    }

    @Override
    public Task getItem(int position) {
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

        DisplayRecordRow displayRecordRow = (DisplayRecordRow) view;
        displayRecordRow.setRightText("right");
        displayRecordRow.setLeftText("left");
        displayRecordRow.setCenterText("center");
        displayRecordRow.requestLayout();
        if(position == selected)
            displayRecordRow.setBackgroundResource(R.color.holo_orange_dark);
        else
            displayRecordRow.setBackgroundResource(R.color.transparent);

        return displayRecordRow;
    }

    public void setSelected(int index){
        this.selected = index;
        notifyDataSetChanged();
    }

    public int getSelectedIndex(){
        return selected;
    }

    public boolean isIndexSelected(int index){
        return index == selected;
    }

    public void clearSelected(){
        setSelected(NO_VALUE_SELECTED);
    }
}
