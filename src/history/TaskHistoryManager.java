package history;

import model.Task;

import java.util.ArrayList;

public interface TaskHistoryManager<T extends Task> {
    void add(T task);

    ArrayList<T> getHistory();
}
