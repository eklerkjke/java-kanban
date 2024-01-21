package manager;

import history.TaskHistoryManager;
import model.Epic;
import model.SubTask;
import model.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс отвечающий за работу с задачами
 */
public class InMemoryTaskManager implements TaskManager {

    /**
     * Внутренний счетчик для ID задач
     */
    private int idTasks = 0;

    /**
     * Список обычных задач
     */
    private Map<Integer, Task> taskList = new HashMap<>();

    /**
     * Список подзадач
     */
    private Map<Integer, SubTask> subTaskList = new HashMap<>();

    /**
     * Список эпиков
     */
    private Map<Integer, Epic> epicList = new HashMap<>();

    private TaskHistoryManager historyManager;

    public InMemoryTaskManager(TaskHistoryManager historyManager) {
        this.historyManager = historyManager;
        idTasks = 0;
    }

    /**
     * Возвращает инкрементированный ID задачи
     * @return ID задачи
     */
    private int getNextTaskId() {
        ++idTasks;
        return idTasks;
    }

    /**
     * Возвращает список обычных задач
     * @return список обычных задач
     */
    @Override
    public List<Task> getTaskList() {
        return new ArrayList<>(taskList.values());
    }

    /**
     * Возвращает список подзадач
     * @return список подзадач
     */
    @Override
    public List<SubTask> getSubTaskList() {
        return new ArrayList<>(subTaskList.values());
    }

    /**
     * Возвращает список эпиков
     * @return список эпиков
     */
    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epicList.values());
    }

    /**
     * Добавляет задачу в внутренний список
     * @param task задача
     */
    @Override
    public void addTask(Task task) {
        task.setId(getNextTaskId());
        taskList.put(task.getId(), task);
    }

    /**
     * Добавляет эпик в внутренний список
     * @param epic эпик
     */
    @Override
    public void addEpic(Epic epic) {
        epic.setId(getNextTaskId());
        epicList.put(epic.getId(), epic);
    }

    /**
     * Добавляет подзадачу в внутренний список
     * @param subTask подзадача
     */
    @Override
    public void addSubTask(SubTask subTask) {
        Epic epic = subTask.getParent();
        subTask.setId(getNextTaskId());
        epic.addSubTask(subTask);

        subTaskList.put(subTask.getId(), subTask);
    }

    /**
     * Обновление задачи
     * @param task Новая модель задачи
     */
    @Override
    public void updateTask(Task task) {
        taskList.remove(task.getId());
        taskList.put(task.getId(), task);
    }

    /**
     * Обновление подзадачи
     * @param subTask новая модель подзадачи
     */
    @Override
    public void updateSubTask(SubTask subTask) {
        subTaskList.remove(subTask.getId());
        subTaskList.put(subTask.getId(), subTask);
        subTask.getParent().updateStatus();
    }

    /**
     * Обновление эпика
     * @param epic новая модель эпика
     */
    @Override
    public void updateEpic(Epic epic) {
        epicList.remove(epic.getId());
        epicList.put(epic.getId(), epic);
    }

    /**
     * Вовзращает задачу по ID
     * @param id ID задачи
     * @return задача
     */
    @Override
    public Task getTaskById(int id) {
        Task task = taskList.get(id);
        if (task != null) {
            historyManager.add(this, task);
        }
        return task;
    }

    /**
     * Возвращает подзадачу по ID
     * @param id ID подзадачи
     * @return подзадача
     */
    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTaskList.get(id);
        if (subTask != null) {
            historyManager.add(this, subTask);
        }
        return subTask;
    }

    /**
     * Возвращает эпик по ID
     * @param id ID эпика
     * @return эпик
     */
    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicList.get(id);
        if (epic != null) {
            historyManager.add(this, epic);
        }
        return epic;
    }

    /**
     * Возвращает список подзадач эпика
     * @param epicId ID эпика
     * @return список подзадач эпика
     * @throws Exception исключение, если эпик не найден
     */
    @Override
    public List<SubTask> getSubTasksEpic(int epicId) throws Exception {
        Epic epic = getEpicById(epicId);
        if (epic == null) {
            throw new Exception("Ошибка. Не найден эпик по ID = " + epicId);
        }

        return epic.getSubTasks();
    }

    /**
     * Удаление задачи по ID
     * @param id ID задачи
     */
    @Override
    public void removeTaskById(Integer id) {
        taskList.remove(id);
    }

    /**
     * Удаление подзадачи по ID
     * @param id ID подздачи
     */
    @Override
    public void removeSubTaskById(Integer id) {
        SubTask subTask = getSubTaskById(id);
        Epic epic = subTask.getParent();
        epic.removeSubTask(id);
        subTaskList.remove(id);
    }

    /**
     * Удаление эпика по ID
     * @param id ID эпика
     */
    @Override
    public void removeEpicById(Integer id) {
        Epic epic = getEpicById(id);
        for (SubTask subTask : epic.getSubTasks()) {
            removeSubTaskById(subTask.getId());
        }
        epicList.remove(id);
    }

    /**
     * Удаление всех задач
     */
    @Override
    public void removeAll() {
        removeTasks();
        removeEpics();
    }

    /**
     * Удаление всех задач
     */
    @Override
    public void removeTasks() {
        taskList.clear();
    }

    /**
     * Удаление всех подзадач
     */
    @Override
    public void removeSubTasks() {
        subTaskList.clear();
    }

    /**
     * Удаление всех эпиков
     */
    @Override
    public void removeEpics() {
        removeSubTasks();
        epicList.clear();
    }

    /**
     * Возвращает список задач в истории
     * @return список задач в истории
     */
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
