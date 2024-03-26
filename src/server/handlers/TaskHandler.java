package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerSaveException;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.lang.reflect.Type;

public class TaskHandler extends MainTasksHandler {
    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected Type getClassModel() {
        return Task.class;
    }

    @Override
    protected void getList(HttpExchange exchange) throws IOException {
        response(exchange, gson.toJson(taskManager.getTaskList()), 200);
    }

    @Override
    protected void delete(HttpExchange exchange, int id) throws IOException {
        if (taskManager.getTaskById(id) != null) {
            taskManager.removeTaskById(id);
            response(exchange, ok(), 200);
        } else {
            response(exchange, error("Задача не найдена"), 404);
        }
    }

    @Override
    protected void getById(HttpExchange exchange, int id) throws IOException {
        Task task = taskManager.getTaskById(id);
        if (task == null) {
            response(exchange, error("Задача не найдена"), 404);
        } else {
            response(exchange, gson.toJson(task), 200);
        }
    }

    @Override
    protected void create(HttpExchange exchange, Task task) throws IOException {
        try {
            taskManager.addTask(task);
            response(exchange, ok(), 200);
        } catch (ManagerSaveException e) {
            response(exchange, error(e.getMessage()), 406);
        }
    }

    @Override
    protected void update(HttpExchange exchange, Task task) throws IOException {
        try {
            taskManager.updateTask(task);
            response(exchange, ok(), 200);
        } catch (ManagerSaveException e) {
            response(exchange, error(e.getMessage()), 406);
        }
    }
}
