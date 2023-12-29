import constans.TaskStatus;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

/**
 * Интрефейс взаимодействия
 */
public class Main {

    /**
     * Главный метод запуска
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Поехали!");

        // Инициализируем менеджер
        TaskManager manager = new TaskManager();

        // Создание задач
        Task firstTask = new Task();
        firstTask.setName("Задача 1");
        firstTask.setDescription("Описание 1");
        firstTask.setStatus(TaskStatus.NEW);

        Task secondTask = new Task();
        secondTask.setName("Задача 2");
        secondTask.setDescription("Описание 2");
        secondTask.setStatus(TaskStatus.IN_PROGRESS);

        Epic firstEpic = new Epic();
        firstEpic.setName("Эпик 1");
        firstEpic.setDescription("Описание Эпика 1");

        Epic secondEpic = new Epic();
        secondEpic.setName("Эпик 2");
        secondEpic.setDescription("Описание Эпика 2");

        SubTask firstSubTask = new SubTask();
        firstSubTask.setName("Подзадача 1");
        firstSubTask.setDescription("Описание 1");
        firstSubTask.setParent(firstEpic);
        firstSubTask.setStatus(TaskStatus.IN_PROGRESS);

        SubTask secondSubTask = new SubTask();
        secondSubTask.setName("Подзадача 2");
        secondSubTask.setDescription("Описание 2");
        secondSubTask.setParent(firstEpic);
        secondSubTask.setStatus(TaskStatus.NEW);

        SubTask thirtSubTask = new SubTask();
        thirtSubTask.setName("Подзадача 2");
        thirtSubTask.setDescription("Описание 2");
        thirtSubTask.setParent(secondEpic);
        thirtSubTask.setStatus(TaskStatus.IN_PROGRESS);

        manager.addTask(firstTask);
        manager.addTask(secondTask);

        manager.addEpic(firstEpic);
        manager.addSubTask(firstEpic.getId(), firstSubTask);
        manager.addSubTask(firstEpic.getId(), secondSubTask);

        manager.addEpic(secondEpic);
        manager.addSubTask(secondEpic.getId(), thirtSubTask);

        // печать
        print(manager);

        // смена статусов
        firstEpic.setStatus(TaskStatus.IN_PROGRESS);
        secondTask.setStatus(TaskStatus.DONE);
        firstSubTask.setStatus(TaskStatus.DONE);
        secondSubTask.setStatus(TaskStatus.IN_PROGRESS);
        thirtSubTask.setStatus(TaskStatus.DONE);

        print(manager);

        // удаление
        manager.removeTaskById(secondTask.getId());
        manager.removeEpicById(firstEpic.getId());

        print(manager);
    }

    /**
     * Вывод в консоль всех задач
     * @param manager менеджер задач
     */
    public static void print(TaskManager manager) {
        for (Task task : manager.getTaskList()) {
            System.out.println(task);
        }
        for (Epic epic : manager.getEpicList()) {
            System.out.println(epic);
        }
        for (SubTask subTask : manager.getSubTaskList()) {
            System.out.println(subTask);
        }
    }
}
