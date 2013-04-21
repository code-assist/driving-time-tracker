package io.ehdev.android.drivingtime.view.dialog;

import android.content.DialogInterface;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.view.fragments.AbstractListDrivingFragment;

public class EditTaskDialog extends InsertOrEditTaskDialog {
    private AbstractListDrivingFragment.PostEditExecution reloadAdapter;

    public EditTaskDialog(Task drivingTask, AbstractListDrivingFragment.PostEditExecution reloadAdapter) {
        super(drivingTask);
        this.reloadAdapter = reloadAdapter;
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        reloadAdapter.execute();
    }

}
