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

/**
 * Класс для тестирования менеджера задач
 */
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
    void tasksEqualsById() {
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.IN_PROGRESS);

        task1.setId(1);
        task2.setId(1);

        Assertions.assertEquals(task1, task2, "Задачи с одинаковым ID не равны");

        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик 2", "Описание 2");

        epic1.setId(1);
        epic2.setId(1);

        Assertions.assertEquals(epic1, epic2, "Эпики с одинаковым ID не равны");

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic1);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", TaskStatus.DONE, epic1);

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

    @Test
    void subTaskShouldUpdateId() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic1);

        epic1.addSubTask(subTask1);

        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);

        int subTaskId = subTask1.getId();

        taskManager.removeSubTaskById(subTaskId);

        epic1.addSubTask(subTask1);
        taskManager.addSubTask(subTask1);

        Assertions.assertNotEquals(subTaskId, subTask1.getId(), "ID подзадачи не изменился!");
    }

    @Test
    void shouldNotUpdateFieldsByDirectSetters() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic1);

        epic1.addSubTask(subTask1);

        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);

        epic1.setStatus(TaskStatus.IN_PROGRESS);

        Assertions.assertEquals(
            epic1.getStatus(),
            TaskStatus.NEW,
            "Статус эпика изменился после вызова сеттера напрямую!"
        );
    }
}