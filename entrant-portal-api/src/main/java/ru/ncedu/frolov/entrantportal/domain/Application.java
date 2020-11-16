package ru.ncedu.frolov.entrantportal.domain;

import ru.ncedu.frolov.entrantportal.domain.base.AbstractEntity;
import ru.ncedu.frolov.entrantportal.domain.enums.Priority;
import ru.ncedu.frolov.entrantportal.domain.enums.Status;

import javax.persistence.*;

@Entity
@Table(name = "applications")
public class Application extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "priority")
    private Priority priority;

    @Column(name = "rating")
    private Double rating;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(name = "exam_score")
    private Long examScore;


    public Long getExamScore() {
        return examScore;
    }

    public void setExamScore(Long examScore) {
        this.examScore = examScore;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
