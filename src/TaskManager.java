import java.util.HashMap;

public class TaskManager {

    private static int idTasks = 0;

    private static HashMap<Integer, Task> taskList = new HashMap<>();

    private static HashMap<Integer, SubTask> subTaskList = new HashMap<>();

    private static HashMap<Integer, Epic> epicList = new HashMap<>();


    protected static int getNextTaskId() {
        ++idTasks;
        return idTasks;
    }

    public static HashMap<Integer, Task> getTaskList() {
        return taskList;
    }

    public static HashMap<Integer, SubTask> getSubTaskList() {
        return subTaskList;
    }

    public static HashMap<Integer, Epic> getEpicList() {
        return epicList;
    }

    public static void createTask(String name, TaskStatus status, String description) {
        Task task = new Task();
        task.setId(getNextTaskId());
        task.setName(name);
        task.setStatus(status);
        task.setDescription(description);

        getTaskList().put(task.getId(), task);
    }

    public static void createEpic(String name, TaskStatus status, String description) {
        Epic epic = new Epic();
        epic.setId(getNextTaskId());
        epic.setName(name);
        epic.setStatus(status);
        epic.setDescription(description);

        getEpicList().put(epic.getId(), epic);
    }

    public static void createSubtask(int epicId, String name, TaskStatus status, String description) {
        SubTask subTask = new SubTask();
        subTask.setId(getNextTaskId());
        subTask.setParent(getEpicById(epicId));
        subTask.setName(name);
        subTask.setStatus(status);
        subTask.setDescription(description);

        Epic epic = (Epic)getTaskById(epicId);
        epic.addSubTask(subTask);

        getSubTaskList().put(subTask.getId(), subTask);
    }

    public static Task getTaskById(int id) {
        return getTaskList().get(id);
    }

    public static SubTask getSubTaskById(int id) {
        return getSubTaskList().get(id);
    }

    public static Epic getEpicById(int id) {
        return getEpicList().get(id);
    }

    public static void removeTaskById(Integer id) {
        getTaskList().remove(id);
    }

    public static void removeSubTaskById(Integer id) {
        SubTask subTask = getSubTaskById(id);
        Epic epic = subTask.getParent();
        epic.removeSubTask(id);
        getSubTaskList().remove(id);
    }

    public static void removeEpicById(Integer id) {
        Epic epic = getEpicById(id);
        for (SubTask subTask : epic.getSubTasks()) {
            removeSubTaskById(subTask.getId());
        }
        getEpicList().remove(id);
    }

    public static boolean has(int id) {
        return (hasTask(id) || hasSubTask(id) || hasEpic(id));
    }

    public static boolean hasTask(int id) {
        return getTaskList().containsKey(id);
    }

    public static boolean hasSubTask(int id) {
        return getSubTaskList().containsKey(id);
    }

    public static boolean hasEpic(int id) {
        return getEpicList().containsKey(id);
    }

    public static void printTasks() {
        for (Task task : getTaskList().values()) {
            System.out.println(task);
        }
        for (Epic epic : getEpicList().values()) {
            System.out.println(epic);
        }
        for (SubTask subTask : getSubTaskList().values()) {
            System.out.println(subTask);
        }
    }
}
