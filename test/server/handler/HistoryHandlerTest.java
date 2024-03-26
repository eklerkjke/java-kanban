package server.handler;

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

public class HistoryHandlerTest {

    private static final String DEFAULT_URL = "http://localhost:8080/";

    private HttpTaskServer taskServer;

    private TaskManager taskManager;

    private HttpClient client;

    @BeforeEach
    public void start() throws IOException {
        taskManager = Managers.getDefault();
        taskServer = new HttpTaskServer(taskManager);
        taskServer.setUp();
        client = HttpClient.newHttpClient();

        taskManager.removeAll();
        taskServer.start();
    }

    @AfterEach
    public void stop() {
        taskServer.stop();
    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {
        Task task1 = new Task("task 1", "descr 1", TaskStatus.NEW, LocalDateTime.of(2024, 3, 23, 14, 0), Duration.ofMinutes(20));
        Task task2 = new Task("task 2", "descr 2", TaskStatus.IN_PROGRESS, LocalDateTime.of(2024, 3, 20, 14, 0), Duration.ofMinutes(20));

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());

        HttpResponse<String> response;

        URI url = URI.create(DEFAULT_URL + "history");
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
        Assertions.assertEquals(taskManager.getHistory(), tasksFromManager, "История не совпадает");
    }
}
