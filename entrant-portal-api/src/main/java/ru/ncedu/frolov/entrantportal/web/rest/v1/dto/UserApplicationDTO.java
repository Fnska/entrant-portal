package ru.ncedu.frolov.entrantportal.web.rest.v1.dto;

import lombok.Data;
import ru.ncedu.frolov.entrantportal.domain.enums.Priority;

@Data
public class UserApplicationDTO {
    private Priority priority;
    private Long courseId;
}
