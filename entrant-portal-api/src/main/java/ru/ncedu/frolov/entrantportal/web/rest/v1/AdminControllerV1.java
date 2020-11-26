package ru.ncedu.frolov.entrantportal.web.rest.v1;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
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
import ru.ncedu.frolov.entrantportal.service.StorageService;
import ru.ncedu.frolov.entrantportal.web.rest.v1.dto.ReportDTO;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@AllArgsConstructor
public class AdminControllerV1 {

    private static final String REPORT_LOCATION = "../python-scripts/reports/";

    private final UserRepository userRepository;
    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;
    private final CourseRepository courseRepository;
    private final RatingService ratingService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final StorageService storageService;

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

    /**
     * Generate report-yyyy-MM-dd.pdf by call python script.
     *
     * @param principal {@link Principal}
     * @return Resource (file) to download.
     * @throws IOException if file not created by python script
     */
    @GetMapping("/applications/reports/create")
    public ResponseEntity<Resource> getReport(Principal principal) throws IOException {
        String token = jwtTokenProvider.createToken(principal.getName(), Role.ADMIN);
        Process process = Runtime.getRuntime().exec("python ../python-scripts/pdf-report.py " + token);
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String filename = "report-" + formatter.format(date) + ".pdf";
        String pathname = REPORT_LOCATION + filename;

        Resource file = storageService.loadAsResource(pathname);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }

    /**
     * Generate list of applications with passed entrants.
     * Method calling from python script.
     *
     * @return {@link ResponseEntity}
     */
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
        if (application.getStatus().equals(Status.REJECTED) || application.getStatus().equals(Status.WAITING)) {
            application.setRating(null);
        }
        Application app = applicationRepository.save(application);
        ratingService.calculate(app);
        return new ResponseEntity<>(app, HttpStatus.OK);
    }

    /**
     * Send email to passed entrants in 3 steps based on previous.
     *
     * @return {@link ResponseEntity}
     */
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
