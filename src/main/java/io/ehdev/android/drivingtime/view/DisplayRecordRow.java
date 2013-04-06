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

package io.ehdev.android.drivingtime.view;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DisplayRecordRow extends RelativeLayout {

    TextView rightText, leftText;
    ProgressBar progressBar;

    public TextView getLeftText(){
        return leftText;
    }

    public TextView getRightText(){
        return rightText;
    }

    public void setMaxOfProgress(float maxValue){
        progressBar.setMax((int)maxValue * 100);
    }

    public void setCurrentProgress(float currentValue){
        progressBar.setProgress((int)currentValue * 100);
    }

    public DisplayRecordRow(Context context, int left_p, int top_p, int right_p, int bottom_p ) {
        super(context);

        setPadding(left_p, top_p, right_p, bottom_p);

        createProgressBar(context);
        createLeftText(context);
        createRightText(context);

        //Set up the relitive layout
        createLeftLayout();
        createRightLayout();
        createProgressBarLayout();
    }

    private void createProgressBarLayout() {
        LayoutParams progressBarLayout =
                new LayoutParams(LayoutParams.MATCH_PARENT,
                        100);

        Log.i(this.getClass().getSimpleName(), "ID: " + leftText.getId());

        progressBarLayout.addRule(RelativeLayout.BELOW, leftText.getId());
        progressBarLayout.addRule(RelativeLayout.BELOW, rightText.getId());
        addView(progressBar, progressBarLayout);
    }

    private void createLeftLayout() {
        LayoutParams left =
                new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

        left.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        left.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        addView(leftText, left);
    }

    private void createRightLayout() {
        LayoutParams right =
                new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

        right.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        right.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addView(rightText, right);
    }


    private void createProgressBar(Context context) {
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 100, getResources().getDisplayMetrics());
        progressBar = new ProgressBar(context, null, R.attr.progressBarStyleHorizontal);
        progressBar.setPadding(10, 15, 10, 15);
        progressBar.setMax(100);
        progressBar.setProgress(50);
        progressBar.setMinimumHeight(value);
        progressBar.setMinimumWidth(value);
    }

    private void createRightText(Context context) {
        rightText = new TextView(context);
        rightText.setText("left");
        rightText.setTextSize(18);
        rightText.setTextColor(Color.BLACK);
        rightText.setId(1);
    }

    private void createLeftText(Context context) {
        leftText = new TextView(context);
        leftText.setText("right");
        leftText.setTextSize(18);
        leftText.setTextColor(Color.BLACK);
        leftText.setId(2);
    }

    public DisplayRecordRow(Context context){
        this(context, 12, 3, 10, 3);
    }
}
