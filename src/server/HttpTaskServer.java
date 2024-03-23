package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import provider.Managers;
import server.adapter.DurationAdapter;
import server.adapter.LocalDateTimeAdapter;
import server.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class HttpTaskServer {
    private static final int HTTP_PORT = 8080;

    private final TaskManager taskManager;

    private final HttpServer server;

    private final Gson gson;

    public static void main(String[] args) {
        try {
            HttpTaskServer self = new HttpTaskServer(Managers.getDefault());
            self.setUp();
            self.start();
        } catch (IOException e) {
            System.out.println();
        }
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        server = createServer();
        gson = createGson();
    }

    public void listen(Map<String, BaseHandler> context) {
        context.forEach(this::addContext);
    }

    public void addContext(String path, BaseHandler handler) {
        server.createContext(path, handler);
    }

    public void setUp() {
        listen(Map.of(
                "/tasks", new TaskHandler(taskManager, gson),
                "/subtasks", new SubTasksHandler(taskManager, gson),
                "/epics", new EpicsHandler(taskManager, gson),
                "/history", new HistoryHandler(taskManager, gson),
                "/prioritized", new PrioritizedHandler(taskManager, gson)
        ));
    }

    private HttpServer createServer() throws IOException {
        return HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);
    }

    private Gson createGson()  {
        return new Gson()
                .newBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    public Gson getGson() {
        return gson;
    }

}
