package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class BaseHandler implements HttpHandler {


    protected static final Charset CHARSET = StandardCharsets.UTF_8;

    protected final TaskManager taskManager;

    protected final Gson gson;

    BaseHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }


    protected void response(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(CHARSET));
        }
        exchange.close();
    }

    protected String error(String errorMessage) {
        return gson.toJson(Map.of("status", "fail", "error", errorMessage));
    }

    protected String ok() {
        return gson.toJson(Map.of("status", "ok", "error", ""));
    }
}
