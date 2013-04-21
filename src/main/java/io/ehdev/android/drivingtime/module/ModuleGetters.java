package io.ehdev.android.drivingtime.module;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import io.ehdev.android.drivingtime.database.dao.DatabaseHelper;
import io.ehdev.android.drivingtime.view.activity.RootActivity;
import io.ehdev.android.drivingtime.view.dialog.EditRecordDialog;
import io.ehdev.android.drivingtime.view.dialog.InsertOrEditRecordDialog;
import io.ehdev.android.drivingtime.view.fragments.AllDrivingRecordReviewFragment;
import io.ehdev.android.drivingtime.view.fragments.MainFragment;
import io.ehdev.android.drivingtime.view.fragments.TaskDrivingRecordReviewFragment;

import javax.inject.Singleton;

@Module(
        entryPoints = {
                RootActivity.class,
                MainFragment.class,
                AllDrivingRecordReviewFragment.class,
                TaskDrivingRecordReviewFragment.class,
                InsertOrEditRecordDialog.class,
                EditRecordDialog.class
        }
)
public class ModuleGetters {

    private Context context;

    public ModuleGetters(Context context){

        this.context = context;
    }

    @Provides
    @Singleton
    public DatabaseHelper getDatabaseHelper(){
        return new DatabaseHelper(context);
    }

}
