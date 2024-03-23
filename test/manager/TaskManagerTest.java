package manager;

import constans.TaskStatus;
import exceptions.ManagerSaveException;
import history.TaskHistoryManager;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import provider.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

/**
 * Класс для тестирования менеджера задач
 */
abstract class TaskManagerTest {

    protected TaskManager taskManager;

    abstract protected TaskManager getDefaultTaskManager();

    @BeforeEach
    void setUpTestManager() {
        // Инициализируем менеджер
        TaskManager manager = getDefaultTaskManager();

        Epic firstEpic = new Epic("Эпик 1", "Описание Эпика 1");
        manager.addEpic(firstEpic);

        Epic secondEpic = new Epic("Эпик 2", "Описание Эпика 2");
        manager.addEpic(secondEpic);

        SubTask thirtSubTask = new SubTask("Подзадача 2", "Описание 2", TaskStatus.IN_PROGRESS, secondEpic.getId(), LocalDateTime.of(2024, 3, 23, 13, 0), Duration.ofMinutes(20));
        manager.addSubTask(thirtSubTask);

        manager.addTask(new Task("Задача 1", "Описание 1", TaskStatus.NEW, LocalDateTime.of(2024, 3, 15, 12, 0), Duration.ofMinutes(20)));
        manager.addTask(new Task("Задача 2", "Задача 2", TaskStatus.IN_PROGRESS, LocalDateTime.of(2024, 3, 23, 11, 0), Duration.ofMinutes(20)));

        manager.addSubTask(new SubTask("Подзадача 1", "Описание 1", TaskStatus.IN_PROGRESS, firstEpic.getId(), LocalDateTime.of(2024, 3, 23, 10, 0), Duration.ofMinutes(20)));
        manager.addSubTask(new SubTask("Подзадача 2", "Описание 2", TaskStatus.NEW, firstEpic.getId(), LocalDateTime.of(2024, 3, 23, 9, 0), Duration.ofMinutes(20)));

        taskManager = manager;
    }

    @Test
    void addNewTask() {
        Task task = new Task("Задача Тест", "Описание Тестовой задачи", TaskStatus.NEW, LocalDateTime.of(2024, 3, 23, 8, 0), Duration.ofMinutes(20));
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

        SubTask subTask = new SubTask("Подзадача тест", "Описание тестовой подзадачи", TaskStatus.NEW, epic.getId(), LocalDateTime.of(2024, 3, 23, 7, 0), Duration.ofMinutes(20));
        taskManager.addSubTask(subTask);
        Assertions.assertTrue(subTask.getId() > 0, "ID новой подзадачи меньше нуля!");
        Assertions.assertEquals(subTask, taskManager.getSubTaskById(subTask.getId()), "Новая подзадача не добавлена!");
    }

    @Test
    void shouldThrowWhenGetSubtaskNotExistEpic() {
        Assertions.assertThrows(Exception.class, () -> {
            taskManager.getSubTasksEpic(99999);
        });
    }

    @Test
    void updateNewTask() {
        Task task = new Task("Задача Тест", "Описание Тестовой задачи", TaskStatus.NEW, LocalDateTime.of(2024, 3, 23, 2, 0), Duration.ofMinutes(20));
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
        Task task = new Task("Задача Тест", "Описание Тестовой задачи", TaskStatus.NEW, LocalDateTime.of(2024, 3, 1, 14, 0), Duration.ofMinutes(20));
        taskManager.addTask(task);
        int taskId = task.getId();

        Task findTask = taskManager.getTaskById(taskId);

        Assertions.assertTrue(taskId > 0, "ID новой задачи меньше нуля!");
        Assertions.assertEquals(task, findTask, "Новая задача не добавилась!");
    }

