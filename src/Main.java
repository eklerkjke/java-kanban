import constans.TaskStatus;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import provider.Managers;

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
        TaskManager manager = Managers.getDefault();

        // Создание задач
        Task firstTask = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        manager.addTask(firstTask);

        Task secondTask = new Task("Задача 2", "Задача 2", TaskStatus.IN_PROGRESS);
        manager.addTask(secondTask);

        Epic firstEpic = new Epic("Эпик 1", "Описание Эпика 1");
        manager.addEpic(firstEpic);

        Epic secondEpic = new Epic("Эпик 2", "Описание Эпика 2");
        manager.addEpic(secondEpic);

        SubTask firstSubTask = new SubTask("Подзадача 1", "Описание 1", TaskStatus.IN_PROGRESS, firstEpic.getId());
        manager.addSubTask(firstSubTask);

        SubTask secondSubTask = new SubTask("Подзадача 2", "Описание 2", TaskStatus.NEW, firstEpic.getId());
        manager.addSubTask(secondSubTask);

        SubTask thirtSubTask = new SubTask("Подзадача 2", "Описание 2", TaskStatus.IN_PROGRESS, secondEpic.getId());
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
    private static void print(TaskManager manager) throws Exception {
        System.out.println("Задачи:");
        for (Task task : manager.getTaskList()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpicList()) {
            System.out.println(epic);

            for (Task task : manager.getSubTasksEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTaskList()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
