/*
 * Copyright (C) 2013 by Ethan Hall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.ehdev.android.drivingtime.view.activity;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.j256.ormlite.table.TableUtils;
import dagger.ObjectGraph;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.database.dao.DatabaseHelper;
import io.ehdev.android.drivingtime.module.ModuleGetters;
import io.ehdev.android.drivingtime.view.PostEditExecution;
import io.ehdev.android.drivingtime.view.dialog.EditTaskDialog;
import io.ehdev.android.drivingtime.view.dialog.InsertOrEditRecordDialog;
import io.ehdev.android.drivingtime.view.dialog.InsertOrEditTaskDialog;
import io.ehdev.android.drivingtime.view.dialog.InsertRecordDialogNoUpdate;
import io.ehdev.android.drivingtime.view.fragments.AllDrivingRecordReviewFragment;
import io.ehdev.android.drivingtime.view.fragments.MainFragment;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RootActivity extends Activity implements ActionBar.TabListener {

    private static final String TAG = RootActivity.class.getName();

    private Fragment listOfFragments[];

    @Inject
    protected DatabaseHelper databaseHelper;

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this); // Add this method.
        GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(this);
        googleAnalytics.getDefaultTracker().sendView("/HomeScreen");
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this); // Add this method.
    }

    @Override
    protected void onResume(){
        super.onResume();
        ActionBar.Tab tab = getActionBar().getSelectedTab();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(listOfFragments[tab.getPosition()]);
        ft.attach(listOfFragments[tab.getPosition()]);
        ft.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ObjectGraph objectGraph = ObjectGraph.create(ModuleGetters.getInstance(this));
        objectGraph.inject(this);


        listOfFragments = new Fragment[]{
                MainFragment.instantiate(this, MainFragment.class.getName()),
                AllDrivingRecordReviewFragment.instantiate(this, AllDrivingRecordReviewFragment.class.getName())
        };

        getActionBar().addTab(
                getActionBar()
                        .newTab()
                        .setTabListener(this)
                        .setText("Overview"));

        getActionBar().addTab(
                getActionBar()
                        .newTab()
                        .setTabListener(this)
                        .setText("Review Entries"));
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getActionBar().setDisplayShowTitleEnabled(true);

        if(savedInstanceState != null){
            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab"));
        }
    }

    protected void onSaveInstanceState (Bundle outState){
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.preferences_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.add:
                createAddEntry();
                return true;
            case R.id.tasks:
                launchTaskActivity();
                return true;
            case R.id.about:
                launchAboutActivity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchAboutActivity() {
        Intent aboutIntent = new Intent();
        aboutIntent.setClass(this, AboutActivity.class);
        startActivity(aboutIntent);
    }

    private void launchTaskActivity() {
        Intent taskIntent = new Intent();
        taskIntent.setClass(this, TaskConfigurationActivity.class);
        startActivity(taskIntent);
    }

    private void createAddEntry() {
        try{
            FragmentManager fm = getFragmentManager();
            DialogFragment insertRecordDialog = getInsertRecordDialog();
            insertRecordDialog.show(fm, "Insert Record Dialog");
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    private DialogFragment getInsertRecordDialog() throws SQLException {
        List<Task> drivingTaskList = databaseHelper.getTaskDao().queryForAll();
        if(drivingTaskList.size() == 0){
            return new EditTaskDialog(new Task("", Duration.standardHours(1)), new PostEditExecution() {
                @Override
                public void execute() {
                }
            });
        } else {
            Record drivingRecord = new Record(drivingTaskList.get(0), new DateTime(), Duration.standardHours(1));
            return new InsertRecordDialogNoUpdate(drivingRecord, drivingTaskList, reloadView());
        }

    }

    private InsertRecordDialogNoUpdate.ReloadView reloadView() {
        return new InsertRecordDialogNoUpdate.ReloadView(){

            @Override
            public void reload() {
                getActionBar().setSelectedNavigationItem(getActionBar().getSelectedNavigationIndex());
            }
        };
    }


    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if(listOfFragments[tab.getPosition()].isDetached())
            ft.attach(listOfFragments[tab.getPosition()]);
        else {
            ft.replace(android.R.id.content, listOfFragments[tab.getPosition()]);
        }
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.detach(listOfFragments[tab.getPosition()]);
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.detach(listOfFragments[tab.getPosition()]);
        ft.attach(listOfFragments[tab.getPosition()]);
    }

}
