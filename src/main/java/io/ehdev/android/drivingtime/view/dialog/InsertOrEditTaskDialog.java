/*
 * Copyright (C) 2013 by Ethan Hall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.ehdev.android.drivingtime.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import dagger.ObjectGraph;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.database.dao.DatabaseHelper;
import io.ehdev.android.drivingtime.module.ModuleGetters;
import org.joda.time.Duration;
import org.joda.time.Period;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.sql.SQLException;

public class InsertOrEditTaskDialog extends DialogFragment {

    private static final String TAG = InsertOrEditTaskDialog.class.getSimpleName();
    private final String title;

    @Inject
    protected DatabaseHelper databaseHelper;

    private Task drivingTask;

    protected InsertOrEditTaskDialog(){
        title = "Add Time";
    }

    public InsertOrEditTaskDialog(Task drivingTask, String title) {
        this.drivingTask = drivingTask;
        this.title = title;
    }

    public void onSaveInstanceState (Bundle outState){
        outState.putInt("task", drivingTask.getId());
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceDialog){

        ObjectGraph objectGraph = ObjectGraph.create(ModuleGetters.getInstance());
        objectGraph.inject(this);

        if(null != savedInstanceDialog){
            pullTaskFromSavedInstance(savedInstanceDialog);
        }

        AlertDialog.Builder builder = createDialogAddButtons();
        builder.setTitle(title);
        return builder.create();
    }

    private void pullTaskFromSavedInstance(Bundle savedInstanceDialog) {
        int taskId = savedInstanceDialog.getInt("task");
        try{
            drivingTask = databaseHelper.getTaskFromId(taskId);
        } catch (SQLException e) {
            dismiss();
        }
    }


    private AlertDialog.Builder createDialogAddButtons() {
        AlertDialog.Builder builder = createDialogBuilder();
        setOKButton(builder);
        setCancelButton(builder);
        return builder;
    }

    private AlertDialog.Builder createDialogBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = setupView();
        builder.setView(v);
        return builder;
    }

    private View setupView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.insert_task_dialog, null);
        setTimeViewParameters(v);
        setName(v);
        return v;
    }

    private void setName(View v) {
        ((EditText)v.findViewById(R.id.taskName)).setText(drivingTask.getTaskName());
    }

    private void setCancelButton(AlertDialog.Builder builder) {
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InsertOrEditTaskDialog.this.getDialog().cancel();
            }
        });
    }

    private void setOKButton(AlertDialog.Builder builder) {
        builder.setPositiveButton(R.string.addRecord, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog thisDialog = InsertOrEditTaskDialog.this.getDialog();
                String taskName = ((EditText) thisDialog.findViewById(R.id.taskName)).getText().toString();
                Long durationOfEntry = getDurationOfEntry(thisDialog);
                drivingTask.setTaskName(taskName);
                drivingTask.setRequiredHours(new Duration(durationOfEntry));
                try{
                    databaseHelper.getTaskDao().createOrUpdate(drivingTask);
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }

                thisDialog.dismiss();
            }
        });
    }

    private Long getDurationOfEntry(Dialog thisDialog) {
        TimePicker timePicker = ((TimePicker)thisDialog.findViewById(R.id.durationPicker));
        return (timePicker.getCurrentHour() * 60 + timePicker.getCurrentMinute()) * 60 * 1000L;
    }

    private void setTimeViewParameters(View view) {
        Period duration = drivingTask.getRequiredHours().toPeriod();
        ((TimePicker)view.findViewById(R.id.durationPicker)).setIs24HourView(true);
        ((TimePicker)view.findViewById(R.id.durationPicker)).setCurrentHour(duration.getHours());
        ((TimePicker)view.findViewById(R.id.durationPicker)).setCurrentMinute(duration.getMinutes());
    }

}
