package model;

import constans.TaskStatus;
import constans.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс отвечающий за работу обычной задачи
 */
public class Task {
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

    protected Duration duration;

    protected LocalDateTime startTime;

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, TaskStatus status, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
    }

    public Task(String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
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
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;

        if (id == task.id) {
            return true;
        }

        return Objects.equals(name, task.name)
            && Objects.equals(description, task.description)
            && status == task.status
            && getType() == task.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    public Type getType() {
        return Type.TASK;
    }

    @Override
    public String toString() {
        return String.join(
            ",",
            String.valueOf(getId()),
            String.valueOf(getType()),
            getName(),
            String.valueOf(getStatus()),
            getDescription(),
            (startTime != null) ? startTime.toString() : "",
            (duration != null) ? duration.toString() : ""
        );
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        } else {
            return startTime.plus(duration);
        }
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
