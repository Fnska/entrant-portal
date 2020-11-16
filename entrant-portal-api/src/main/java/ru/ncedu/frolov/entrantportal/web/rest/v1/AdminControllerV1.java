package ru.ncedu.frolov.entrantportal.web.rest.v1;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ncedu.frolov.entrantportal.domain.Application;
import ru.ncedu.frolov.entrantportal.domain.Course;
import ru.ncedu.frolov.entrantportal.domain.User;
import ru.ncedu.frolov.entrantportal.domain.enums.Priority;
import ru.ncedu.frolov.entrantportal.domain.enums.Role;
import ru.ncedu.frolov.entrantportal.domain.enums.Status;
import ru.ncedu.frolov.entrantportal.repository.ApplicationRepository;
import ru.ncedu.frolov.entrantportal.repository.CourseRepository;
import ru.ncedu.frolov.entrantportal.repository.UserRepository;
import ru.ncedu.frolov.entrantportal.security.jwt.JwtTokenProvider;
import ru.ncedu.frolov.entrantportal.service.ApplicationService;
import ru.ncedu.frolov.entrantportal.service.EmailService;
import ru.ncedu.frolov.entrantportal.service.RatingService;
import ru.ncedu.frolov.entrantportal.web.rest.v1.dto.ReportDTO;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@AllArgsConstructor
public class AdminControllerV1 {

    private final UserRepository userRepository;
    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;
    private final CourseRepository courseRepository;
    private final RatingService ratingService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/applications")
    public ResponseEntity<Page<Application>> getApplications(Pageable pageable) {
        return new ResponseEntity<>(applicationRepository.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/applications/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable(name = "id") Long id) {
        Optional<Application> appById = applicationRepository.findById(id);
        if (appById.isPresent()) {
            Application application = appById.get();
            return new ResponseEntity<>(application, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/applications/reports/create")
    public ResponseEntity<?> getReport(Principal principal) throws IOException {
        String token = jwtTokenProvider.createToken(principal.getName(), Role.ADMIN);
        Runtime.getRuntime().exec("python ../python-scripts/pdf-report.py " + token);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/applications/reports")
    public ResponseEntity<List<ReportDTO>> getApplicationsByCourseId() {
        List<ReportDTO> reports = new ArrayList<>();
        List<Course> courses = courseRepository.findAll();
        for (Course course : courses) {
            ReportDTO reportDTO = new ReportDTO();
            reportDTO.setApplications(applicationService.getSortedApplicationsByCourseExam(course)
                    .filter(a -> a.getStatus().equals(Status.ADMITTED))
                    .collect(Collectors.toList()));
            reportDTO.setCourse(course);
            reports.add(reportDTO);
        }
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @PostMapping("/applications")
    public ResponseEntity<Application> saveApplications(@RequestBody Application application) {
        Application app = applicationRepository.save(application);
        ratingService.calculate(app);
        return new ResponseEntity<>(app, HttpStatus.OK);
    }

    @PostMapping("/complete")
    public ResponseEntity<?> sendFeedbackToAdmittedEntrants() {
        List<Application> firstWaveApplications = applicationRepository.findAllByRatingGreaterThanAndPriority(0.0, Priority.HIGH);
        for (Application app : firstWaveApplications) {
            app.setStatus(Status.ADMITTED);
            app.getCourse().setLeftSeats(app.getCourse().getLeftSeats() - 1);
            courseRepository.save(app.getCourse());
        }
        applicationService.saveAll(firstWaveApplications);
        emailService.sendFeedbackToAdmittedEntrants(firstWaveApplications);

        List<Application> secondWaveApplications = applicationService.nextWaveApplications(firstWaveApplications, Priority.MEDIUM);
        applicationService.saveAll(secondWaveApplications);
        emailService.sendFeedbackToAdmittedEntrants(secondWaveApplications);

        secondWaveApplications.addAll(firstWaveApplications);
        List<Application> thirdWaveApplications = applicationService.nextWaveApplications(secondWaveApplications, Priority.LOW);
        applicationService.saveAll(thirdWaveApplications);
        emailService.sendFeedbackToAdmittedEntrants(thirdWaveApplications);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}