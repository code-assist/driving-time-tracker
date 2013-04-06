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
import io.ehdev.android.drivingtime.adapter.pojo.AggregatedDrivingRecord;
import io.ehdev.android.drivingtime.view.dialog.InsertRecordDialog;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(MainFragment.class.getSimpleName(), "onCreateView");
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

    private void createTestDrivingRecords(View view) {
        List<AggregatedDrivingRecord> aggregatedDrivingRecordList = new ArrayList<AggregatedDrivingRecord>();
        aggregatedDrivingRecordList.add(new AggregatedDrivingRecord("Highway", 100f, 50f));
        aggregatedDrivingRecordList.add(new AggregatedDrivingRecord("Night", 10f, 7f));
        ListView newListView = (ListView)view.findViewById(R.id.currentStatusView);
        newListView.setAdapter(new DrivingRecordAdapter(getSherlockActivity(), aggregatedDrivingRecordList));
    }
}
