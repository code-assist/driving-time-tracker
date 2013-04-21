package io.ehdev.android.drivingtime.view.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dagger.ObjectGraph;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.adapter.TaskReviewAdapter;
import io.ehdev.android.drivingtime.backend.StringHelper;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.module.ModuleGetters;
import io.ehdev.android.drivingtime.view.dialog.ShowDialog;
import io.ehdev.android.drivingtime.view.entry.DisplayProgressRecordRow;

import java.util.ArrayList;
import java.util.List;

public class TaskEditFragment extends AbstractListDrivingFragment<Task> {

    public static final String TAG = TaskEditFragment.class.getSimpleName();
    private Task drivingTask;
    private DisplayProgressRecordRow progressBar;

    public TaskEditFragment(Task drivingTask){

        this.drivingTask = drivingTask;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        ObjectGraph objectGraph = ObjectGraph.create(ModuleGetters.getInstance());
        objectGraph.inject(this);

        List<Task> taskEntries = getTasks();
        setAdapter(new TaskReviewAdapter(getActivity(), taskEntries));
        View view = super.onCreateView(inflater, container, savedInstanceState);
        progressBar = (DisplayProgressRecordRow)view.findViewById(R.id.taskProgressDialog);
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

    private List<Task> getTasks() {
        try{
            return databaseHelper.getTaskDao().queryForAll();
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
            return new ArrayList<Task>();
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

    protected AsyncTask<Void, Void, List<Task>> getReloadAdapter(){

        return new AsyncTask<Void, Void, List<Task>>(){

            @Override
            protected List<Task> doInBackground(Void... params) {
                return getTasks();
            }

            @Override
            protected void onPostExecute(List<Task> records){
            }
        };
    }

    protected ShowDialog getShowDialog(){
        return new ShowDialog<Task>() {
            @Override
            public void showDialog(Task recordToEdit) {

            }
        };
    }
}
