package io.ehdev.android.drivingtime.view.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.adapter.DrivingRecordAdapter;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.database.dao.DrivingRecordDao;
import io.ehdev.android.drivingtime.database.dao.DrivingTaskDao;
import io.ehdev.android.drivingtime.view.dialog.InsertOrEditRecordDialog;
import io.ehdev.android.drivingtime.view.dialog.ShowDialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListDrivingRecordsFragment extends Fragment {

    private static final String TAG = ListDrivingRecordsFragment.class.getName();
    private List<Record> drivingRecordList;
    private DrivingRecordAdapter adapter;
    private ActionMode actionMode;
    private DrivingRecordDao drivingRecordDao;
    private DrivingTaskDao drivingTaskDao;

    private void getAllEntries() {
        try{
            drivingRecordList = new ArrayList<Record>();
            drivingRecordDao = new DrivingRecordDao(getActivity());
            drivingTaskDao = new DrivingTaskDao(getActivity());

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
        ListView listView = (ListView) view.findViewById(R.id.listOfAllRecords);
        adapter = new DrivingRecordAdapter(getActivity(), drivingRecordList);
        listView.setAdapter(adapter);
        listView.setSelector(R.drawable.custom_selector);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter.isIndexSelected(DrivingRecordAdapter.NO_VALUE_SELECTED)){
                    try{
                        actionMode = getActivity().startActionMode(new EditDeleteActionMode(adapter, getShowDialog(), drivingRecordDao.getDao()));
                        adapter.setSelected(position);
                    } catch (SQLException e) {
                        Toast.makeText(getActivity(), "Unable to select item", Toast.LENGTH_LONG);
                    }
                } else if (!adapter.isIndexSelected(position)) {
                    adapter.setSelected(position);
                } else if (actionMode != null){
                    actionMode.finish();
                    actionMode = null;
                }

            }
        });

        return view;

    }

    private ShowDialog getShowDialog(){
        return new ShowDialog() {
            @Override
            public void showDialog(Record recordToEdit) {
                try {
                    FragmentManager fm = getChildFragmentManager();
                    InsertOrEditRecordDialog insertRecordDialog = getInsertRecordDialog(recordToEdit);
                    insertRecordDialog.show(fm, "Insert Record Dialog");
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Unable to create view", Toast.LENGTH_LONG);
                    Log.i(TAG, e.getMessage());
                }
            }

            private InsertOrEditRecordDialog getInsertRecordDialog(Record recordToEdit) throws SQLException {
                List<Task> drivingTaskList = drivingTaskDao.getDao().queryForAll();
                return new InsertOrEditRecordDialog(recordToEdit, drivingTaskList, drivingRecordDao);
            }
        };
    }
}
