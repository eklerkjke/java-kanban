package util.converter;

import constans.TaskStatus;
import constans.Type;
import history.TaskHistoryManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Csv {
    public static String historyToString(TaskHistoryManager manager) {
        List<String> historyId = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            historyId.add(String.valueOf(task.getId()));
        }

        return String.join(",", historyId);
    }

    public static List<Integer> historyFromString(String value) {
        String[] arIds = value.split(",");
        List<Integer> taskIdList = new ArrayList<>();
        for (String taskId : arIds) {
            taskIdList.add(Integer.parseInt(taskId));
        }
        return taskIdList;
    }

    public static Task fromString(String value) {
        String[] arTask = value.split(",");

        int id = Integer.parseInt(arTask[0]);
        Type type = Type.valueOf(arTask[1]);
        TaskStatus status = TaskStatus.valueOf(arTask[3]);

        Task task;

        switch (type) {
            case TASK:
                task = new Task(arTask[2], arTask[4], status, LocalDateTime.parse(arTask[5]), Duration.parse(arTask[6]));
                break;
            case SUB_TASK:
                task = new SubTask(arTask[2], arTask[4], status, Integer.parseInt(arTask[7]), LocalDateTime.parse(arTask[5]), Duration.parse(arTask[6]));
                break;
            case EPIC:
                task = new Epic(arTask[2], arTask[4]);
                break;

            default:
                throw new RuntimeException("Ошибка парсинга задачи из строки");
        }

        task.setId(id);

        return task;
    }
}
