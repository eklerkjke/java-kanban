package manager;

import constans.TaskStatus;
import exceptions.ManagerSaveException;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.*;
import provider.Managers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManagerTest extends TaskManagerTest {
    protected File file;

    @Override
    protected TaskManager getDefaultTaskManager() {
        try {
            file = File.createTempFile("tasks_test", "csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return FileBackedTaskManager.loadFromFile(file);
    }


    @BeforeEach
    public void setUpTestManager() {
        taskManager = getDefaultTaskManager();
    }

    @AfterEach
    public void clean() {
        taskManager.removeAll();
    }

    @Test
    public void shouldSaveAndLoadFromFile() {
        Task task = new Task("Задание 1", "Описание 1", TaskStatus.NEW, LocalDateTime.of(2024, 3, 23, 14, 0), Duration.ofMinutes(20));
        taskManager.addTask(task);

        Epic epic = new Epic("Эпик 1", "Описание 2");
        taskManager.addEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание 3", TaskStatus.NEW, epic.getId(), LocalDateTime.of(2024, 3, 23, 13, 0), Duration.ofMinutes(20));
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

        Assertions.assertTrue(extraTaskManager.getTaskList().isEmpty(), "Список задач должен быть пустым при загрузке из пустого файла");
        Assertions.assertTrue(extraTaskManager.getEpicList().isEmpty(), "Список эпиков должен быть пустым при загрузке из пустого файла");
        Assertions.assertTrue(extraTaskManager.getSubTaskList().isEmpty(), "Список подзадач должен быть пустым при загрузке из пустого файла");
    }

    @Test
    void shouldThrowWhenLoadNotExistFile() {
        Assertions.assertThrows(ManagerSaveException.class, () -> {
            Path path = Paths.get("src/not_exist_file_test.csv");
            FileBackedTaskManager.loadFromFile(path.toFile());
        }, "Должно выбрасывать исключение при загрузке с несуществующим файлом");
    }

    @Test
    void shouldThrowWhenLoadSaveNotExistFile() {
        Assertions.assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistory());

            manager.addTask(new Task("Задание 1", "Описание 1", TaskStatus.NEW, LocalDateTime.of(2024, 2, 23, 14, 0), Duration.ofMinutes(20)));
        }, "Должно выбрасываться исключение при сохранении менеджера без файла");
    }
}
