package history;

import manager.TaskManager;
import model.Task;
import provider.Managers;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskHistoryManager implements TaskHistoryManager {
    /**
     * Константа, обозначает максимальное количество задач в списке истории
     */
    private final int MAX_VALUE = 10;

    private List<Task> historyList = new ArrayList<>();

    @Override
    public void add(TaskManager manager, Task task) {
        if (task.getId() <= 0) {
            return;
        }

        if (historyList.size() >= MAX_VALUE) {
            // for JDK 21
            // historyList.removeFirst();

            // for JDK 11
            historyList.remove(0);
        }

        historyList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(historyList);
    }
}
