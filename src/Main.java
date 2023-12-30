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
        Task firstTask = new Task("Задача 1", "Описание 1", TaskStatus.NEW);

        Task secondTask = new Task("Задача 2", "Задача 2", TaskStatus.IN_PROGRESS);

        Epic firstEpic = new Epic("Эпик 1", "Описание Эпика 1");

        Epic secondEpic = new Epic("Эпик 2", "Описание Эпика 2");

        SubTask firstSubTask = new SubTask("Подзадача 1", "Описание 1", TaskStatus.IN_PROGRESS, firstEpic);

        SubTask secondSubTask = new SubTask("Подзадача 2", "Описание 2", TaskStatus.NEW, firstEpic);

        SubTask thirtSubTask = new SubTask("Подзадача 2", "Описание 2", TaskStatus.IN_PROGRESS, secondEpic);

        manager.addTask(firstTask);
        manager.addTask(secondTask);

        manager.addEpic(firstEpic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);

        manager.addEpic(secondEpic);
        manager.addSubTask(thirtSubTask);

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
