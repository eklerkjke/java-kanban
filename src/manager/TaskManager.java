package manager;

import model.BaseTask;
import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Интерфейс для работы с менеджерами задач
 */
public interface TaskManager {
    /**
     * Должен возвращать список задач
     * @return список задач
     */
    ArrayList<Task> getTaskList();

    /**
     * Возвращает список подзадач
     * @return список подзадач
     */
    ArrayList<SubTask> getSubTaskList();

    /**
     * Возвращает список эпиков
     * @return список эпиков
     */
    ArrayList<Epic> getEpicList();

    /**
     * Добавляет задачу в внутренний список
     * @param task задача
     */
    void addTask(Task task);

    /**
     * Добавляет эпик в внутренний список
     * @param epic эпик
     */
    void addEpic(Epic epic);

    /**
     * Добавляет подзадачу в внутренний список
     * @param subTask подзадача
     */
    void addSubTask(SubTask subTask);

    /**
     * Обновление задачи
     * @param task Новая модель задачи
     */
    void updateTask(Task task);

    /**
     * Обновление подзадачи
     * @param subTask новая модель подзадачи
     */
    void updateSubTask(SubTask subTask);

    /**
     * Обновление эпика
     * @param epic новая модель эпика
     */
    void updateEpic(Epic epic);

    /**
     * Вовзращает задачу по ID
     * @param id ID задачи
     * @return задача
     */
    Task getTaskById(int id);

    /**
     * Возвращает подзадачу по ID
     * @param id ID подзадачи
     * @return подзадача
     */
    SubTask getSubTaskById(int id);

    /**
     * Возвращает эпик по ID
     * @param id ID эпика
     * @return эпик
     */
    Epic getEpicById(int id);

    /**
     * Возвращает список подзадач эпика
     * @param epicId ID эпика
     * @return список подзадач эпика
     * @throws Exception исключение, если эпик не найден
     */
    List<SubTask> getSubTasksEpic(int epicId) throws Exception;

    /**
     * Удаление задачи по ID
     * @param id ID задачи
     */
    void removeTaskById(Integer id);

    /**
     * Удаление подзадачи по ID
     * @param id ID подздачи
     */
    void removeSubTaskById(Integer id);

    /**
     * Удаление эпика по ID
     * @param id ID эпика
     */
    void removeEpicById(Integer id);

    /**
     * Удаление всех задач
     */
    void removeAll();

    /**
     * Удаление всех задач
     */
    void removeTasks();

    /**
     * Удаление всех подзадач
     */
    void removeSubTasks();

    /**
     * Удаление всех эпиков
     */
    void removeEpics();

    /**
     * Возвращает список задач в истории
     * @return список задач в истории
     */
    List<BaseTask> getHistory();

    /**
     * Очистка истории
     */
    void clearHistory();
}
