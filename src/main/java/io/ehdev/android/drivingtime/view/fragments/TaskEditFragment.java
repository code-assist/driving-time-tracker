package io.ehdev.android.drivingtime.view.fragments;

import android.app.FragmentManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import dagger.ObjectGraph;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.adapter.TaskReviewAdapter;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.module.ModuleGetters;
import io.ehdev.android.drivingtime.view.dialog.EditTaskDialog;
import io.ehdev.android.drivingtime.view.dialog.InsertOrEditTaskDialog;
import io.ehdev.android.drivingtime.view.dialog.ShowDialog;
import io.ehdev.android.drivingtime.view.entry.DisplayRecordRow;

import java.util.ArrayList;
import java.util.List;

public class TaskEditFragment extends AbstractListDrivingFragment<Task> {

    public static final String TAG = TaskEditFragment.class.getSimpleName();

    public TaskEditFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        ObjectGraph objectGraph = ObjectGraph.create(ModuleGetters.getInstance());
        objectGraph.inject(this);

        List<Task> taskEntries = getTasks();
        setAdapter(new TaskReviewAdapter(getActivity(), taskEntries));
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setTitleEntry(view);
        return view;
    }

    private List<Task> getTasks() {
        try{
            return databaseHelper.getTaskDao().queryForAll();
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
            return new ArrayList<Task>();
        }
    }

    private void setTitleEntry(View view) {
        DisplayRecordRow recordRow = (DisplayRecordRow)view.findViewById(R.id.titleBar);
        recordRow.setLeftText("Type Of Driving");
        recordRow.setCenterText("");
        recordRow.setRightText("Required Time");
        recordRow.setTextAttributes(20, Typeface.BOLD);
    }

    protected PostEditExecution getReloadAdapter(){
        return new PostEditExecution() {
            public void execute(){

                new AsyncTask<Void, Void, List<Task>>(){

                    @Override
                    protected List<Task> doInBackground(Void... params) {
                        return getTasks();
                    }

                    @Override
                    protected void onPostExecute(List<Task> records){
                        getAdapter().replaceDataSet(records);
                    }
                }.execute();
            }
        };
    }

    protected ShowDialog getShowDialog(){
        return new ShowDialog<Task>() {
            @Override
            public void showDialog(Task recordToEdit) {
                try {
                    FragmentManager fm = getChildFragmentManager();
                    InsertOrEditTaskDialog insertRecordDialog = new EditTaskDialog(recordToEdit, getReloadAdapter());
                    insertRecordDialog.show(fm, "Insert Record Dialog");
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Unable to create view", Toast.LENGTH_LONG);
                    Log.i(TAG, e.getMessage());
                }
            }
        };
    }
}
