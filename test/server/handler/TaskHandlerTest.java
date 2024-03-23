package server.handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import constans.TaskStatus;
import manager.TaskManager;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import provider.Managers;
import server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TaskHandlerTest {

    protected static final String DEFAULT_URL = "http://localhost:8080/";

    protected HttpTaskServer taskServer;

    protected TaskManager taskManager;

    protected HttpClient client;

    public TaskHandlerTest() throws IOException {
        taskManager = Managers.getDefault();
        taskServer = new HttpTaskServer(taskManager);
        taskServer.setUp();
    }

    @BeforeEach
    public void start() {
        client = HttpClient.newHttpClient();

        taskManager.removeAll();
        taskServer.start();
    }

    @AfterEach
    public void stop() {
        taskServer.stop();
    }

    @Test
    public void shouldGetTasks() throws IOException, InterruptedException {
        Task task1 = new Task("task 1", "descr 1", TaskStatus.NEW);
        task1.setStartTime(LocalDateTime.of(2024, 3, 23, 14, 5, 0, 0));
        task1.setDuration(Duration.ofMinutes(10));

        Task task2 = new Task("task 2", "descr 2", TaskStatus.IN_PROGRESS);
        task2.setStartTime(LocalDateTime.of(2024, 3, 24, 14, 0, 0, 0));
        task2.setDuration(Duration.ofMinutes(15));

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        HttpResponse<String> response;

        URI url = URI.create(DEFAULT_URL + "tasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .uri(url)
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        List<Task> tasksFromManager = taskServer
                .getGson()
                .fromJson(
                        response.body(),
                        new TypeToken<List<Task>>() {}.getType()
                );

        Assertions.assertNotNull(tasksFromManager, "Задачи не были получены");
        Assertions.assertEquals(2, tasksFromManager.size(), "Количество задач не совпадает");
    }

    @Test
    public void shouldAddTasks() throws IOException, InterruptedException {
        Task task1 = new Task("task 1", "descr 1", TaskStatus.NEW);
        task1.setStartTime(LocalDateTime.of(2024, 3, 23, 14, 5, 0, 0));
        task1.setDuration(Duration.ofMinutes(10));

        taskManager.addTask(task1);

        URI url = URI.create(DEFAULT_URL + "tasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskServer.getGson().toJson(task1)))
                .uri(url)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals("ok", responseObject.get("status").getAsString(), "Не верный ответ сервера");
    }

    @Test
    public void shouldUpdateTask() throws IOException, InterruptedException {
        Task task1 = new Task("task 1", "descr 1", TaskStatus.NEW);
        task1.setStartTime(LocalDateTime.of(2024, 3, 23, 14, 5, 0, 0));
        task1.setDuration(Duration.ofMinutes(10));

        taskManager.addTask(task1);

        task1.setStatus(TaskStatus.DONE);

        URI url = URI.create(DEFAULT_URL + "tasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskServer.getGson().toJson(task1)))
                .uri(url)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals("ok", responseObject.get("status").getAsString(), "Не верный ответ сервера");

        Assertions.assertEquals(TaskStatus.DONE, task1.getStatus(), "Статус задачи не изменился");
    }

    @Test
    public void shouldDeleteTask() throws IOException, InterruptedException {
        Task task1 = new Task("task 1", "descr 1", TaskStatus.NEW);
        task1.setStartTime(LocalDateTime.of(2024, 3, 23, 14, 5, 0, 0));
        task1.setDuration(Duration.ofMinutes(10));

        taskManager.addTask(task1);

        URI url = URI.create(DEFAULT_URL + "tasks?id=" + task1.getId());
        HttpRequest request = HttpRequest
                .newBuilder()
                .DELETE()
                .uri(url)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals("ok", responseObject.get("status").getAsString(), "Не верный ответ сервера");

        Assertions.assertEquals(0, taskManager.getTaskList().size(), "Задача не удалилась");
    }
}
