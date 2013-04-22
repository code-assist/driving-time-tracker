package io.ehdev.android.drivingtime.view.dialog;

import android.content.DialogInterface;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.view.PostEditExecution;

import java.util.List;

public class EditRecordDialog extends InsertOrEditRecordDialog {
    private PostEditExecution reloadAdapter;

    public EditRecordDialog(Record drivingRecord, List<Task> drivingTaskList, PostEditExecution reloadAdapter) {
        super(drivingRecord, drivingTaskList);
        this.reloadAdapter = reloadAdapter;
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        reloadAdapter.execute();
    }

}
