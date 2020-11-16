package ru.ncedu.frolov.entrantportal.web.rest.v1.dto;

import lombok.Data;
import ru.ncedu.frolov.entrantportal.domain.Application;
import ru.ncedu.frolov.entrantportal.domain.Course;

import java.util.List;
@Data
public class ReportDTO {
    private List<Application> applications;
    private Course course;
}
