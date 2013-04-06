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

package io.ehdev.android.drivingtime.view.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import com.actionbarsherlock.app.SherlockDialogFragment;
import io.ehdev.android.drivingtime.R;
import io.ehdev.android.drivingtime.database.DrivingRecord;
import io.ehdev.android.drivingtime.database.DrivingTask;
import org.joda.time.DateTime;

public class InsertRecordDialog extends SherlockDialogFragment {

    private static final String TAG = InsertRecordDialog.class.getSimpleName();

    private DateTime dateTimeForEntry;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceDialog){
        dateTimeForEntry = new DateTime();
        AlertDialog.Builder builder = createDialogAddButtons();
        builder.setTitle("Add Driving Time");
        return builder.create();
    }


    private AlertDialog.Builder createDialogAddButtons() {
        AlertDialog.Builder builder = createDialogBuilder();
        setOKButton(builder);
        setCancelButton(builder);
        return builder;
    }

    private void setCancelButton(AlertDialog.Builder builder) {
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InsertRecordDialog.this.getDialog().cancel();
            }
        });
    }

    private void setOKButton(AlertDialog.Builder builder) {
        builder.setPositiveButton(R.string.addRecord, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog thisDialog = InsertRecordDialog.this.getDialog();
                DrivingTask task = (DrivingTask) ((Spinner) thisDialog.findViewById(R.id.drivingTypeSpinner)).getSelectedItem();
                Long durationOfEntry = getDurationOfEntry(thisDialog);
                new DrivingRecord(task, dateTimeForEntry, durationOfEntry);
                //TODO: add entry to DB

                thisDialog.dismiss();
            }
        });
    }

    private Long getDurationOfEntry(Dialog thisDialog) {
        TimePicker timePicker = ((TimePicker)thisDialog.findViewById(R.id.durationPicker));
        return (long)timePicker.getCurrentHour() * 60 + timePicker.getCurrentMinute();
    }

    private AlertDialog.Builder createDialogBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.insert_record_dialog, null);
        setCurrentDateTime(v);
        setTimeViewParameters(v);
        builder.setView(v);
        return builder;
    }

    private void setCurrentDateTime(View v) {
        setupDateButtonText(v);
        setupDateButtonDialog(v);
        setupTimeButtonText(v);
        setupTimeButtonDialog(v);
    }

    private void setupTimeButtonText(View v) {
        java.text.DateFormat formatter = DateFormat.getTimeFormat(getSherlockActivity());
        ((Button)v.findViewById(R.id.setTime)).setText(formatter.format(dateTimeForEntry.toDate()));
    }

    private void setupTimeButtonDialog(View v) {
        (v.findViewById(R.id.setTime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog =
                        new TimePickerDialog(
                                getSherlockActivity(),
                                new DateTimeSelector(),
                                dateTimeForEntry.getHourOfDay(),
                                dateTimeForEntry.getMinuteOfHour(),
                                DateFormat.is24HourFormat(getSherlockActivity()));
                timePickerDialog.show();
            }
        });
    }

    private void setupDateButtonText(View v) {
        java.text.DateFormat formatter = DateFormat.getLongDateFormat(getSherlockActivity());
        ((Button)v.findViewById(R.id.setDate)).setText(formatter.format(dateTimeForEntry.toDate()));
    }

    private void setupDateButtonDialog(View v) {
        (v.findViewById(R.id.setDate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(
                                getSherlockActivity(),
                                new DateTimeSelector(),
                                dateTimeForEntry.getYear(),
                                dateTimeForEntry.getMonthOfYear(),
                                dateTimeForEntry.getDayOfMonth());
                datePickerDialog.show();
            }
        });
    }

    private class DateTimeSelector implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Log.d(TAG, String.format("year: %d, month: %d, day: %d", year, monthOfYear,dayOfMonth));
            dateTimeForEntry = dateTimeForEntry.withYear(year);
            dateTimeForEntry = dateTimeForEntry.withMonthOfYear(monthOfYear);
            dateTimeForEntry = dateTimeForEntry.withDayOfMonth(dayOfMonth);
            setupDateButtonText(InsertRecordDialog.this.getDialog().findViewById(R.id.setDate));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Log.d(TAG, String.format("hour: %d, min: %d", hourOfDay, minute));
            dateTimeForEntry = dateTimeForEntry.withHourOfDay(hourOfDay);
            dateTimeForEntry = dateTimeForEntry.withMinuteOfHour(minute);
            setupTimeButtonText(InsertRecordDialog.this.getDialog().findViewById(R.id.setTime));
        }
    }

    private void setTimeViewParameters(View view) {
        ((TimePicker)view.findViewById(R.id.durationPicker)).setIs24HourView(true);
        ((TimePicker)view.findViewById(R.id.durationPicker)).setCurrentHour(1);
        ((TimePicker)view.findViewById(R.id.durationPicker)).setCurrentMinute(0);
    }
}