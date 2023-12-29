package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс отвечающий за работу с задачами
 */
public class TaskManager {

    /**
     * Внутренний счетчик для ID задач
     */
    private int idTasks = 0;

    /**
     * Список обычных задач
     */
    private HashMap<Integer, Task> taskList = new HashMap<>();

    /**
     * Список подзадач
     */
    private HashMap<Integer, SubTask> subTaskList = new HashMap<>();

    /**
     * Список эпиков
     */
    private HashMap<Integer, Epic> epicList = new HashMap<>();

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
    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(taskList.values());
    }

    /**
     * Возвращает список подзадач
     * @return список подзадач
     */
    public ArrayList<SubTask> getSubTaskList() {
        return new ArrayList<>(subTaskList.values());
    }

    /**
     * Возвращает список эпиков
     * @return список эпиков
     */
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epicList.values());
    }

    /**
     * Добавляет задачу в внутренний список
     * @param task задача
     */
    public void addTask(Task task) {
        task.setId(getNextTaskId());
        taskList.put(task.getId(), task);
    }

    /**
     * Добавляет эпик в внутренний список
     * @param epic эпик
     */
    public void addEpic(Epic epic) {
        epic.setId(getNextTaskId());
        epicList.put(epic.getId(), epic);
    }

    /**
     * Добавляет подзадачу в внутренний список
     * @param epicId ID эпика
     * @param subTask подзадача
     * @throws Exception исключение, если найден эпик по ID
     */
    public void addSubTask(int epicId, SubTask subTask) throws Exception {
        Epic epic = getEpicById(epicId);
        if (epic == null) {
            throw new Exception("Ошибка создания подзадачи. Не найден эпик по ID = " + epicId);
        }

        subTask.setId(getNextTaskId());
        subTask.setParent(epic);
        epic.addSubTask(subTask);
        epic.updateStatus();

        subTaskList.put(subTask.getId(), subTask);
    }

    /**
     * Обновление задачи
     * @param id ID задачи для обноление
     * @param task Новая модель задачи
     */
    public void updateTask(int id, Task task) {
        task.setId(id);
        taskList.put(id, task);
    }

    /**
     * Обновление подзадачи
     * @param id ID подзадачи
     * @param subTask новая модель подзадачи
     */
    public void updateSubTask(int id, SubTask subTask) {
        subTask.setId(id);
        subTaskList.put(id, subTask);
    }

    /**
     * Обновление эпика
     * @param id ID эпика
     * @param epic новая модель эпика
     */
    public void updateEpic(int id, Epic epic) {
        epic.setId(id);
        epicList.put(id, epic);
    }

    /**
     * Вовзращает задачу по ID
     * @param id ID задачи
     * @return задача
     */
    public Task getTaskById(int id) {
        return taskList.get(id);
    }

    /**
     * Возвращает подзадачу по ID
     * @param id ID подзадачи
     * @return подзадача
     */
    public SubTask getSubTaskById(int id) {
        return subTaskList.get(id);
    }

    /**
     * Возвращает эпик по ID
     * @param id ID эпика
     * @return эпик
     */
    public Epic getEpicById(int id) {
        return epicList.get(id);
    }

    /**
     * Возвращает список подзадач эпика
     * @param epicId ID эпика
     * @return список подзадач эпика
     * @throws Exception исключение, если эпик не найден
     */
    public ArrayList<SubTask> getSubTasksEpic(int epicId) throws Exception {
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
    public void removeTaskById(Integer id) {
        taskList.remove(id);
    }

    /**
     * Удаление подзадачи по ID
     * @param id ID подздачи
     */
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
    public void removeAll() {
        taskList.clear();
        subTaskList.clear();
        epicList.clear();
    }
}
