package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerSaveException;
import manager.TaskManager;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.lang.reflect.Type;

public class SubTasksHandler extends MainTasksHandler {
    public SubTasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected Type getClassModel() {
        return SubTask.class;
    }

    @Override
    protected void getList(HttpExchange exchange) throws IOException {
        response(exchange, gson.toJson(taskManager.getSubTaskList()), 200);
    }

    @Override
    protected void delete(HttpExchange exchange, int id) throws IOException {
        if (taskManager.getSubTaskById(id) != null) {
            taskManager.removeSubTaskById(id);
            response(exchange, ok(), 200);
        } else {
            response(exchange, error("Задача не найдена"), 404);
        }
    }

    @Override
    protected void getById(HttpExchange exchange, int id) throws IOException {
        SubTask task = taskManager.getSubTaskById(id);
        if (task == null) {
            response(exchange, error("Задача не найдена"), 404);
        } else {
            response(exchange, gson.toJson(task), 200);
        }
    }

    @Override
    protected void create(HttpExchange exchange, Task task) throws IOException {
        try {
            taskManager.addSubTask((SubTask) task);
            response(exchange, ok(), 200);
        } catch (ManagerSaveException e) {
            response(exchange, error(e.getMessage()), 406);
        }
    }

    @Override
    protected void update(HttpExchange exchange, Task task) throws IOException {
        try {
            taskManager.updateSubTask((SubTask) task);
            response(exchange, ok(), 200);
        } catch (ManagerSaveException e) {
            response(exchange, error(e.getMessage()), 406);
        }
    }
}
