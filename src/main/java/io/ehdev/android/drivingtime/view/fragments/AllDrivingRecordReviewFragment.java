package io.ehdev.android.drivingtime.view.fragments;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dagger.ObjectGraph;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.adapter.DrivingRecordAdapter;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.database.dao.DatabaseHelper;
import io.ehdev.android.drivingtime.module.ModuleGetters;
import io.ehdev.android.drivingtime.view.entry.DisplayRecordRow;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AllDrivingRecordReviewFragment extends AbstractListDrivingRecordsFragment {

    public static final String TAG = AllDrivingRecordReviewFragment.class.getSimpleName();

    @Inject
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        ObjectGraph objectGraph = ObjectGraph.create(new ModuleGetters(getActivity()));
        objectGraph.inject(this);

        setAdapter(new DrivingRecordAdapter(getActivity(), getAllEntries()));
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setTitleEntry(view);

        return view;
    }


    private List<Record> getAllEntries() {
        List<Record> drivingRecordList;
        try{
            drivingRecordList = new ArrayList<Record>();
            List<Record> list = databaseHelper.getRecordDao().queryForAll();
            for(Record record : list){
                databaseHelper.getTaskDao().refresh(record.getDrivingTask());
                drivingRecordList.add(record);
            }
            return drivingRecordList;
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
            return new ArrayList<Record>();
        }
    }

    private void setTitleEntry(View view) {
        DisplayRecordRow recordRow = (DisplayRecordRow)view.findViewById(R.id.titleBar);
        recordRow.setLeftText("Type Of Driving");
        recordRow.setCenterText("Duration of the Drive");
        recordRow.setRightText("Start Time");
        recordRow.setTextAttributes(20, Typeface.BOLD);
    }

    protected AsyncTask<Void, Void, List<Record>> getReloadAdapter(){

        return new AsyncTask<Void, Void, List<Record>>(){

            @Override
            protected List<Record> doInBackground(Void... params) {
                return getAllEntries();
            }

            @Override
            protected void onPostExecute(List<Record> records){
                getAdapter().replaceDataSet(getAllEntries());
            }
        };
    }
}
