package ru.ncedu.frolov.entrantportal.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.ncedu.frolov.entrantportal.domain.Application;

import java.util.List;

@Service
@AllArgsConstructor
public class EmailService {
    public static final String EMAIL_FROM = "noreply@localhost.com";
    public static final String EMAIL_SUBJECT = "University Admissions Office";
    public static final String EMAIL_CONGRATULATION = "Congratulation! You are admitted to the University!";

    private final JavaMailSender emailSender;

    public void sendFeedbackToAdmittedEntrants(List<Application> applications) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL_FROM);
        message.setSubject(EMAIL_SUBJECT);

        for (Application app : applications) {
            message.setTo(app.getUser().getLogin());
            message.setText(EMAIL_CONGRATULATION +
                    "\nYour course is" +
                    " " + app.getCourse().getFaculty().getName() +
                    " " + app.getCourse().getName() +
                    " " + app.getPriority().name() +
                    "\nVisit our office to sign the application."
                    );
            emailSender.send(message);
        }
    }
}
