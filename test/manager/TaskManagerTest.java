package manager;

import constans.TaskStatus;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import provider.ProviderManager;

import java.util.ArrayList;

class TaskManagerTest {

    public static TaskManager<Task, SubTask, Epic> taskManager;

    @BeforeAll
    static void setUpTestManager() {
        // Инициализируем менеджер
        TaskManager<Task, SubTask, Epic> manager = ProviderManager.getTaskManager();

        Epic firstEpic = new Epic("Эпик 1", "Описание Эпика 1");
        Epic secondEpic = new Epic("Эпик 2", "Описание Эпика 2");

        SubTask thirtSubTask = new SubTask("Подзадача 2", "Описание 2", TaskStatus.IN_PROGRESS, secondEpic);

        manager.addTask(new Task("Задача 1", "Описание 1", TaskStatus.NEW));
        manager.addTask(new Task("Задача 2", "Задача 2", TaskStatus.IN_PROGRESS));

        manager.addEpic(firstEpic);
        manager.addSubTask(new SubTask("Подзадача 1", "Описание 1", TaskStatus.IN_PROGRESS, firstEpic));
        manager.addSubTask(new SubTask("Подзадача 2", "Описание 2", TaskStatus.NEW, firstEpic));

        manager.addEpic(secondEpic);
        manager.addSubTask(thirtSubTask);

        taskManager = manager;
    }

    @Test
    void addNewTask() {
        Task task = new Task("Задача Тест", "Описание Тестовой задачи", TaskStatus.NEW);
        taskManager.addTask(task);
        Assertions.assertTrue(task.getId() > 0, "ID новой задачи меньше нуля!");
        Assertions.assertEquals(task, taskManager.getTaskById(task.getId()), "Новая задача не добавилась!");
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Эпик Тест", "Описание Тестового эпика");
        taskManager.addEpic(epic);
        Assertions.assertTrue(epic.getId() > 0, "ID нового эпика меньше нуля!");
        Assertions.assertEquals(epic, taskManager.getEpicById(epic.getId()), "Новый эпик не добавился!");
    }

    @Test
    void addNewEpicAndSubTask() {
        Epic epic = new Epic("Эпик Тест 2", "Описание Тестового эпика 2");
        taskManager.addEpic(epic);
        Assertions.assertTrue(epic.getId() > 0, "ID нового эпика меньше нуля!");
        Assertions.assertEquals(epic, taskManager.getEpicById(epic.getId()), "Новый эпик не добавился!");

        SubTask subTask = new SubTask("Подзадача тест", "Описание тестовой подзадачи", TaskStatus.NEW, epic);
        taskManager.addSubTask(subTask);
        Assertions.assertTrue(subTask.getId() > 0, "ID новой подзадачи меньше нуля!");
        Assertions.assertEquals(subTask, taskManager.getSubTaskById(subTask.getId()), "Новая подзадача не добавлена!");
    }

    @Test
    void updateNewTask() {
        Task task = new Task("Задача Тест", "Описание Тестовой задачи", TaskStatus.NEW);
        taskManager.addTask(task);

        task.setName("Задача Тест Новое название");
        task.setDescription("Описание Тестовой задачи Новое");
        task.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task);

        Assertions.assertEquals(task.getName(), "Задача Тест Новое название", "Названия не совпадают");
        Assertions.assertEquals(task.getDescription(), "Описание Тестовой задачи Новое", "Описания не совпадают");
        Assertions.assertEquals(task.getStatus(), TaskStatus.IN_PROGRESS, "Статусы не совпадают");
    }

    @Test
    void createTaskAndGetTaskById() {
        Task task = new Task("Задача Тест", "Описание Тестовой задачи", TaskStatus.NEW);
        taskManager.addTask(task);
        int taskId = task.getId();

        Task findTask = taskManager.getTaskById(taskId);

        Assertions.assertTrue(taskId > 0, "ID новой задачи меньше нуля!");
        Assertions.assertEquals(task, findTask, "Новая задача не добавилась!");
    }

    @Test
    void removeTaskById() {
        Task task = new Task("Задача Тест", "Описание Тестовой задачи", TaskStatus.NEW);
        taskManager.addTask(task);
        int taskId = task.getId();

        Task findTask = taskManager.getTaskById(taskId);

        Assertions.assertTrue(taskId > 0, "ID новой задачи меньше нуля!");
        Assertions.assertEquals(task, findTask, "Новая задача не добавилась!");

        taskManager.removeTaskById(taskId);
        Assertions.assertNull(taskManager.getTaskById(taskId), "Задача не удалилась");
    }

    @Test
    void removeAll() {
        setUpTestManager();

        taskManager.removeAll();
        Assertions.assertEquals(0, taskManager.getTaskList().size(), "Задачи не удалены");
        Assertions.assertEquals(0, taskManager.getEpicList().size(), "Эпики не удалены");
        Assertions.assertEquals(0, taskManager.getSubTaskList().size(), "Подзадачи не удалены");
    }

    @Test
    void getHistory() {
        setUpTestManager();

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.getEpicById(3);

        ArrayList<Task> history = taskManager.getHistory();

        System.out.println(history);

        Assertions.assertEquals(3, history.size(), "Не верное количество записей в истории");
    }
}