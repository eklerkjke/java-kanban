package manager;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager <T extends Task, S extends SubTask, E extends Epic> {
    ArrayList<T> getTaskList();

    ArrayList<S> getSubTaskList();

    ArrayList<E> getEpicList();

    void addTask(T task);

    void addEpic(E epic);

    void addSubTask(S subTask);

    void updateTask(T task);

    void updateSubTask(S subTask);

    void updateEpic(E epic);

    T getTaskById(int id);

    S getSubTaskById(int id);

    E getEpicById(int id);

    ArrayList<S> getSubTasksEpic(int epicId) throws Exception;

    void removeTaskById(Integer id);

    void removeSubTaskById(Integer id);

    void removeEpicById(Integer id);

    void removeAll();

    void removeTasks();

    void removeSubTasks();

    void removeEpics();

    ArrayList<T> getHistory();
}
