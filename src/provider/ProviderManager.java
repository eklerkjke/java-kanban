package provider;

import history.InMemoryTaskHistoryManager;
import history.TaskHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

public class ProviderManager {
    static TaskManager<Task, SubTask, Epic> taskManager;
    static TaskHistoryManager<Task> taskHistoryManager;

    private static TaskManager<Task, SubTask, Epic> getDefaultTaskManager() {
        return new InMemoryTaskManager(getTaskHistoryManager());
    }

    private static TaskHistoryManager<Task> getDefaultTaskHistoryManager() {
        return new InMemoryTaskHistoryManager();
    }

    public static TaskManager<Task, SubTask, Epic> getTaskManager() {
        if (taskManager == null) {
            taskManager = getDefaultTaskManager();
        }
        return taskManager;
    }

    public static TaskHistoryManager<Task> getTaskHistoryManager() {
        if (taskHistoryManager == null) {
            taskHistoryManager = getDefaultTaskHistoryManager();
        }
        return taskHistoryManager;
    }


}
