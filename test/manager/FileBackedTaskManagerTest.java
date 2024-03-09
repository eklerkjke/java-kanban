package manager;

import constans.TaskStatus;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class FileBackedTaskManagerTest extends TaskManagerTest {
    protected File file;

    @BeforeEach
    public void setUpTestFileManager() throws IOException {
        file = File.createTempFile("tasks_test", "csv");
        taskManager = FileBackedTaskManager.loadFromFile(file);
    }

    @Test
    public void shouldSaveAndLoadFromFile() {
        Task task = new Task("Задание 1", "Описание 1", TaskStatus.NEW);
        taskManager.addTask(task);

        Epic epic = new Epic("Эпик 1", "Описание 2");
        taskManager.addEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание 3", TaskStatus.NEW, epic.getId());
        taskManager.addSubTask(subTask);

        TaskManager extraTaskManager = FileBackedTaskManager.loadFromFile(file);

        Assertions.assertEquals(task, extraTaskManager.getTaskById(task.getId()), "Ошибка загрузки задачи из файла");
        Assertions.assertEquals(epic, extraTaskManager.getEpicById(epic.getId()), "Ошибка загрузки эпика из файла");
        Assertions.assertEquals(subTask, extraTaskManager.getSubTaskById(subTask.getId()), "Ошибка загрузки подзадачи из файла");
    }

    @Test
    public void shouldNotLoadFromEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("empty_tasks", "csv");
        TaskManager extraTaskManager = FileBackedTaskManager.loadFromFile(emptyFile);

        Assertions.assertEquals(0, extraTaskManager.getTaskList().size(), "Список задач должен быть пустым при загрузке из пустого файла");
        Assertions.assertEquals(0, extraTaskManager.getEpicList().size(), "Список эпиков должен быть пустым при загрузке из пустого файла");
        Assertions.assertEquals(0, extraTaskManager.getSubTaskList().size(), "Список подзадач должен быть пустым при загрузке из пустого файла");
    }
}
