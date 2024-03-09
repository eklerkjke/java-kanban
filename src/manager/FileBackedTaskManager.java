package manager;

import exceptions.ManagerSaveException;
import history.TaskHistoryManager;
import model.Epic;
import model.SubTask;
import model.Task;
import provider.Managers;
import util.converter.Csv;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(TaskHistoryManager historyManager) {
        super(historyManager);
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager self = new FileBackedTaskManager(Managers.getDefaultHistory());

        self.setFile(file);

        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            List<String> arLines = new ArrayList<>();
            while (br.ready()) {
                arLines.add(br.readLine());
            }

            if (arLines.isEmpty()) {
                return self;
            }

            String historyStr = arLines.get(arLines.size() - 1);
            List<Integer> historyIds = new ArrayList<>();
            if (!historyStr.isEmpty()) {
                historyIds = Csv.historyFromString(historyStr);
                arLines.remove(arLines.size() - 1);
            }

            for (String line : arLines) {
                if (line.isEmpty() || line.equals("\n")) {
                    continue;
                }

                Task task = Csv.fromString(line);
                if (task instanceof Epic) {
                    self.epicList.put(task.getId(), (Epic) task);
                } else if (task instanceof SubTask) {
                    self.subTaskList.put(task.getId(), (SubTask) task);
                } else {
                    self.taskList.put(task.getId() ,task);
                }
            }

            if (!historyIds.isEmpty()) {
                for (Integer taskId : historyIds) {
                    if (self.taskList.containsKey(taskId)) {
                        self.historyManager.add(self, self.getTaskById(taskId));
                    } else if (self.subTaskList.containsKey(taskId)) {
                        self.historyManager.add(self, self.getSubTaskById(taskId));
                    } else if (self.epicList.containsKey(taskId)) {
                        self.historyManager.add(self, self.getEpicById(taskId));
                    }
                }
            }

        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка загрузки задач", exception);
        }

        return self;
    }

    private void save() {
        File file = getFile();

        try (BufferedWriter br = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, false))) {
            for (Task task : getTaskList()) {
                br.write(task.toString());
                br.newLine();
            }
            for (Epic epic : getEpicList()) {
                br.write(epic.toString());
                br.newLine();
            }
            for (SubTask subTask : getSubTaskList()) {
                br.write(subTask.toString());
                br.newLine();
            }

            br.newLine();

            br.write(Csv.historyToString(historyManager));
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка сохранения задач", exception);
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void removeTaskById(Integer id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubTaskById(Integer id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(Integer id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeAll() {
        super.removeAll();
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubTasks() {
        super.removeSubTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }
}
