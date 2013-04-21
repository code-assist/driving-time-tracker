package io.ehdev.android.drivingtime.view.dialog;

import android.content.DialogInterface;
import io.ehdev.android.drivingtime.adapter.AggregatedDrivingRecordAdapter;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.database.dao.DatabaseHelper;

import java.util.List;

public class InsertRecordDialog extends InsertOrEditRecordDialog {
    private AggregatedDrivingRecordAdapter aggregatedDrivingRecordAdapter;
    private DatabaseHelper dao;

    public InsertRecordDialog(Record drivingRecord, List<Task> drivingTaskList, DatabaseHelper dao, AggregatedDrivingRecordAdapter aggregatedDrivingRecordAdapter) {
        super(drivingRecord, drivingTaskList);
        this.aggregatedDrivingRecordAdapter = aggregatedDrivingRecordAdapter;
        this.dao = dao;
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        aggregatedDrivingRecordAdapter.replaceDataSet(dao.createDrivingRecordList());
    }

}
