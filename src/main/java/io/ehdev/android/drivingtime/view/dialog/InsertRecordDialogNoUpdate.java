package io.ehdev.android.drivingtime.view.dialog;

import android.content.DialogInterface;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.database.dao.DatabaseHelper;

import javax.inject.Inject;
import java.util.List;

public class InsertRecordDialogNoUpdate extends InsertOrEditRecordDialog {

    @Inject
    protected DatabaseHelper dao;

    private ReloadView reloadView;

    public InsertRecordDialogNoUpdate(Record drivingRecord, List<Task> drivingTaskList, ReloadView reloadView) {
        super(drivingRecord, drivingTaskList);
        this.reloadView = reloadView;
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        reloadView.reload();
    }

    public interface ReloadView{
        public void reload();
    }
}
