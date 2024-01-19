package history;

import model.BaseTask;
import java.util.List;

/**
 * Интерфейс для работы с историей задач
 */
public interface TaskHistoryManager {
    /**
     * Константа, обозначает максимальное количество задач в списке истории
     */
    int MAX_VALUE = 10;

    /**
     * Метод добавления задачи в историю
     * @param task задача
     */
    void add(BaseTask task);

    /**
     * Возвращает список задач в истории
     * @return список задач истории
     */
    List<BaseTask> getHistory();

    /**
     * Очистка истории
     */
    void clearHistory();
}
