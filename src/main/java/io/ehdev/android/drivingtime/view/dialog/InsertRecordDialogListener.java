package io.ehdev.android.drivingtime.view.dialog;

import android.content.DialogInterface;
import android.util.Log;
import io.ehdev.android.drivingtime.adapter.DrivingRecordAdapter;
import io.ehdev.android.drivingtime.database.dao.AggregatedDrivingRecordDAO;

public class InsertRecordDialogListener implements DialogInterface {

    private final DrivingRecordAdapter drivingRecordAdapter;
    private final AggregatedDrivingRecordDAO aggregatedDrivingRecordDAO;
    private static final String TAG = InsertRecordDialogListener.class.getName();

    public InsertRecordDialogListener(DrivingRecordAdapter drivingRecordAdapter, AggregatedDrivingRecordDAO aggregatedDrivingRecordDAO) {
        this.drivingRecordAdapter = drivingRecordAdapter;
        this.aggregatedDrivingRecordDAO = aggregatedDrivingRecordDAO;
    }

    @Override
    public void cancel() {
        Log.i(TAG, "Move along now");
    }

    @Override
    public void dismiss() {
        Log.i(TAG, "Dismissing view");
        drivingRecordAdapter.setAggregatedDrivingRecord(aggregatedDrivingRecordDAO.createDrivingRecordList());
    }
}
