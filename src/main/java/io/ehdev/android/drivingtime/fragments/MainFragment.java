package io.ehdev.android.drivingtime.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.adapter.DrivingRecordAdapter;
import io.ehdev.android.drivingtime.database.dao.AggregatedDrivingRecordDAO;
import io.ehdev.android.drivingtime.database.dao.DrivingRecordDao;
import io.ehdev.android.drivingtime.database.dao.DrivingTaskDao;
import io.ehdev.android.drivingtime.view.dialog.InsertRecordDialog;

public class MainFragment extends SherlockFragment {

    private DrivingTaskDao drivingTaskDao;
    private DrivingRecordDao drivingRecordDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(MainFragment.class.getName(), "onCreateView");
        setupDAOs();
        View view = inflater.inflate(R.layout.main, null);
        showEntryDialogFromButtonClick(view);
        createTestDrivingRecords(view);
        return view;

    }

    private void showEntryDialogFromButtonClick(View view) {
        (view.findViewById(R.id.addTimeToLog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getChildFragmentManager();
                InsertRecordDialog insertRecordDialog = new InsertRecordDialog();
                insertRecordDialog.show(fm, "Insert Record Dialog");
            }
        });
    }

    private void updateUi() {
    }

    private void setupDAOs() {
        drivingTaskDao = new DrivingTaskDao(getSherlockActivity());
        drivingRecordDao = new DrivingRecordDao(getSherlockActivity());
    }

    private void createTestDrivingRecords(View view) {
        ListView newListView = (ListView)view.findViewById(R.id.currentStatusView);
        AggregatedDrivingRecordDAO aggregatedDrivingRecordDAO = new AggregatedDrivingRecordDAO(drivingRecordDao, drivingTaskDao);
        newListView.setAdapter(new DrivingRecordAdapter(getSherlockActivity(), aggregatedDrivingRecordDAO.createDrivingRecordList()));
    }
}
