package ru.ncedu.frolov.entrantportal.web.rest.v1;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ncedu.frolov.entrantportal.domain.Course;
import ru.ncedu.frolov.entrantportal.domain.Faculty;
import ru.ncedu.frolov.entrantportal.repository.CourseRepository;
import ru.ncedu.frolov.entrantportal.repository.FacultyRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ENTRANT')")
@AllArgsConstructor
public class FacultiesControllerV1 {

    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;

    @GetMapping("/faculties")
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        return new ResponseEntity<>(facultyRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/faculties/{id}")
    public ResponseEntity<List<Course>> getCoursesByFacultyId (@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(courseRepository.findByFaculty_Id(id), HttpStatus.OK);
    }
}
