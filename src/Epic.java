import java.util.ArrayList;
import java.util.Arrays;

public class Epic extends Task {

    protected ArrayList<SubTask> subTasks = new ArrayList<>();

    @Override
    public String toString() {
        return "Эпик: " +
                "нзвание='" + getName() + '\'' +
                ", описание='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", статус=" + getStatus();
    }

    @Override
    public void setStatus(TaskStatus status) {
        updateStatus();
    }

    protected void updateStatus() {
        ArrayList<SubTask> tasks = getSubTasks();
        if (tasks.isEmpty()) {
            status = TaskStatus.NEW;
            return;
        }

        ArrayList<TaskStatus> statuses = new ArrayList<>();
        for (SubTask subTask : tasks) {
            statuses.add(subTask.getStatus());
        }

        if (!statuses.contains(TaskStatus.IN_PROGRESS)) {
            if (!statuses.contains(TaskStatus.NEW)) {
                status = TaskStatus.DONE;
            } else if (!statuses.contains(TaskStatus.DONE)) {
                status = TaskStatus.NEW;
            }
        } else {
            status = TaskStatus.IN_PROGRESS;
        }
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask task) {
        subTasks.add(task);
    }

    public void removeSubTask(int subTaskId) {
        for (SubTask task : getSubTasks()) {
            if (task.getId() == subTaskId) {
                subTasks.remove(task);
            }
        }
    }
}
