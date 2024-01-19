package model;

import constans.TaskStatus;
import java.util.Objects;

/**
 * Класс отвечающий за работу обычной задачи
 */
public class Task implements BaseTask {
    /**
     * Название задачи
     */
    protected String name;

    /**
     * Описание задачи
     */
    protected String description;

    /**
     * ID задачи
     */
    protected int id;

    /**
     * Статус задачи
     */
    protected TaskStatus status;

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }
    
    /**
     * Возвращает название задачи
     * @return название задачи
     */
    public String getName() {
        return name;
    }

    /**
     * Задает название задачи
     * @param name название задачи
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает описание задачи
     * @return описание задачи
     */
    public String getDescription() {
        return description;
    }

    /**
     * Задает описание задачи
     * @param description описание задачи
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Вовзрашает ID задачи
     * @return ID задачи
     */
    public int getId() {
        return id;
    }

    /**
     * Задает ID задачи
     * @param id ID задачи
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Возвращает статус задачи
     * @return статус задачи
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Задает статус задачи
     * @param status статус задачи
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    @Override
    public String toString() {
        return "Задача: " +
                "нзвание='" + getName() + '\'' +
                ", описание='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", статус=" + getStatus();
    }
}
