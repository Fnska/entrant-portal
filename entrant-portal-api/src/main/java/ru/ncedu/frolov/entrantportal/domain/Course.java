package ru.ncedu.frolov.entrantportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.ncedu.frolov.entrantportal.domain.base.AbstractEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course extends AbstractEntity {
    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private Set<Application> applications;

    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @Column(name = "name")
    private String name;

    @Column(name = "seats")
    private Integer seats;

    @Column(name = "left_seats")
    private Integer leftSeats;

    public void addApplication(Application application) {
        this.applications.add(application);
        application.setCourse(this);
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Integer getLeftSeats() {
        return leftSeats;
    }

    public void setLeftSeats(Integer leftSeats) {
        this.leftSeats = leftSeats;
    }
}
