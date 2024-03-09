package model;

import constans.TaskStatus;
import constans.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвечающий за работу эпиков
 */
public class Epic extends Task {

    /**
     * Список подзадач
     */
    final private List<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }

    @Override
    public void setStatus(TaskStatus status) {
        updateStatus();
    }

    /**
     * Метод расчета статуса эпика
     */
    public void updateStatus() {
        List<SubTask> tasks = getSubTasks();
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

    /**
     * Вовзращает все подзадачи
     * @return подзадачи
     */
    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    /**
     * Добавляет подзадачу в эписк
     * @param task подзадача
     */
    public void addSubTask(SubTask task) {
        subTasks.add(task);
        updateStatus();
    }

    /**
     * Удаляет подзадачу в эпике по ID
     * @param subTaskId ID подзадачи
     */
    public void removeSubTask(int subTaskId) {
        for (SubTask task : getSubTasks()) {
            if (task.getId() == subTaskId) {
                subTasks.remove(task);
                updateStatus();
            }
        }
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }
}
