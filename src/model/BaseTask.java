package model;

import constans.TaskStatus;

/**
 * Интерфейс для работы с задачами
 */
public interface BaseTask {
    /**
     * Возвращает название задачи
     * @return название задачи
     */
    String getName();

    /**
     * Устанавливает название задачи
     * @param name название задачи
     */
    void setName(String name);

    /**
     * Возвращает описание задачи
     * @return описание задачи
     */
    String getDescription();

    /**
     * Устанавливает описание задачи
     * @param description описание задачи
     */
    void setDescription(String description);

    /**
     * Возвращает ID задачи
     * @return ID Задачи
     */
    int getId();

    /**
     * Устанавливает ID задачи
     * @param id ID задачи
     */
    void setId(int id);

    /**
     * Возвращает статус задачи
     * @return статус задачи
     */
    TaskStatus getStatus();

    /**
     * Устанавливает статус задачи
     * @param status статус задачи
     */
    void setStatus(TaskStatus status);

}
