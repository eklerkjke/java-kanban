package history;

import manager.TaskManager;
import model.Task;

import java.util.List;

/**
 * Интерфейс для работы с историей задач
 */
public interface TaskHistoryManager {

    /**
     * Метод добавления задачи в историю
     * @param task задача
     */
    void add(TaskManager manager, Task task);

    void remove(int id);

    /**
     * Возвращает список задач в истории
     * @return список задач истории
     */
    List<Task> getHistory();
}
