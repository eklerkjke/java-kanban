import java.util.Objects;

public class SubTask extends Task {
    private Epic parent;

    public int getParentId() {
        return parent.getId();
    }

    public Epic getParent() {
        return parent;
    }

    public void setParent(Epic parent) {
        this.parent = parent;
    }

    @Override
    public void setStatus(TaskStatus status) {
        super.setStatus(status);
        Epic epic = (Epic)TaskManager.getTaskById(getParentId());
        epic.updateStatus();
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
