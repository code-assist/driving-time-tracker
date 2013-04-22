package io.ehdev.android.drivingtime.view.fragments;

import android.app.Fragment;
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
import io.ehdev.android.drivingtime.adapter.EntryAdapter;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.database.dao.DatabaseHelper;
import io.ehdev.android.drivingtime.view.PostEditExecution;
import io.ehdev.android.drivingtime.view.dialog.ShowDialog;

import java.sql.SQLException;

public abstract class AbstractListDrivingRecordFragment extends Fragment {

    private static final String TAG = AbstractListDrivingRecordFragment.class.getName();

    private ActionMode actionMode;
    private EntryAdapter<Record> adapter;

    abstract protected DatabaseHelper getDatabaseHelper();

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

    public EntryAdapter<Record> getAdapter() {
        return adapter;
    }

    public void setAdapter(EntryAdapter<Record> adapter) {
        this.adapter = adapter;
    }

    protected int getViewId(){
        return R.layout.detailed_list_view;
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
                if(adapter.isIndexSelected(EntryAdapter.NO_VALUE_SELECTED)){
                    try{
                        actionMode = getActivity().startActionMode(new EditDeleteActionMode<Record>(adapter, getShowDialog(), getDatabaseHelper().getDao(adapter.getClassName()), getReloadAdapter()));
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

    abstract protected ShowDialog<Record> getShowDialog();

    abstract protected PostEditExecution getReloadAdapter();

    public static class AdapterNotSetException extends RuntimeException {
    }
}
