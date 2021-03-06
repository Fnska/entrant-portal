package ru.ncedu.frolov.entrantportal.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ncedu.frolov.entrantportal.domain.Application;
import ru.ncedu.frolov.entrantportal.domain.Course;
import ru.ncedu.frolov.entrantportal.domain.enums.Status;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class RatingService {

    private final ApplicationService applicationService;

    /**
     * Recalculates the rating to all applications at this course
     *
     * @param application user's application to course
     */
    public void calculate(Application application) {
        Course course = application.getCourse();
        List<Application> sortedApplications = applicationService.getSortedApplicationsByCoursePriorityAndExam(course)
                .filter(a -> a.getStatus().equals(Status.APPROVED))
                .collect(Collectors.toList());
        calculateRating(course, sortedApplications);
        applicationService.saveAll(sortedApplications);
    }

    /**
     * Calculate rating for list of applications. Formula for calculating this:
     * rating = 1 - (position in sorted list) / (number of seats)
     * So rating can be between -infinity and 1.
     *
     * @param course direction of faculty
     * @param applications user's application to course
     */
    private void calculateRating(Course course, List<Application> applications) {
        int seats = course.getSeats();
        double rating;
        for (Application app : applications) {
            int position = applications.indexOf(app);
            rating = 1 - ((double) position / seats);
            app.setPosition(position);
            app.setRating(rating);
        }
    }

}
