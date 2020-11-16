package ru.ncedu.frolov.entrantportal.web.rest.v1;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ncedu.frolov.entrantportal.domain.Application;
import ru.ncedu.frolov.entrantportal.domain.Course;
import ru.ncedu.frolov.entrantportal.domain.User;
import ru.ncedu.frolov.entrantportal.domain.enums.Status;
import ru.ncedu.frolov.entrantportal.repository.ApplicationRepository;
import ru.ncedu.frolov.entrantportal.repository.CourseRepository;
import ru.ncedu.frolov.entrantportal.repository.UserRepository;
import ru.ncedu.frolov.entrantportal.web.rest.v1.dto.UserApplicationDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users/{id}")
@PreAuthorize("hasRole('ROLE_ADMIN') or @authenticationService.isOwner(principal.username, #id)")
@AllArgsConstructor
public class UserApplicationControllerV1 {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ApplicationRepository applicationRepository;

    @GetMapping("/applications")
    public ResponseEntity<List<Application>> getUserApplications(@PathVariable(name = "id") Long id) {
        List<Application> applications = applicationRepository.findAllByUser_Id(id);
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    @PostMapping("/applications")
    public ResponseEntity<Application> saveUserApplication(@PathVariable(name = "id") Long id,
                                                           @RequestBody UserApplicationDTO dto) {
        Optional<User> userById = userRepository.findOneWithApplicationsById(id);
        Optional<Course> courseById = courseRepository.findOneWithApplicationsById(dto.getCourseId());
        if (userById.isPresent() && courseById.isPresent()) {
            User user = userById.get();
            Course course = courseById.get();

            Application application = new Application();
            application.setStatus(Status.WAITING);
            application.setPriority(dto.getPriority());
            application.setExamScore(0L);
            user.addApplication(application);
            course.addApplication(application);
            applicationRepository.save(application);
            return new ResponseEntity<>(application, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
