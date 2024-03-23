package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerSaveException;
import manager.TaskManager;
import model.Epic;
import model.Task;

import java.io.IOException;
import java.lang.reflect.Type;

public class EpicsHandler extends MainTasksHandler {
    public EpicsHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected Type getClassModel() {
        return Epic.class;
    }

    @Override
    protected void getList(HttpExchange exchange) throws IOException {
        response(exchange, gson.toJson(taskManager.getEpicList()), 200);
    }

    @Override
    protected void delete(HttpExchange exchange, int id) throws IOException {
        if (taskManager.getEpicById(id) != null) {
            taskManager.removeEpicById(id);
            response(exchange, ok(), 200);
        } else {
            response(exchange, error("Задача не найдена"), 404);
        }
    }

    @Override
    protected void getById(HttpExchange exchange, int id) throws IOException {
        Task task = taskManager.getEpicById(id);
        if (task == null) {
            response(exchange, error("Задача не найдена"), 404);
        } else {
            response(exchange, gson.toJson(task), 200);
        }
    }

    @Override
    protected void create(HttpExchange exchange, Task task) throws IOException {
        try {
            taskManager.addEpic((Epic) task);
            response(exchange, ok(), 200);
        } catch (ManagerSaveException e) {
            response(exchange, error(e.getMessage()), 406);
        }
    }

    @Override
    protected void update(HttpExchange exchange, Task task) throws IOException {
        try {
            taskManager.updateEpic((Epic) task);
            response(exchange, ok(), 200);
        } catch (ManagerSaveException e) {
            response(exchange, error(e.getMessage()), 406);
        }
    }
}
