package ru.ncedu.frolov.entrantportal.domain.enums;

public enum Priority {
    HIGH(1),
    MEDIUM(2),
    LOW(3);

    private Integer priority;

    Priority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }
}
