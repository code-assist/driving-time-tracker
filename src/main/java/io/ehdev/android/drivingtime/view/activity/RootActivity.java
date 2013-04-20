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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.j256.ormlite.table.TableUtils;
import io.ehdev.android.drivingtime.backend.model.Record;
import io.ehdev.android.drivingtime.backend.model.Task;
import io.ehdev.android.drivingtime.database.dao.DrivingRecordDao;
import io.ehdev.android.drivingtime.database.dao.DrivingTaskDao;
import io.ehdev.android.drivingtime.view.fragments.ListDrivingRecordsFragment;
import io.ehdev.android.drivingtime.view.fragments.MainFragment;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.sql.SQLException;

public class RootActivity extends SherlockFragmentActivity implements ActionBar.TabListener {

    private DrivingTaskDao drivingTaskDao;
    private DrivingRecordDao drivingRecordDao;
    static private Fragment listOfFragments[];

    private static final String TAG = RootActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setupTempDatabase();

            listOfFragments = new Fragment[]{
                    new MainFragment(),
                    new ListDrivingRecordsFragment()
            };
        }

        getSupportActionBar().addTab(
                getSupportActionBar()
                        .newTab()
                        .setTabListener(this)
                        .setText("Overview"));

        getSupportActionBar().addTab(
                getSupportActionBar()
                        .newTab()
                        .setTabListener(this)
                        .setText("Review Entries"));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        if(savedInstanceState != null){
            getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab"));
        }
    }

    private void setupTempDatabase() {
        drivingRecordDao = new DrivingRecordDao(this);
        drivingTaskDao = new DrivingTaskDao(this);

        try{
            TableUtils.dropTable(drivingTaskDao.getConnectionSource(), Task.class, true);
            TableUtils.dropTable(drivingTaskDao.getConnectionSource(), Record.class, true);

            TableUtils.createTable(drivingTaskDao.getConnectionSource(), Task.class);
            TableUtils.createTable(drivingTaskDao.getConnectionSource(), Record.class);

            buildTempDatabase();
        } catch (Exception e){
            Log.i(TAG, e.getMessage());
        }
    }

    private void buildTempDatabase() throws SQLException {
        Task drivingTask1 = new Task("Highway", Duration.standardHours(40));
        Task drivingTask2 = new Task("Night", Duration.standardHours(8));
        drivingTaskDao.getDao().create(drivingTask1);
        drivingTaskDao.getDao().create(drivingTask2);

        Record drivingRecord = new Record(drivingTask1, DateTime.now().minusHours(15), Duration.standardHours(10));
        Record drivingRecord2 = new Record(drivingTask2, DateTime.now().minusHours(15), Duration.standardHours(6));
        drivingRecordDao.getDao().create(drivingRecord);
        drivingRecordDao.getDao().create(drivingRecord2);
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if(listOfFragments[tab.getPosition()].isDetached())
            ft.attach(listOfFragments[tab.getPosition()]);
        else
            ft.replace(android.R.id.content, listOfFragments[tab.getPosition()]);
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.remove(listOfFragments[tab.getPosition()]);
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }

}
