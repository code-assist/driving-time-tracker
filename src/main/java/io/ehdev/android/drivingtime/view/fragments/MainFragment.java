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
import io.ehdev.android.drivingtime.backend.AggregatedRecord;
import io.ehdev.android.drivingtime.backend.StringHelper;
import io.ehdev.android.drivingtime.database.dao.DatabaseHelper;
import io.ehdev.android.drivingtime.module.ModuleGetters;
import io.ehdev.android.drivingtime.view.activity.ListEntriesForTaskActivity;
import io.ehdev.android.drivingtime.view.entry.DisplayProgressRecordRow;
import org.joda.time.Duration;

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
        DisplayProgressRecordRow displayProgressRecordRow = (DisplayProgressRecordRow)view.findViewById(R.id.main_progress_bar);
        Duration requiredTime = Duration.millis(1);
        Duration spentTime = Duration.millis(1);
        for( int i = 0; i < aggregatedDrivingRecordAdapter.getCount(); i++){
            AggregatedRecord record = aggregatedDrivingRecordAdapter.getItem(i);
            requiredTime = requiredTime.plus(record.getRequiredTime());
            spentTime = spentTime.plus(record.getTimeSpent());
        }

        String timeRequired = String.format("Required %s", StringHelper.getPeriodAsString(requiredTime.toPeriod()));
        String timeRemaining = String.format("Remaining %s", StringHelper.getPeriodAsString(requiredTime.minus(spentTime).toPeriod()));
        displayProgressRecordRow.setRightText(timeRequired);
        displayProgressRecordRow.setLeftText(timeRemaining);
        displayProgressRecordRow.setCurrentProgress(100 * ((float)spentTime.getMillis() / requiredTime.getMillis()));
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
