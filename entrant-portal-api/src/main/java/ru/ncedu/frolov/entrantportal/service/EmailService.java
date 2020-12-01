package ru.ncedu.frolov.entrantportal.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.ncedu.frolov.entrantportal.domain.Application;
import ru.ncedu.frolov.entrantportal.domain.User;

import java.util.List;

@Service
@AllArgsConstructor
public class EmailService {
    public static final String EMAIL_FROM = "noreply@localhost.com";
    public static final String EMAIL_SUBJECT = "University Admissions Office";
    public static final String EMAIL_SUBJECT_DISABLED = "Your Account Disabled";
    public static final String EMAIL_CONGRATULATION = "Congratulation! You are admitted to the University!";
    public static final String EMAIL_DISABLED = "Sorry! We have to disable your account!\nIf you want to know reasons call us ######.";

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

    public void sendFeedbackThatDisabled(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL_FROM);
        message.setSubject(EMAIL_SUBJECT_DISABLED);
        message.setTo(user.getLogin());
        message.setText(EMAIL_DISABLED);
        emailSender.send(message);
    }
}