    @Test
    void removeTaskById() {
        Task task = new Task("Задача Тест", "Описание Тестовой задачи", TaskStatus.NEW, LocalDateTime.of(2024, 3, 2, 14, 0), Duration.ofMinutes(20));
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
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW, LocalDateTime.of(2024, 3, 3, 14, 0), Duration.ofMinutes(20));
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.IN_PROGRESS, LocalDateTime.of(2024, 3, 4, 14, 0), Duration.ofMinutes(20));

        task1.setId(1);
        task2.setId(1);

        Assertions.assertEquals(task1, task2, "Задачи с одинаковым ID не равны");

        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик 2", "Описание 2");

        epic1.setId(1);
        epic2.setId(1);

        Assertions.assertEquals(epic1, epic2, "Эпики с одинаковым ID не равны");

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic1.getId(), LocalDateTime.of(2024, 3, 23, 5, 0), Duration.ofMinutes(20));
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", TaskStatus.DONE, epic1.getId(), LocalDateTime.of(2024, 3, 23, 6, 0), Duration.ofMinutes(20));

        subTask1.setId(1);
        subTask2.setId(1);

        Assertions.assertEquals(subTask1, subTask2, "Подзадачи с одинаковым ID не равны");
    }
    @Test
    void epicShouldNotAddedAsSubTask() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic1.getId(), LocalDateTime.of(2024, 3, 7, 14, 0), Duration.ofMinutes(20));

        Assertions.assertThrows(NullPointerException.class, () -> taskManager.addSubTask(subTask1));

        Assertions.assertNull(taskManager.getEpicById(epic1.getId()), "Нельзя добавить подзадачу без добавленного эпика");
    }

    @Test
    void checkManagersClasses() throws ManagerSaveException {
        Assertions.assertInstanceOf(TaskManager.class, taskManager, "Менеджер задач имеет не корректный интерфейс");

        TaskHistoryManager history = Managers.getDefaultHistory();
        Assertions.assertInstanceOf(TaskHistoryManager.class, history, "Менеджер истории задач имеет не корректный интерфейс");
    }

    @Test
    void subTaskShouldUpdateId() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic1.getId(), LocalDateTime.of(2024, 3, 23, 8, 0), Duration.ofMinutes(20));

        epic1.addSubTask(subTask1);

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
        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic1.getId(), LocalDateTime.of(2024, 3, 9, 14, 0), Duration.ofMinutes(20));
        epic1.addSubTask(subTask1);

        taskManager.addSubTask(subTask1);

        epic1.setStatus(TaskStatus.IN_PROGRESS);

        Assertions.assertEquals(
            epic1.getStatus(),
            TaskStatus.NEW,
            "Статус эпика изменился после вызова сеттера напрямую!"
        );
    }

    @Test
    void shouldAddTwoTaskWithDiffTime() {
        Task task1 = new Task("Задача Тест", "Описание Тестовой задачи", TaskStatus.NEW, LocalDateTime.of(2024, 3, 10, 14, 0), Duration.ofMinutes(20));
        task1.setStartTime(LocalDateTime.of(2024, Month.MARCH, 16, 15, 20));
        task1.setDuration(Duration.ofMinutes(40));

        Task task2 = new Task("Задача Тест", "Описание Тестовой задачи", TaskStatus.NEW, LocalDateTime.of(2024, 3, 11, 14, 0), Duration.ofMinutes(20));
        task2.setStartTime(LocalDateTime.of(2024, Month.MARCH, 16, 16, 0));
        task2.setDuration(Duration.ofMinutes(20));

        taskManager.addTask(task1);

        Assertions.assertDoesNotThrow(() -> {
            taskManager.addTask(task2);
        }, "Вторая задача должна добавляться без пересечения времени");
    }

    @Test
    void shouldThrowExceptionAddTasksWithIntersectTimeStart() {
        Task task1 = new Task("Задача Тест", "Описание Тестовой задачи", TaskStatus.NEW, LocalDateTime.of(2024, 3, 12, 14, 0), Duration.ofMinutes(20));
        task1.setStartTime(LocalDateTime.of(2024, Month.MARCH, 16, 15, 20));
        task1.setDuration(Duration.ofMinutes(40));

        Task task2 = new Task("Задача Тест", "Описание Тестовой задачи", TaskStatus.NEW, LocalDateTime.of(2024, 3, 13, 14, 0), Duration.ofMinutes(20));
        task2.setStartTime(LocalDateTime.of(2024, Month.MARCH, 16, 15, 20));
        task2.setDuration(Duration.ofMinutes(20));

        taskManager.addTask(task1);

        Assertions.assertThrows(RuntimeException.class, () -> {
            taskManager.addTask(task2);
        }, "Вторая задача не пересекается по времени с первой");
    }


    @Test
    void shouldThrowExceptionUpdateTasksWithIntersectTimeStart() {
        Task task1 = new Task("Задача Тест", "Описание Тестовой задачи", TaskStatus.NEW, LocalDateTime.of(2024, 3, 14, 14, 0), Duration.ofMinutes(20));
        task1.setStartTime(LocalDateTime.of(2024, Month.MARCH, 16, 15, 20));
        task1.setDuration(Duration.ofMinutes(40));

        Task task2 = new Task("Задача Тест", "Описание Тестовой задачи", TaskStatus.NEW, LocalDateTime.of(2024, 3, 15, 14, 0), Duration.ofMinutes(20));
        task2.setStartTime(LocalDateTime.of(2024, Month.MARCH, 16, 16, 0));
        task2.setDuration(Duration.ofMinutes(20));

        taskManager.addTask(task1);

        Assertions.assertDoesNotThrow(() -> {
            taskManager.addTask(task2);
        }, "Вторая задача пересекается по времени с первой");

        Assertions.assertThrows(RuntimeException.class, () -> {
            task2.setStartTime(LocalDateTime.of(2024, Month.MARCH, 16, 15, 30));
            taskManager.updateTask(task2);
        }, "Вторая задача не пересекается по времени с первой при обновлении");
    }

    @Test
    void shouldStatusEpicNewWhenSubtasksNew() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic1.getId(), LocalDateTime.of(2024, 3, 16, 14, 0), Duration.ofMinutes(20));
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", TaskStatus.NEW, epic1.getId(), LocalDateTime.of(2024, 3, 17, 14, 0), Duration.ofMinutes(20));
        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        Assertions.assertEquals(epic1.getStatus(), TaskStatus.NEW, "Статус эпика должен быть NEW");
    }

    @Test
    void shouldStatusEpicDoneWhenSubtasksDone() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", TaskStatus.DONE, epic1.getId(), LocalDateTime.of(2024, 3, 18, 14, 0), Duration.ofMinutes(20));
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", TaskStatus.DONE, epic1.getId(), LocalDateTime.of(2024, 4, 23, 14, 0), Duration.ofMinutes(20));
        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        Assertions.assertEquals(epic1.getStatus(), TaskStatus.DONE, "Статус эпика должен быть DONE");
    }

    @Test
    void shouldStatusEpicInProgressWhenSubtasksInProgress() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", TaskStatus.IN_PROGRESS, epic1.getId(), LocalDateTime.of(2024, 5, 23, 14, 0), Duration.ofMinutes(20));
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", TaskStatus.IN_PROGRESS, epic1.getId(), LocalDateTime.of(2024, 6, 23, 14, 0), Duration.ofMinutes(20));
        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        Assertions.assertEquals(epic1.getStatus(), TaskStatus.IN_PROGRESS, "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    void shouldStatusEpicNewWhenSubtasksNewAndDone() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic1.getId(), LocalDateTime.of(2024, 7, 23, 14, 0), Duration.ofMinutes(20));
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", TaskStatus.DONE, epic1.getId(), LocalDateTime.of(2024, 8, 23, 14, 0), Duration.ofMinutes(20));
        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        Assertions.assertEquals(epic1.getStatus(), TaskStatus.NEW, "Статус эпика должен быть NEW");
    }
}