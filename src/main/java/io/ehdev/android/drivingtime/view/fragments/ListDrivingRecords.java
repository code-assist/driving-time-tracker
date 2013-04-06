package io.ehdev.android.drivingtime.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.database.model.DrivingRecord;

import java.util.List;

public class ListDrivingRecords extends SherlockFragment {

    private static final String TAG = ListDrivingRecords.class.getName();
    private List<DrivingRecord> drivingRecordList;

    public ListDrivingRecords(List<DrivingRecord> drivingRecordList){

        this.drivingRecordList = drivingRecordList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(MainFragment.class.getName(), "onCreateView");
        View view = inflater.inflate(R.layout.list_view, null);
        return view;

    }
}
