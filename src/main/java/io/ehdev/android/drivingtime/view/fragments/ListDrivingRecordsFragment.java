package io.ehdev.android.drivingtime.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.adapter.DrivingRecordAdapter;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.database.dao.DrivingRecordDao;
import io.ehdev.android.drivingtime.database.dao.DrivingTaskDao;

import java.util.ArrayList;
import java.util.List;

public class ListDrivingRecordsFragment extends SherlockFragment {

    private static final String TAG = ListDrivingRecordsFragment.class.getName();
    private List<Record> drivingRecordList;

    private void getAllEntries() {
        try{
            drivingRecordList = new ArrayList<Record>();
            DrivingRecordDao drivingRecordDao = new DrivingRecordDao(getSherlockActivity());
            DrivingTaskDao drivingTaskDao = new DrivingTaskDao(getSherlockActivity());
            List<Record> list = drivingRecordDao.getDao().queryForAll();
            for(Record record : list){
                drivingTaskDao.getDao().refresh(record.getDrivingTask());
                drivingRecordList.add(record);
            }
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreateView");
        getAllEntries();
        View view = inflater.inflate(R.layout.list_view, null);
        ((ListView)view.findViewById(R.id.listOfAllRecords)).setAdapter(new DrivingRecordAdapter(getSherlockActivity(), drivingRecordList));
        return view;

    }
}
