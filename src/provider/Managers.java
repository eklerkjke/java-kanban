package provider;

import exceptions.ManagerSaveException;
import history.InMemoryTaskHistoryManager;
import history.TaskHistoryManager;
import manager.FileBackedTaskManager;
import manager.TaskManager;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Утилитарный класс для работы с менеджерами
 */
public class Managers {

    private Managers() {

    }

    /**
     * Возвращает менеджер задач по умолчанию
     * @return менеджер задач
     */
    public static TaskManager getDefault() throws ManagerSaveException {
        Path path = Paths.get("src/tasks.csv");
        return FileBackedTaskManager.loadFromFile(path.toFile());
    }

    /**
     * Возвращает менеджер истории задач по умолчанию
     * @return менеджер истории задач
     */
    public static TaskHistoryManager getDefaultHistory() {
        return new InMemoryTaskHistoryManager();
    }
}
