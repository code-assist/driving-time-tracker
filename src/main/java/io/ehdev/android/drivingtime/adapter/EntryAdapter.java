package io.ehdev.android.drivingtime.adapter;

import android.widget.BaseAdapter;

public abstract class EntryAdapter<T> extends BaseAdapter implements ReplaceDataSetAdapter<T> {
    static final public int NO_VALUE_SELECTED = -1;

    private int selected = -1;

    abstract public T getItem(int position);

    public void setSelected(int index){
        this.selected = index;
        notifyDataSetChanged();
    }

    public int getSelectedIndex(){
        return selected;
    }

    public boolean isIndexSelected(int index){
        return index == selected;
    }

    public void clearSelected(){
        setSelected(NO_VALUE_SELECTED);
    }
}
