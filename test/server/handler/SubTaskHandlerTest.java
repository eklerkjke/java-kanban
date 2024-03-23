package server.handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import constans.TaskStatus;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
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

public class SubTaskHandlerTest {

    protected static final String DEFAULT_URL = "http://localhost:8080/";

    protected HttpTaskServer taskServer;

    protected TaskManager taskManager;

    protected HttpClient client;

    public SubTaskHandlerTest() throws IOException {
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
    public void shouldGetSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("epic name", "epic descr");
        taskManager.addEpic(epic);

        SubTask subTask = new SubTask("task 1", "descr 1", TaskStatus.NEW, epic.getId());
        subTask.setStartTime(LocalDateTime.of(2024, 3, 23, 14, 5, 0, 0));
        subTask.setDuration(Duration.ofMinutes(10));
        taskManager.addSubTask(subTask);

        HttpResponse<String> response;

        URI url = URI.create(DEFAULT_URL + "subtasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .uri(url)
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        List<SubTask> tasksFromManager = taskServer
                .getGson()
                .fromJson(
                        response.body(),
                        new TypeToken<List<Epic>>() {}.getType()
                );

        Assertions.assertNotNull(tasksFromManager, "Задачи не были получены");
        Assertions.assertEquals(1, tasksFromManager.size(), "Количество задач не совпадает");
    }

    @Test
    public void shouldAddSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("epic name", "epic descr");
        taskManager.addEpic(epic);

        SubTask subTask = new SubTask("task 1", "descr 1", TaskStatus.NEW, epic.getId());
        subTask.setStartTime(LocalDateTime.of(2024, 3, 23, 14, 5, 0, 0));
        subTask.setDuration(Duration.ofMinutes(10));

        URI url = URI.create(DEFAULT_URL + "subtasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskServer.getGson().toJson(subTask)))
                .uri(url)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals("ok", responseObject.get("status").getAsString(), "Не верный ответ сервера");
    }

    @Test
    public void shouldUpdateTask() throws IOException, InterruptedException {
        Epic epic = new Epic("epic name", "epic descr");
        taskManager.addEpic(epic);

        SubTask subTask = new SubTask("task 1", "descr 1", TaskStatus.NEW, epic.getId());
        subTask.setStartTime(LocalDateTime.of(2024, 3, 23, 14, 5, 0, 0));
        subTask.setDuration(Duration.ofMinutes(10));
        taskManager.addSubTask(subTask);

        subTask.setStatus(TaskStatus.DONE);
        
        URI url = URI.create(DEFAULT_URL + "subtasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskServer.getGson().toJson(subTask)))
                .uri(url)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals("ok", responseObject.get("status").getAsString(), "Не верный ответ сервера");

        Assertions.assertEquals(TaskStatus.DONE, subTask.getStatus(), "Статус задачи не изменился");
    }

    @Test
    public void shouldDeleteTask() throws IOException, InterruptedException {
        Epic epic = new Epic("epic name", "epic descr");
        taskManager.addEpic(epic);

        SubTask subTask = new SubTask("task 1", "descr 1", TaskStatus.NEW, epic.getId());
        subTask.setStartTime(LocalDateTime.of(2024, 3, 23, 14, 5, 0, 0));
        subTask.setDuration(Duration.ofMinutes(10));
        taskManager.addSubTask(subTask);

        URI url = URI.create(DEFAULT_URL + "subtasks?id=" + subTask.getId());
        HttpRequest request = HttpRequest
                .newBuilder()
                .DELETE()
                .uri(url)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals("ok", responseObject.get("status").getAsString(), "Не верный ответ сервера");

        Assertions.assertEquals(0, taskManager.getSubTaskList().size(), "Задача не удалилась");
    }
}
