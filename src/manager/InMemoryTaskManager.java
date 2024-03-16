package manager;

import history.TaskHistoryManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    final protected Map<Integer, Task> taskList = new HashMap<>();

    /**
     * Список подзадач
     */
    final protected Map<Integer, SubTask> subTaskList = new HashMap<>();

    /**
     * Список эпиков
     */
    final protected Map<Integer, Epic> epicList = new HashMap<>();

    final protected TaskHistoryManager historyManager;

    private Set<Task> prioritizedTasks;

    public InMemoryTaskManager(TaskHistoryManager historyManager) {
        this.historyManager = historyManager;
        prioritizedTasks = new TreeSet<>(
            Comparator
                .comparing(Task::getStartTime)
                .thenComparing(Task::getEndTime)
        );
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
        checkTask(task);

        task.setId(getNextTaskId());
        taskList.put(task.getId(), task);
        addPrioritizedTasks(task);
    }

    /**
     * Добавляет эпик в внутренний список
     * @param epic эпик
     */
    @Override
    public void addEpic(Epic epic) {
        checkTask(epic);

        epic.setId(getNextTaskId());
        epicList.put(epic.getId(), epic);
        updateEpicTime(epic);
        addPrioritizedTasks(epic);
    }

    /**
     * Добавляет подзадачу в внутренний список
     * @param subTask подзадача
     */
    @Override
    public void addSubTask(SubTask subTask) {
        checkTask(subTask);

        Epic epic = getEpicById(subTask.getParentId());
        subTask.setId(getNextTaskId());
        epic.addSubTask(subTask);

        subTaskList.put(subTask.getId(), subTask);
        updateEpicTime(epic);
        addPrioritizedTasks(subTask);
    }

    /**
     * Обновление задачи
     * @param task Новая модель задачи
     */
    @Override
    public void updateTask(Task task) {
        checkTask(task);

        taskList.remove(task.getId());
        taskList.put(task.getId(), task);
        addPrioritizedTasks(task);
    }

    /**
     * Обновление подзадачи
     * @param subTask новая модель подзадачи
     */
    @Override
    public void updateSubTask(SubTask subTask) {
        checkTask(subTask);

        subTaskList.remove(subTask.getId());
        subTaskList.put(subTask.getId(), subTask);
        Epic epic = getEpicById(subTask.getParentId());
        epic.updateStatus();
        updateEpicTime(epic);
        addPrioritizedTasks(subTask);
    }

    /**
     * Обновление эпика
     * @param epic новая модель эпика
     */
    @Override
    public void updateEpic(Epic epic) {
        checkTask(epic);

        epicList.remove(epic.getId());
        epicList.put(epic.getId(), epic);
        updateEpicTime(epic);
        addPrioritizedTasks(epic);
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
        removePrioritizedTasks(taskList.get(id));
        taskList.remove(id);
        historyManager.remove(id);
    }

    /**
     * Удаление подзадачи по ID
     * @param id ID подздачи
     */
    @Override
    public void removeSubTaskById(Integer id) {
        SubTask subTask = getSubTaskById(id);
        Epic epic = getEpicById(subTask.getParentId());
        epic.removeSubTask(id);
        removePrioritizedTasks(subTask);
        subTaskList.remove(id);
        historyManager.remove(id);
        updateEpicTime(epic);
    }

    /**
     * Удаление эпика по ID
     * @param id ID эпика
     */
    @Override
    public void removeEpicById(Integer id) {
        Epic epic = getEpicById(id);
        removePrioritizedTasks(epic);
        for (SubTask subTask : epic.getSubTasks()) {
            removeSubTaskById(subTask.getId());
            historyManager.remove(subTask.getId());
        }
        epicList.remove(id);
        historyManager.remove(id);
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
        for (Task task : taskList.values()) {
            removePrioritizedTasks(task);
            historyManager.remove(task.getId());
        }
        taskList.clear();
    }

    /**
     * Удаление всех подзадач
     */
    @Override
    public void removeSubTasks() {
        for (Task task : subTaskList.values()) {
            removePrioritizedTasks(task);
            historyManager.remove(task.getId());
        }
        subTaskList.clear();
    }

    /**
     * Удаление всех эпиков
     */
    @Override
    public void removeEpics() {
        removeSubTasks();
        for (Task task : epicList.values()) {
            removePrioritizedTasks(task);
            historyManager.remove(task.getId());
        }
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

    private void addPrioritizedTasks(Task task) {
        if (task.getStartTime() == null) {
            return;
        }
        prioritizedTasks.add(task);
    }

    private void removePrioritizedTasks(Task task) {
        prioritizedTasks.remove(task);
    }

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public void updateEpicTime(Epic epic) {
        List<SubTask> subTasks = epic.getSubTasks();
        if (subTasks.isEmpty()) {
            return;
        }

        Optional<LocalDateTime> startTime = subTasks
            .stream()
            .map(Task::getStartTime)
            .filter(Objects::nonNull)
            .min(LocalDateTime::compareTo);
        if (startTime.isEmpty()) {
            return;
        }

        epic.setStartTime(startTime.get());

        LocalDateTime endTime = subTasks
                .stream()
                .map(Task::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElseThrow(NoSuchElementException::new);

        epic.setEndTime(endTime);

        epic.setDuration(Duration.between(startTime.get(), endTime));
    }

    public void checkTask(Task task) {
        Optional<Task> intersectTask = getPrioritizedTasks()
            .stream()
            .filter(filterTask -> {
                LocalDateTime start = filterTask.getStartTime();
                LocalDateTime end = filterTask.getEndTime();

                LocalDateTime taskStart = task.getStartTime();
                LocalDateTime taskEnd = task.getEndTime();

                return (
                    (taskStart.equals(start) || taskEnd.equals(end))
                    || taskStart.isAfter(start) && taskEnd.isBefore(end)
                    || taskStart.isBefore(start) && taskEnd.isAfter(end)
                    || taskStart.isBefore(start) && taskEnd.isAfter(start)
                );
            })
            .findFirst();

        if (intersectTask.isPresent()) {
            throw new RuntimeException("Ошибка. Время задач пересекается");
        }
    }
}
