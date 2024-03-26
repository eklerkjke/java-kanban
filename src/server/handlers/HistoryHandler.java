package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHandler {
    public HistoryHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            response(exchange, gson.toJson(taskManager.getHistory()), 200);
        } catch (Exception e) {
            response(exchange, error("Ошибка на сервере"), 500);
        }
    }
}
