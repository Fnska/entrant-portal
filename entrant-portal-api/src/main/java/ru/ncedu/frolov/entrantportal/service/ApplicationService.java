package ru.ncedu.frolov.entrantportal.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ncedu.frolov.entrantportal.domain.Application;
import ru.ncedu.frolov.entrantportal.domain.Course;
import ru.ncedu.frolov.entrantportal.domain.enums.Priority;
import ru.ncedu.frolov.entrantportal.domain.enums.Status;
import ru.ncedu.frolov.entrantportal.repository.ApplicationRepository;
import ru.ncedu.frolov.entrantportal.repository.CourseRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CourseRepository courseRepository;


    public Stream<Application> getSortedApplicationsByCourseExam(Course course) {
        return getSortedApplicationsByCourseExam(course.getId());
    }

    public Stream<Application> getSortedApplicationsByCourseExam(Long courseId) {
        return applicationRepository.findAllByCourse_Id(courseId).stream()
                .sorted(Comparator.comparing(Application::getExamScore).reversed());
    }

    public Stream<Application> getSortedApplicationsByCoursePriorityAndExam(Course course) {
        return getSortedApplicationsByCoursePriorityAndExam(course.getId());
    }

    public Stream<Application> getSortedApplicationsByCoursePriorityAndExam(Long courseId) {
        List<Application> applications = applicationRepository.findAllByCourse_Id(courseId);
        Comparator<Application> compareByPriorityAndExam = Comparator
                .comparing(Application::getPriority)
                .thenComparing(Comparator.comparing(Application::getExamScore).reversed());

        return applications.stream()
                .sorted(compareByPriorityAndExam);
    }

    public List<Application> saveAll(List<Application> applications) {
        return applicationRepository.saveAll(applications);
    }

    /**
     * Generate list of applications to send emails based on priority that not used in previous wave.
     *
     * @param prevWaveApplications previous wave of applications with sent emails
     * @param priority {@link Priority}
     * @return new list of applications
     */
    public List<Application> nextWaveApplications(final List<Application> prevWaveApplications,
                                                  Priority priority) {
        List<Course> courses = courseRepository.findAll();
        List<Application> newWaveApplications = new ArrayList<>();
        for (Course course : courses) {
            List<Application> sortedApplications = getSortedApplicationsByCoursePriorityAndExam(course)
                    .collect(Collectors.toList());
            List<Application> notSentApplications = sortedApplications.stream()
                    .filter(a -> !containsUserId(prevWaveApplications, a.getUser().getId()))
                    .filter(a -> a.getStatus().equals(Status.APPROVED))
                    .collect(Collectors.toList());

            int leftSeats = course.getLeftSeats();
            int countSentApplications = 0;
            for (Application app : notSentApplications) {
                if (notSentApplications.indexOf(app) < leftSeats) {
                    if (app.getPriority().equals(priority)) {
                        app.setStatus(Status.ADMITTED);
                        newWaveApplications.add(app);
                        countSentApplications++;
                    }
                }
            }
            course.setLeftSeats(leftSeats - countSentApplications);
            courseRepository.save(course);
        }
        return newWaveApplications;
    }

    private static boolean containsUserId(final List<Application> list, final Long id) {
        return list.stream().anyMatch(a -> a.getUser().getId().equals(id));
    }
}
