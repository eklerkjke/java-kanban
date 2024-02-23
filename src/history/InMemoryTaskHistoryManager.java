package history;

import manager.TaskManager;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskHistoryManager implements TaskHistoryManager {
    /**
     * Константа, обозначает максимальное количество задач в списке истории
     */
    private final int MAX_VALUE = 10;

    private Map<Integer, Node<Task>> historyMap = new HashMap<>();


    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    @Override
    public void add(TaskManager manager, Task task) {
        if (task.getId() <= 0) {
            return;
        }

        if (size >= MAX_VALUE) {
            return;
        }

        Node<Task> node = new Node<>(task);

        if (historyMap.get(task.getId()) != null) {
            removeNode(node);
        }
        linkLast(node);
    }

    @Override
    public void remove(int id) {
        Node<Task> node = historyMap.get(id);
        if (node != null) {
            removeNode(node);
            --size;
        }
    }

    private void removeNode(Node<Task> node) {
        Task task = node.data;
        historyMap.get(task.getId());

        historyMap.remove(task.getId());
        Node<Task> next = node.next;
        Node<Task> prev = node.prev;

        if (prev != null) {
            next.prev = prev;
        }

        if (next != null) {
            prev.next = next;
        }
    }

    private void linkLast(Node<Task> node) {
        Node<Task> oldTail = tail;
        node.prev = oldTail;
        if (oldTail == null) {
            head = node;
        } else {
            oldTail.next = node;
        }

        tail = node;

        historyMap.put(node.data.getId(), node);

        size++;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> list = new ArrayList<>();

        for (Map.Entry<Integer, Node<Task>> entry : historyMap.entrySet()) {
            list.add(entry.getValue().data);
        }

        return list;
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(getTasks());
    }
}
