package model;

import constans.TaskStatus;
import java.util.Objects;

/**
 * Класс, отвечающий за работу подзадач
 */
public class SubTask extends Task {
    /**
     * Родительский эпик, к которому принадлежит подзадача
     */
    private Epic parent;

    public SubTask(String name, String description, TaskStatus status, Epic epic) {
        super(name, description, status);
        this.parent = epic;
    }

    /**
     * Возвращает ID родительского эпика
     * @return ID родительского эпика
     */
    public int getParentId() {
        return parent.getId();
    }

    /**
     * Возвращает родительский эпик
     * @return родительский эпик
     */
    public Epic getParent() {
        return parent;
    }

    /**
     * Задает родительский эпик
     * @param parent родительский эпик
     */
    public void setParent(Epic parent) {
        this.parent = parent;
    }

    @Override
    public void setStatus(TaskStatus status) {
        super.setStatus(status);
        parent.updateStatus();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return super.equals(o) && getParentId() == subTask.getParentId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getParentId());
    }

    @Override
    public String toString() {
        return "Подзача: " +
                "id эпика='" + getParentId() + '\'' +
                ", нзвание='" + getName() + '\'' +
                ", описание='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", статус=" + getStatus();
    }
}
