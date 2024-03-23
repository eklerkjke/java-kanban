package model;

import constans.TaskStatus;
import constans.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс, отвечающий за работу подзадач
 */
public class SubTask extends Task {
    /**
     * Родительский эпик, к которому принадлежит подзадача
     */
    private final int parentId;

    public SubTask(String name, String description, TaskStatus status, int parentId, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.parentId = parentId;
    }

    /**
     * Возвращает ID родительского эпика
     * @return ID родительского эпика
     */
    public int getParentId() {
        return parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SubTask subTask = (SubTask) o;
        return super.equals(o) && getParentId() == subTask.getParentId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getParentId());
    }

    @Override
    public Type getType() {
        return Type.SUB_TASK;
    }

    @Override
    public String toString() {
        return String.join(",", super.toString(), String.valueOf(getParentId()));
    }
}
