package history;

import model.BaseTask;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskHistoryManager implements TaskHistoryManager {

    private ArrayList<BaseTask> historyList = new ArrayList<>();


    @Override
    public void add(BaseTask task) {
        if (task.getId() <= 0) {
            return;
        }

        if (historyList.size() >= MAX_VALUE) {
            historyList.removeFirst();
        }

        historyList.add(task);
    }

    @Override
    public List<BaseTask> getHistory() {
        return List.copyOf(historyList);
    }

    /**
     * Очистка истории
     */
    @Override
    public void clearHistory() {
        historyList.clear();
    }
}
