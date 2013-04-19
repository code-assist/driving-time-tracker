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
import io.ehdev.android.drivingtime.backend.AggregatedRecord;
import io.ehdev.android.drivingtime.view.entry.DisplayProgressRecordRow;

import java.util.List;

public class AggregatedDrivingRecordAdapter extends BaseAdapter {

    private Context viewContext;
    private List<AggregatedRecord> recordList;

    public AggregatedDrivingRecordAdapter(Context viewContext, List<AggregatedRecord> recordList){
        this.viewContext = viewContext;
        this.recordList = recordList;
    }
    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public AggregatedRecord getItem(int i) {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setAggregatedDrivingRecord(List<AggregatedRecord> recordList){
        this.recordList = recordList;
        notifyDataSetInvalidated();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(null == view || !(view instanceof DisplayProgressRecordRow))
            view = new DisplayProgressRecordRow(viewContext);

        AggregatedRecord aggRecord = recordList.get(i);
        DisplayProgressRecordRow displayRecordRow = (DisplayProgressRecordRow) view;
        updateViewFields(aggRecord, displayRecordRow);

        return view;
    }

    private void updateViewFields(AggregatedRecord aggRecord, DisplayProgressRecordRow displayRecordRow) {
        displayRecordRow.setMaxOfProgress(100);
        displayRecordRow.setCurrentProgress(aggRecord.getPercentageComplete() * 100);
        displayRecordRow.setLeftText(aggRecord.getTaskName());
        displayRecordRow.setRightText(String.format("%2d%%", (int)(aggRecord.getPercentageComplete() * 100)));
    }
}
