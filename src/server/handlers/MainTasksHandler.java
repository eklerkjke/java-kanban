package server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Optional;

public abstract class MainTasksHandler extends BaseHandler {

    MainTasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (!resolve(exchange)) {
                response(exchange, error("Запрашиваемый адрес не найден"), 404);
            }
        } catch (Exception e) {
            response(exchange, error("Ошибка на сервере"), 500);
        }
    }

    protected boolean resolve(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] arPath = path.split("/");
        String method = exchange.getRequestMethod();

        if (arPath.length == 2) {
            if (method.equals("GET")) {
                getList(exchange);
                return true;
            } else if (method.equals("POST")) {
                Optional<Task> taskOptional = fromBody(exchange);
                if (taskOptional.isEmpty()) {
                    response(exchange, error("Не корректный запрос"), 400);
                    return true;
                }

                if (taskOptional.get().getId() <= 0) {
                    create(exchange, taskOptional.get());
                    return true;
                } else {
                    update(exchange, taskOptional.get());
                    return true;
                }
            } else if (method.equals("DELETE")) {
                int id = getIdFromQuery(exchange.getRequestURI().getQuery());
                if (id > 0) {
                    delete(exchange, id);
                    return true;
                }

                return false;
            }

            return false;
        } else if (arPath.length == 3 && method.equals("GET")) {
            int id = Integer.parseInt(arPath[2]);
            getById(exchange, id);
            return true;
        }

        return false;
    }

    protected static int getIdFromQuery(String query) {
        String[] queryParams = query.split("&");

        for (String param : queryParams) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2 && keyValue[0].equals("id")) {
                try {
                    return Integer.parseInt(keyValue[1]);
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return -1;
    }

    protected Optional<Task> fromBody(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        try {
            String body = new String(inputStream.readAllBytes(), CHARSET);
            return Optional.of(gson.fromJson(body, getClassModel()));
        } catch (JsonSyntaxException e) {
            return Optional.empty();
        }
    }

    protected abstract Type getClassModel();

    protected abstract void getList(HttpExchange exchange) throws IOException;

    protected abstract void delete(HttpExchange exchange, int id) throws IOException;

    protected abstract void getById(HttpExchange exchange, int id) throws IOException;

    protected abstract void create(HttpExchange exchange, Task task) throws IOException;

    protected abstract void update(HttpExchange exchange, Task task) throws IOException;
}
