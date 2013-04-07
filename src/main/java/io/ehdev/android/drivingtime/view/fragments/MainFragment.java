package io.ehdev.android.drivingtime.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.adapter.AggregatedDrivingRecordAdapter;
import io.ehdev.android.drivingtime.database.dao.AggregatedDrivingRecordDAO;
import io.ehdev.android.drivingtime.database.dao.DrivingRecordDao;
import io.ehdev.android.drivingtime.database.dao.DrivingTaskDao;
import io.ehdev.android.drivingtime.database.model.DrivingRecord;
import io.ehdev.android.drivingtime.database.model.DrivingTask;
import io.ehdev.android.drivingtime.view.activity.ListEntriesForTaskActivity;
import io.ehdev.android.drivingtime.view.dialog.InsertRecordDialog;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.sql.SQLException;
import java.util.List;

public class MainFragment extends SherlockFragment {

    private DrivingTaskDao drivingTaskDao;
    private DrivingRecordDao drivingRecordDao;
    private AggregatedDrivingRecordAdapter aggregatedDrivingRecordAdapter;
    private AggregatedDrivingRecordDAO aggregatedDrivingRecordDAO;

    private static final String TAG = MainFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(MainFragment.class.getName(), "onCreateView");
        setupDAOs();
        View view = inflater.inflate(R.layout.main, null);
        showEntryDialogFromButtonClick(view);
        addAdapterToListView(view);
        return view;

    }

    private void showEntryDialogFromButtonClick(View view) {
        (view.findViewById(R.id.addTimeToLog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragmentManager fm = getChildFragmentManager();
                    InsertRecordDialog insertRecordDialog = getInsertRecordDialog();
                    insertRecordDialog.show(fm, "Insert Record Dialog");
                } catch (Exception e) {
                    Toast.makeText(getSherlockActivity(), "Unable to create view", Toast.LENGTH_LONG);
                    Log.i(TAG, e.getMessage());
                }
            }

            private InsertRecordDialog getInsertRecordDialog() throws SQLException {
                List<DrivingTask> drivingTaskList = drivingTaskDao.getDao().queryForAll();
                DrivingRecord drivingRecord = new DrivingRecord(drivingTaskList.get(0), new DateTime(), Duration.standardHours(1));
                return new InsertRecordDialog(drivingRecord, drivingTaskList, aggregatedDrivingRecordDAO, aggregatedDrivingRecordAdapter);
            }
        });
    }

    private void setupDAOs() {
        drivingTaskDao = new DrivingTaskDao(getSherlockActivity());
        drivingRecordDao = new DrivingRecordDao(getSherlockActivity());
    }

    private void addAdapterToListView(View view) {
        ListView newListView = (ListView)view.findViewById(R.id.currentStatusView);
        aggregatedDrivingRecordDAO = new AggregatedDrivingRecordDAO(drivingRecordDao, drivingTaskDao);
        aggregatedDrivingRecordAdapter = new AggregatedDrivingRecordAdapter(getSherlockActivity(), aggregatedDrivingRecordDAO.createDrivingRecordList());
        newListView.setAdapter(aggregatedDrivingRecordAdapter);
        newListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "OnClick");
                Intent newActivity = new Intent();
                newActivity.setClass(getSherlockActivity(), ListEntriesForTaskActivity.class);
                newActivity.putExtra("taskId", aggregatedDrivingRecordAdapter.getItem(position).getDrivingTaskId());
                getSherlockActivity().startActivity(newActivity);
            }
        });
    }
}
