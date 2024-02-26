package history;

import manager.TaskManager;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskHistoryManager implements TaskHistoryManager {

    private Map<Integer, Node<Task>> historyMap = new HashMap<>();

    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    @Override
    public void add(TaskManager manager, Task task) {
        if (task.getId() <= 0) {
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
        Task task = node.getData();
        historyMap.get(task.getId());

        historyMap.remove(task.getId());
        Node<Task> next = node.getNext();
        Node<Task> prev = node.getPrev();

        if (prev == null && next == null) {
            head = null;
            tail = null;
        } else if (prev == null) {
            head = next;
            next.setPrev(null);
        } else if (next == null) {
            tail = prev;
            prev.setNext(null);
        } else {
            prev.setNext(next);
            next.setPrev(prev);
        }
    }

    private void linkLast(Node<Task> node) {
        Node<Task> oldTail = tail;
        node.setPrev(oldTail);
        if (oldTail == null) {
            head = node;
        } else {
            oldTail.setNext(node);
        }

        tail = node;

        historyMap.put(node.getData().getId(), node);

        size++;
    }

    public List<Task> getTasks() {
        List<Task> list = new ArrayList<>();

        for (Map.Entry<Integer, Node<Task>> entry : historyMap.entrySet()) {
            list.add(entry.getValue().getData());
        }

        return list;
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(getTasks());
    }
}
