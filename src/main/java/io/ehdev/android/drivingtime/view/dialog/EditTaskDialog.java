package io.ehdev.android.drivingtime.view.dialog;

import android.content.DialogInterface;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.view.PostEditExecution;

public class EditTaskDialog extends InsertOrEditTaskDialog {
    private static PostEditExecution reloadAdapter;
    private static Task drivingTask;

    public EditTaskDialog(Task drivingTask, PostEditExecution reloadAdapter) {
        super(drivingTask, "Add Task");
        EditTaskDialog.drivingTask = drivingTask;
        EditTaskDialog.reloadAdapter = reloadAdapter;
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        reloadAdapter.execute();
    }

}
