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

package io.ehdev.android.drivingtime.adapter.pojo;

import io.ehdev.android.drivingtime.database.model.DrivingTask;
import io.ehdev.android.drivingtime.view.entry.DisplayProgressRecordRow;
import org.joda.time.Duration;

public class AggregatedDrivingRecord {

    private DrivingTask recordTitle;
    private Duration loggedHours;

    public AggregatedDrivingRecord(DrivingTask recordTitle, Duration loggedHours){
        this.recordTitle = recordTitle;
        this.loggedHours = loggedHours;
    }

    public String getRecordTitle() {
        return recordTitle.getTaskName();
    }

    public int getDrivingTaskId(){
        return recordTitle.getId();
    }

    public long getRequiredHours() {
        return recordTitle.getRequiredHours().getStandardHours();
    }

    public long getLoggedHours() {
        return loggedHours.getStandardHours();
    }

    public void setView(DisplayProgressRecordRow record){
        setProgressBar(record);
        setTextField(record);
    }

    private void setTextField(DisplayProgressRecordRow record) {
        record.getLeftText().setText(getRecordTitle());
        String percentageDriven;
        if(getLoggedHours() / getRequiredHours() >= 1)
            percentageDriven = String.format("100%%");
        else
            percentageDriven = String.format("%d%%", (int)(100 * getLoggedHours() / getRequiredHours()));

        record.getRightText().setText(percentageDriven);
    }

    private void setProgressBar(DisplayProgressRecordRow record) {
        record.setMaxOfProgress(getRequiredHours());

        if(getRequiredHours() <= getLoggedHours() )
            record.setCurrentProgress(getRequiredHours());
        else
            record.setCurrentProgress(getLoggedHours());
    }
}
