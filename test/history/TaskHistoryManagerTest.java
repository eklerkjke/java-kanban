package history;

import constans.TaskStatus;
import manager.TaskManager;
import model.Epic;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import provider.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskHistoryManagerTest {

    private TaskHistoryManager historyManager;

    @BeforeEach
    public void createHistoryManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void add() {
        TaskManager manager = Managers.getDefault();

        Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW);
        task.setId(1);

        historyManager.add(
            manager,
            task
        );

        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История задач пустая!");
        assertEquals(1, history.size(), "Размер истории задач не увеличился после добавления!");
    }

    @Test
    void remove() {
        TaskManager manager = Managers.getDefault();

        Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW);

        historyManager.add(manager, task);
        historyManager.remove(task.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(0, history.size(), "Размер истории задач не уменьшился после удаления!");
    }

    @Test
    void getHistory() {
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Задача 2", TaskStatus.IN_PROGRESS);
        Epic epic = new Epic("Эпик 1", "Описание Эпика 1");

        TaskManager taskManager = Managers.getDefault();

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(999);
        taskManager.getEpicById(epic.getId());

        List<Task> history = taskManager.getHistory();

        Assertions.assertEquals(3, history.size(), "Не верное количество записей в истории");
    }

}