package io.ehdev.android.drivingtime.view.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import dagger.ObjectGraph;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.adapter.AggregatedDrivingRecordAdapter;
import io.ehdev.android.drivingtime.database.dao.DatabaseHelper;
import io.ehdev.android.drivingtime.module.ModuleGetters;
import io.ehdev.android.drivingtime.view.activity.ListEntriesForTaskActivity;

import javax.inject.Inject;

public class MainFragment extends Fragment {

    @Inject
    protected DatabaseHelper databaseHelper;

    private AggregatedDrivingRecordAdapter aggregatedDrivingRecordAdapter;

    private static final String TAG = MainFragment.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ObjectGraph objectGraph = ObjectGraph.create( ModuleGetters.getInstance());
        objectGraph.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        Log.d(MainFragment.class.getName(), "onCreateView");
        View view = inflater.inflate(R.layout.main, null);
        addAdapterToListView(view);
        return view;

    }

    private void addAdapterToListView(View view) {
        GridView newListView = (GridView)view.findViewById(R.id.currentStatusView);
        aggregatedDrivingRecordAdapter = new AggregatedDrivingRecordAdapter(getActivity(), databaseHelper.createDrivingRecordList());
        newListView.setAdapter(aggregatedDrivingRecordAdapter);
        newListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "OnClick");
                Intent newActivity = new Intent();
                newActivity.setClass(getActivity(), ListEntriesForTaskActivity.class);
                newActivity.putExtra("taskId", aggregatedDrivingRecordAdapter.getItem(position).getTaskId());
                getActivity().startActivity(newActivity);
            }
        });
    }
}
