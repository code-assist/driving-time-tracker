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

package io.ehdev.android.drivingtime.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import io.ehdev.android.drivingtime.adapter.pojo.AggregatedDrivingRecord;
import io.ehdev.android.drivingtime.view.DisplayRecordRow;

import java.util.List;

public class DrivingRecordAdapter extends BaseAdapter {

    private Context viewContext;
    private List<AggregatedDrivingRecord> recordList;

    public DrivingRecordAdapter(Context viewContext, List<AggregatedDrivingRecord> recordList){
        this.viewContext = viewContext;
        this.recordList = recordList;
    }
    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setAggregatedDrivingRecord(List<AggregatedDrivingRecord> recordList){
        this.recordList = recordList;
        notifyDataSetInvalidated();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(null == view || !(view instanceof DisplayRecordRow))
            view = new DisplayRecordRow(viewContext);

        DisplayRecordRow displayRecordRow = (DisplayRecordRow) view;
        recordList.get(i).setView(displayRecordRow);

        return view;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
