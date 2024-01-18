package history;

import model.Task;

import java.util.ArrayList;

public class InMemoryTaskHistoryManager implements TaskHistoryManager<Task> {

    private ArrayList<Task> historyList = new ArrayList<>();


    @Override
    public void add(Task task) {
        if (historyList.size() >= 10) {
            historyList.removeFirst();
        }

        historyList.add(task);
    }

    public ArrayList<Task> getHistory() {
        return historyList;
    }
}
