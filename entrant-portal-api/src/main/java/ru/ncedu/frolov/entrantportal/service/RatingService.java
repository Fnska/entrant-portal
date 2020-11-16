package ru.ncedu.frolov.entrantportal.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ncedu.frolov.entrantportal.domain.Application;
import ru.ncedu.frolov.entrantportal.domain.Course;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class RatingService {

    private final ApplicationService applicationService;

    public void calculate(Application application) {
        Course course = application.getCourse();
        List<Application> sortedApplications = applicationService.getSortedApplicationsByCoursePriorityAndExam(course)
                .collect(Collectors.toList());
        calculateRating(course, sortedApplications);
        applicationService.saveAll(sortedApplications);
    }

    private void calculateRating(Course course, List<Application> applications) {
        int seats = course.getSeats();
        double rating;
        for (Application app : applications) {
            int position = applications.indexOf(app);
            rating = 1 - ((double) position / seats);
            app.setRating(rating);
        }
    }

}
