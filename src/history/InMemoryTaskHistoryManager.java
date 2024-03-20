package history;

import manager.TaskManager;
import model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс отвечающий за хранение истории задач
 */
public class InMemoryTaskHistoryManager implements TaskHistoryManager {

    /**
     * Свойство для хранения задачп
     */
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();

    /**
     * Свойство "головы" истории задач
     */
    private Node<Task> head;

    /**
     * Свойство "хвоста" истории задач
     */
    private Node<Task> tail;

    /**
     * Добавления записи в историю
     * @param manager Менеджер задач
     * @param task задача
     */
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

    /**
     * Удаления записи из истории
     * @param id ID задачи
     */
    @Override
    public void remove(int id) {
        Node<Task> node = historyMap.get(id);
        if (node != null) {
            removeNode(node);
        }
    }

    /**
     * Удаление ноды из мапы
     * @param node
     */
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

    /**
     * Добавление ноды в конец списка
     * @param node
     */
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
    }

    /**
     * Возвращает список задач в истории из мапы
     * @return список истории задач
     */
    public List<Task> getTasks() {
        return historyMap
                .values()
                .stream()
                .map(Node::getData)
                .collect(Collectors.toList());
    }

    /**
     * Вовзвращает список истории задач
     * @return список истории задач
     */
    @Override
    public List<Task> getHistory() {
        return List.copyOf(getTasks());
    }
}
