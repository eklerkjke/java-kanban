package manager;

import constans.TaskStatus;
import history.TaskHistoryManager;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import provider.Managers;

import java.util.List;

class TaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUpTestManager() {
        // Инициализируем менеджер
        TaskManager manager = Managers.getDefault();

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
        taskManager.removeAll();
        Assertions.assertEquals(0, taskManager.getTaskList().size(), "Задачи не удалены");
        Assertions.assertEquals(0, taskManager.getEpicList().size(), "Эпики не удалены");
        Assertions.assertEquals(0, taskManager.getSubTaskList().size(), "Подзадачи не удалены");
    }

    @Test
    void getHistory() {
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Задача 2", TaskStatus.IN_PROGRESS);
        Epic epic = new Epic("Эпик 1", "Описание Эпика 1");

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

    @Test
    void getHistoryWithMoreThen10Tasks() {
        Task[] arrayTasks = {
            new Task("Задача 1", "Описание 1", TaskStatus.NEW),
            new Task("Задача 2", "Описание 2", TaskStatus.NEW),
            new Task("Задача 3", "Описание 3", TaskStatus.NEW),
            new Task("Задача 4", "Описание 4", TaskStatus.NEW),
            new Task("Задача 5", "Описание 5", TaskStatus.NEW),
            new Task("Задача 6", "Описание 6", TaskStatus.NEW),
            new Task("Задача 7", "Описание 7", TaskStatus.NEW),
            new Task("Задача 8", "Описание 8", TaskStatus.NEW),
            new Task("Задача 9", "Описание 9", TaskStatus.NEW),
            new Task("Задача 10", "Описание 10", TaskStatus.NEW),
            new Task("Задача 11", "Описание 11", TaskStatus.NEW)
        };

        for (Task task : arrayTasks) {
            taskManager.addTask(task);
            taskManager.getTaskById(task.getId());
        }

        List<Task> history = taskManager.getHistory();

        Assertions.assertEquals(10, history.size(), "Не верное количество записей в истории");
    }

    @Test
    void tasksEqualsById() {
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);

        task1.setId(1);
        task2.setId(1);

        Assertions.assertEquals(task1, task2, "Задачи с одинаковым ID не равны");

        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик 1", "Описание 1");

        epic1.setId(1);
        epic2.setId(1);

        Assertions.assertEquals(epic1, epic2, "Эпики с одинаковым ID не равны");

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic1);
        SubTask subTask2 = new SubTask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic1);

        subTask1.setId(1);
        subTask2.setId(1);

        Assertions.assertEquals(subTask1, subTask2, "Подзадачи с одинаковым ID не равны");
    }
    @Test
    void epicShouldNotAddedAsSubTask() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic1);

        taskManager.addSubTask(subTask1);

        Assertions.assertNull(taskManager.getEpicById(epic1.getId()), "Нельзя добавить подзадачу без добавленного эпика");
    }

    @Test
    void checkManagersClasses() {
        TaskManager manager = Managers.getDefault();
        Assertions.assertInstanceOf(TaskManager.class, manager, "Менеджер задач имеет не корректный интерфейс");

        TaskHistoryManager history = Managers.getDefaultHistory();
        Assertions.assertInstanceOf(TaskHistoryManager.class, history, "Менеджер истории задач имеет не корректный интерфейс");
    }
}