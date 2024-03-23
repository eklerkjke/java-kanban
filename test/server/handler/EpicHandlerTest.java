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

public class EpicHandlerTest {

    protected static final String DEFAULT_URL = "http://localhost:8080/";

    protected HttpTaskServer taskServer;

    protected TaskManager taskManager;

    protected HttpClient client;

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
    public void shouldGetEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epic name", "epic descr");
        taskManager.addEpic(epic);

        HttpResponse<String> response;

        URI url = URI.create(DEFAULT_URL + "epics");
        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .uri(url)
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        List<Epic> tasksFromManager = taskServer
                .getGson()
                .fromJson(
                        response.body(),
                        new TypeToken<List<Epic>>() {}.getType()
                );

        Assertions.assertNotNull(tasksFromManager, "Задачи не были получены");
        Assertions.assertEquals(1, tasksFromManager.size(), "Количество задач не совпадает");
    }

    @Test
    public void shouldAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epic name", "epic descr");

        URI url = URI.create(DEFAULT_URL + "epics");
        HttpRequest request = HttpRequest
                .newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskServer.getGson().toJson(epic)))
                .uri(url)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals("ok", responseObject.get("status").getAsString(), "Не верный ответ сервера");
    }

    @Test
    public void shouldUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epic name", "epic descr");
        taskManager.addEpic(epic);

        epic.setName("epic test");
        
        URI url = URI.create(DEFAULT_URL + "epics");
        HttpRequest request = HttpRequest
                .newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskServer.getGson().toJson(epic)))
                .uri(url)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals("ok", responseObject.get("status").getAsString(), "Не верный ответ сервера");

        Assertions.assertEquals("epic test", epic.getName(), "Статус задачи не изменился");
    }

    @Test
    public void shouldDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epic name", "epic descr");
        taskManager.addEpic(epic);

        URI url = URI.create(DEFAULT_URL + "epics?id=" + epic.getId());
        HttpRequest request = HttpRequest
                .newBuilder()
                .DELETE()
                .uri(url)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals("ok", responseObject.get("status").getAsString(), "Не верный ответ сервера");

        Assertions.assertEquals(0, taskManager.getEpicList().size(), "Задача не удалилась");
    }
}
