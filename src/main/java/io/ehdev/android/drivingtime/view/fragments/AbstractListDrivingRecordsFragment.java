package io.ehdev.android.drivingtime.view.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
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
import io.ehdev.android.drivingtime.StaticInstances;
import io.ehdev.android.drivingtime.adapter.DrivingRecordAdapter;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.view.dialog.EditRecordDialog;
import io.ehdev.android.drivingtime.view.dialog.InsertOrEditRecordDialog;
import io.ehdev.android.drivingtime.view.dialog.ShowDialog;

import java.sql.SQLException;
import java.util.List;

public abstract class AbstractListDrivingRecordsFragment extends Fragment {

    private static final String TAG = AbstractListDrivingRecordsFragment.class.getName();
    private DrivingRecordAdapter adapter;
    private ActionMode actionMode;
    private StaticInstances staticInstances;

    public AbstractListDrivingRecordsFragment(){
        super();
        staticInstances = StaticInstances.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(getViewId(), null);
        if(adapter == null)
            throw new AdapterNotSetException();
        setupListView(view);

        return view;
    }

    protected int getViewId(){
        return R.layout.detailed_list_view;
    }

    protected void setAdapter(DrivingRecordAdapter adapter){
        this.adapter = adapter;
    }

    public DrivingRecordAdapter getAdapter() {
        return adapter;
    }

    private void setupListView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.listOfAllRecords);
        listView.setAdapter(adapter);
        listView.setSelector(R.drawable.custom_selector);
        addOnItemClickListener(listView);
    }

    private void addOnItemClickListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter.isIndexSelected(DrivingRecordAdapter.NO_VALUE_SELECTED)){
                    try{
                        actionMode = getActivity().startActionMode(new EditDeleteActionMode(adapter, getShowDialog(), staticInstances.getDrivingRecordDao(), getReloadAdapter()));
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
                List<Task> drivingTaskList = staticInstances.getDrivingTaskDao().queryForAll();
                return new EditRecordDialog(recordToEdit, drivingTaskList, staticInstances.getDrivingRecordHelper(), getReloadAdapter());
            }
        };
    }

    abstract protected AsyncTask<Void, Void, List<Record>> getReloadAdapter();

    public static class AdapterNotSetException extends RuntimeException {
    }
}
