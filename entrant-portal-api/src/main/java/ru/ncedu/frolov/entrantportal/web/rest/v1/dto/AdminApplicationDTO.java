package ru.ncedu.frolov.entrantportal.web.rest.v1.dto;

import lombok.Data;
import ru.ncedu.frolov.entrantportal.domain.enums.Status;

@Data
public class AdminApplicationDTO {
    private Status status;
}
