package provider;

import history.InMemoryTaskHistoryManager;
import history.TaskHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;

/**
 * Утилитарный класс для работы с менеджерами
 */
public class Managers {
    /**
     * Менеджер задач
     */
    static TaskManager taskManager;

    /**
     * Менеджер истории задач
     */
    static TaskHistoryManager taskHistoryManager;

    /**
     * Возвращает менеджер задач по умолчанию
     * @return менеджер задач
     */
    private static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(getTaskHistoryManager());
    }

    /**
     * Возвращает менеджер истории задач по умолчанию
     * @return менеджер истории задач
     */
    private static TaskHistoryManager getDefaultTaskHistoryManager() {
        return new InMemoryTaskHistoryManager();
    }

    /**
     * Возввращает менеджер задач
     * @return менеджер задач
     */
    public static TaskManager getTaskManager() {
        if (taskManager == null) {
            taskManager = getDefaultTaskManager();
        }
        return taskManager;
    }

    /**
     * Возвращает менеджер истории задач
     * @return менеджер истории задач
     */
    public static TaskHistoryManager getTaskHistoryManager() {
        if (taskHistoryManager == null) {
            taskHistoryManager = getDefaultTaskHistoryManager();
        }
        return taskHistoryManager;
    }


}
