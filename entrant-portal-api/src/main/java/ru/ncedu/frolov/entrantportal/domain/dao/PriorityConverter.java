package ru.ncedu.frolov.entrantportal.domain.dao;

import ru.ncedu.frolov.entrantportal.domain.enums.Priority;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class PriorityConverter implements AttributeConverter<Priority, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Priority priority) {
        if (priority == null) {
            return null;
        }
        return priority.getPriority();
    }

    @Override
    public Priority convertToEntityAttribute(Integer priority) {
        if (priority == null) {
            return null;
        }
        return Stream.of(Priority.values())
                .filter(p -> p.getPriority().equals(priority))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
