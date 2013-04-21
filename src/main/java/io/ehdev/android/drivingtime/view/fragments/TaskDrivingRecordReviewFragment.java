package io.ehdev.android.drivingtime.view.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dagger.ObjectGraph;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.adapter.DrivingRecordAdapter;
import io.ehdev.android.drivingtime.backend.StringHelper;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.module.ModuleGetters;
import io.ehdev.android.drivingtime.view.entry.DisplayProgressRecordRow;

import java.util.ArrayList;
import java.util.List;

public class TaskDrivingRecordReviewFragment extends AbstractListDrivingRecordsFragment {

    public static final String TAG = TaskDrivingRecordReviewFragment.class.getSimpleName();
    private Task drivingTask;
    private DisplayProgressRecordRow progressBar;

    public TaskDrivingRecordReviewFragment(Task drivingTask){

        this.drivingTask = drivingTask;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        ObjectGraph objectGraph = ObjectGraph.create(ModuleGetters.getInstance());
        objectGraph.inject(this);

        List<Record> taskEntries = getTaskEntries();
        setAdapter(new DrivingRecordAdapter(getActivity(), taskEntries));
        View view = super.onCreateView(inflater, container, savedInstanceState);
        progressBar = (DisplayProgressRecordRow)view.findViewById(R.id.taskProgressDialog);
        setProgressBar(taskEntries);
        return view;
    }

    private void setProgressBar(List<Record> listOfEntries) {

        long requiredTime = drivingTask.getRequiredHours().getMillis();
        long spentTime = getRemainingTime(listOfEntries);

        progressBar.setRightText(String.format("%s Required", StringHelper.getPeriodAsString(requiredTime)));
        progressBar.setMaxOfProgress(requiredTime);

        progressBar.setCurrentProgress(spentTime);
        progressBar.setLeftText(String.format("Remaining : %s", StringHelper.getPeriodAsString((requiredTime - spentTime))));

        progressBar.invalidate();
    }

    private List<Record> getTaskEntries() {
        List<Record> drivingRecordList;
        try{
            drivingRecordList = new ArrayList<Record>();
            List<Record> list = databaseHelper.getDrivingRecordForTask(drivingTask.getId(), databaseHelper.getTaskDao());
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

    private long getRemainingTime(List<Record> listOfEntries) {
        long requiredTime = 0;
        if(listOfEntries == null || listOfEntries.isEmpty())
            return requiredTime;

        for(Record record : listOfEntries)
            requiredTime += record.getDurationOfDriving().getMillis();

        return requiredTime;
    }

    protected int getViewId(){
        return R.layout.aggregated_list_view;
    }

    protected AsyncTask<Void, Void, List<Record>> getReloadAdapter(){

        return new AsyncTask<Void, Void, List<Record>>(){

            @Override
            protected List<Record> doInBackground(Void... params) {
                return getTaskEntries();
            }

            @Override
            protected void onPostExecute(List<Record> records){
                getAdapter().replaceDataSet(records);
                setProgressBar(records);
            }
        };
    }
}
