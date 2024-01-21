package provider;

import history.InMemoryTaskHistoryManager;
import history.TaskHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;

/**
 * Утилитарный класс для работы с менеджерами
 */
final public class Managers {

    private Managers() {

    }

    /**
     * Возвращает менеджер задач по умолчанию
     * @return менеджер задач
     */
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    /**
     * Возвращает менеджер истории задач по умолчанию
     * @return менеджер истории задач
     */
    public static TaskHistoryManager getDefaultHistory() {
        return new InMemoryTaskHistoryManager();
    }
}
