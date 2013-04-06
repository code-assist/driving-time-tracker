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

import io.ehdev.android.drivingtime.view.DisplayRecordRow;
import org.joda.time.Duration;

public class AggregatedDrivingRecord {

    private String recordTitle;
    private Duration requiredHours;
    private Duration loggedHours;

    public AggregatedDrivingRecord(String recordTitle, Duration requiredHours, Duration loggedHours){
        this.recordTitle = recordTitle;
        this.requiredHours = requiredHours;
        this.loggedHours = loggedHours;
    }

    public String getRecordTitle() {
        return recordTitle;
    }

    public long getRequiredHours() {
        return requiredHours.getStandardHours();
    }

    public long getLoggedHours() {
        return loggedHours.getStandardHours();
    }

    public void setView(DisplayRecordRow record){
        setProgressBar(record);
        setTextField(record);
    }

    private void setTextField(DisplayRecordRow record) {
        record.getLeftText().setText(getRecordTitle());
        String percentageDriven = String.format("%d%%", (int)(100 * getLoggedHours() / getRequiredHours()));
        record.getRightText().setText(percentageDriven);
    }

    private void setProgressBar(DisplayRecordRow record) {
        record.setMaxOfProgress(getRequiredHours());

        if(getRequiredHours() <= getLoggedHours() )
            record.setCurrentProgress(getRequiredHours());
        else
            record.setCurrentProgress(getLoggedHours());
    }
}
