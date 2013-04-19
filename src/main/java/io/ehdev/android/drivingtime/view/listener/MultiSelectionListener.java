package io.ehdev.android.drivingtime.view.listener;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import io.ehdev.android.drivingtime.R;

@Deprecated
public class MultiSelectionListener implements AbsListView.MultiChoiceModeListener {

    private int numberOfItemsSelected = 0;
    private MenuItem editMenu;
    private ListView listView;

    public MultiSelectionListener(ListView listView){

        this.listView = listView;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position,
                                          long id, boolean checked) {
        if(checked)
            numberOfItemsSelected++;
        else
            numberOfItemsSelected--;

        editMenu.setEnabled(numberOfItemsSelected == 1);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        // Respond to clicks on the actions in the CAB
        switch (item.getItemId()) {
            case R.id.menu_delete:
                //deleteSelectedItems();
                mode.finish(); // Action picked, so close the CAB
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate the menu for the CAB
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.records_selected_menu, menu);
        editMenu = menu.findItem(R.id.menu_edit);
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        // Here you can make any necessary updates to the activity when
        // the CAB is removed. By default, selected items are deselected/unchecked.
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // Here you can perform updates to the CAB due to
        // an invalidate() request
        return false;
    }
}
