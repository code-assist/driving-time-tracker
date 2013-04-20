package io.ehdev.android.drivingtime.backend.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.joda.time.Duration;


@DatabaseTable(tableName= Task.TABLE_NAME)
public class Task {

    public final static String TABLE_NAME = "task";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String taskName;

    @DatabaseField(canBeNull = false, dataType= DataType.SERIALIZABLE)
    private Duration requiredHours;


    public Task() {}

    public Task(String taskName, Duration requiredHours) {
        this.taskName = taskName;
        this.requiredHours = requiredHours;
    }

    public int getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Duration getRequiredHours() {
        return requiredHours;
    }

    public void setRequiredHours(Duration requiredHours) {
        this.requiredHours = requiredHours;
    }

    public String toString(){
        return taskName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (!requiredHours.equals(task.requiredHours)) return false;
        if (!taskName.equals(task.taskName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + taskName.hashCode();
        result = 31 * result + requiredHours.hashCode();
        return result;
    }
}
